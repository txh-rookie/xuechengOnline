package com.kevintam.media.service;

import com.kevintam.media.model.po.MediaProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MediaProcessService {

    public List<MediaProcess> getMediaProcessList( int shardIndex,  int shardTotal, int count);

    /**
     * 跟新文件的状体啊
     * @param id
     * @param url
     * @param status
     * @param message
     */
    public void updateMediaProcessStatus(Long id,String url,String fileId,String status,String message);
}
