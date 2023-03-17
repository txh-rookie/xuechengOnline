package com.kevintam.media;

import com.kevintam.media.Utils.GetDateTool;
import io.minio.*;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FilterInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/23
 */
@SpringBootTest
public class MinioTest {
    static MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://localhost:9000")
                    .credentials("admin", "kevintam")
                    .build();

    public static void main(String[] args) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException{
//   文件上传
//        //        minioClient.uploadObject(
//                UploadObjectArgs.builder()
//                        .bucket("testbucket")
//                        .object("kevintam.png")
//                        .filename("/Users/kevintam/Downloads/2022-06-18 17_31_23-kevintam.png")
//                        .build();
//        System.out.println("上传成功....");
//        删除文件
//        RemoveObjectArgs build = RemoveObjectArgs.builder().bucket("testbucket").object("kevintam.png").build();
//        minioClient.removeObject(build);
//        System.out.println("删除成功");
        //查询文件是否存在
//        GetObjectArgs build = GetObjectArgs.builder().bucket("testbucket").object("kevintam.png").build();
//        FilterInputStream fis = minioClient.getObject(build);
        String fileFolder = GetDateTool.getFileFolder(new Date(), true, true, true);
        System.out.println(fileFolder);
    }
}

