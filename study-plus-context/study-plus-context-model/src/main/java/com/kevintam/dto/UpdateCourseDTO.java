package com.kevintam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/20
 */
@Data
public class UpdateCourseDTO extends AddCourseDto{
    @NotNull(message = "更新时id不能为空")
    @ApiModelProperty(value = "课程名称", required = true)
    private Long id;
}
