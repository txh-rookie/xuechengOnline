package com.kevintam.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kevintam.media.mapper.MediaFilesProcessMapper;
import com.kevintam.media.mapper.MediaProcessHistoryMapper;
import com.kevintam.media.model.po.MediaProcess;
import com.kevintam.media.model.po.MediaProcessHistory;
import com.kevintam.media.service.MediaProcessService;
import com.kevintam.study.exception.StudyException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/27
 */
@Service
public class MediaProcessServiceImpl implements MediaProcessService {

    @Autowired
    private MediaFilesProcessMapper mediaFilesProcessMapper;

    @Autowired
    private MediaProcessHistoryMapper mediaProcessHistoryMapper;

    /**
     * 查询待处理任务
     * @param shardIndex
     * @param shardTotal
     * @param count
     * @return
     */
    @Override
    public List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count) {

        return mediaFilesProcessMapper.getMediaProcessList(shardIndex,shardTotal,count);
    }

    @Transactional
    @Override
    public void updateMediaProcessStatus(Long id, String url, String fileId, String status, String message) {
        //1、根据任务id查询待处理的视频
        MediaProcess taskMedia = mediaFilesProcessMapper.selectById(id);
        if(taskMedia==null){
            StudyException.cast("待处理的视频信息不存在");
        }
        //处理视频的态,1:未处理，2：处理成功  3处理失败

        if("3".equals(status)){
            MediaProcess mediaProcess_u = new MediaProcess();
            mediaProcess_u.setStatus("3");
            mediaProcess_u.setErrorMsg(message);
            mediaProcess_u.setId(id);
            //更新失败的状态
            mediaFilesProcessMapper.updateById(mediaProcess_u);
            return;
        }
        //处理成功
        QueryWrapper<MediaProcess> wrapper=new QueryWrapper<>();
        wrapper.eq("file_id",fileId);
        MediaProcess mediaProcess = mediaFilesProcessMapper.selectById(wrapper);
        if("2".equals(status)){
            //处理成功之后，要将我们的待处理的信息进行删除.
            mediaFilesProcessMapper.deleteById(id);
        }
        //将我们的信息放入到历史表
        MediaProcessHistory mediaProcessHistory = new MediaProcessHistory();
        BeanUtils.copyProperties(mediaProcess,mediaProcessHistory);
        mediaProcessHistoryMapper.insert(mediaProcessHistory);//进行插入即可
    }
}
