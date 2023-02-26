package com.kevintam.dto;

import com.kevintam.entity.Teachplan;
import com.kevintam.entity.TeachplanMedia;
import lombok.Data;

import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/20
 */
@Data
public class TeachplanDTO extends Teachplan {
    private TeachplanMedia teachplanMedia;
    private List<TeachplanDTO> teachPlanTreeNodes;
}
