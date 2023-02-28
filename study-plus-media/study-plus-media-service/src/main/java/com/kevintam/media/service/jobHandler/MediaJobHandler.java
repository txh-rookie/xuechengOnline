package com.kevintam.media.service.jobHandler;

import com.kevintam.media.model.po.MediaProcess;
import com.kevintam.media.service.BigUploadFileService;
import com.kevintam.media.service.MediaProcessService;
import com.kevintam.study.exception.StudyException;
import com.kevintam.study.utils.Mp4VideoUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/27
 */
@Component
@Slf4j
public class MediaJobHandler {

    @Autowired
    private MediaProcessService mediaProcessService;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private MinioClient minioClient;

    @Autowired
    private BigUploadFileService bigUploadFileService;


    //视频处理的业务
    @XxlJob("mediaJobHandler")
    public void shardingJobHandler() throws Exception {
        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        int count = 2;//根据我们的核心线程来进行处理，最好不要超过我们的核心线程数
        //去拿到我们的分片参数,去编写我们的业务类
        //0、任务调度中心，去调度我们的执行器，去执行任务
        //1、去数据库中拿到我们的需要处理的视频资源
        List<MediaProcess> mediaProcessList = mediaProcessService.getMediaProcessList(shardIndex, shardTotal, count);
        if (mediaProcessList == null || mediaProcessList.size() <= 0) {
            log.debug("没有需要处理的任务数....");
            return;
        }
        CountDownLatch countDownLatch = new CountDownLatch(mediaProcessList.size());
        //2、拿到所有待处理的任务后，去利用线程池来进行处理,去minio中将视频资源进行下载。
        mediaProcessList.forEach(mediaProcess -> {
            threadPoolExecutor.execute(() -> {
                //0、需要进行一个幂等性的校验,通过我们的数据库的一个字段来进行幂等姓的校验
                String status = mediaProcess.getStatus();
                if ("2".equals(status)) {//表示该视频已经处理过了，不需要再处理了
                    log.debug("该视频已经处理过：", mediaProcess);
                    countDownLatch.countDown();
                    return;
                }
                String bucket = mediaProcess.getBucket();
                String fileId = mediaProcess.getFileId();
                String filePath = mediaProcess.getFilePath();
                String filename = mediaProcess.getFilename();
                //需要下载下来，定义两个file，一个file用于接收下载下来的文件，一个用于编码生成之后的文件
                File sourceFile = null;
                File mp4File = null;
                FileOutputStream outputStream = null;
                try {
                    GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucket).object(filePath).build();
                    InputStream stream = minioClient.getObject(getObjectArgs);
                    sourceFile = File.createTempFile("source", null);
                    mp4File = File.createTempFile("mp4", ".mp4");
                    //1、将视频文件下载下来。
                    outputStream = new FileOutputStream(sourceFile);
                    IOUtils.copy(stream, outputStream);//将文件下载到sourceFile中
                    //2、去用我们的ffmpeg来进行转码
                    String ffmpeg_path = "ffmpeg";//ffmpeg的配置环境变量
                    //源avi视频的路径
                    //转换后mp4文件的名称
                    String mp4_name = filename+".mp4";
                    //转换后mp4文件的路径
                    //创建工具类对象
                    Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpeg_path,sourceFile.getAbsolutePath(),mp4_name,mp4File.getAbsolutePath());
                    //开始视频转换，成功将返回success
                    String result = videoUtil.generateMp4();
                    String url=null;
                    String statusCode="3";
                    if("success".equals(result)){
                        //表示我们转换成功,将我们的上传的信息上传到minio中
                        String filePath1 = getFilePath(fileId, ".mp4");
                        //将文件进行上传
                        bigUploadFileService.uploadBigFiles(mp4File.getAbsolutePath(),bucket,filePath1);
                        url="/"+bucket+"/"+filePath1;
                        statusCode="2";
                    }
                    //视频上传成功，需要进行记录
                    //3、下载完成之后，去利用ffmpeg去处理视频文件进行转码
                    //4、将转码成功的文件会传到我们的minio中。然后去修改一下数据库表的数据
                    //5、然后还要将我们的处理完后的数据写入到数据库表中。
                    mediaProcessService.updateMediaProcessStatus(mediaProcess.getId(),url,fileId,statusCode,result);
                    //3、将转码过后的数据放到我们的
                } catch (Exception e) {
                    StudyException.cast("下载文件出错:" + e.getMessage());
                    countDownLatch.countDown();
                    return;
                }
                //减一
               countDownLatch.countDown();
            });
        });
        //当我们任务执行30分钟之后，这个任务会被自动释放掉,而不是一直在这阻塞住。
        countDownLatch.await(30, TimeUnit.MINUTES);//当我们的计数器归零表示我们的任务已被执行完毕

    }
    private String getFilePath(String fileMd5,String fileExt){
        return   fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/" +fileMd5 +fileExt;
    }
}
