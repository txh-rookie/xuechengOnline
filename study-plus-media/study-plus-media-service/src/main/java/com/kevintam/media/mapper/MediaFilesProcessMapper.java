package com.kevintam.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kevintam.media.model.po.MediaProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MediaFilesProcessMapper extends BaseMapper<MediaProcess> {
    /**
     * @param shardIndex 分片序号
     * @param shardTotal 分片的总数
     * @param count 我们一次要去多少个任务
     * @return
     */

    public List<MediaProcess> getMediaProcessList(@Param("shardIndex") int shardIndex,@Param("shardTotal") int shardTotal,@Param("count") int count);
}
