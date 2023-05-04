package com.yanda.school.user.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class SearchUserInfoForm {
    @NotNull
    @Min(1)
    private Integer userId;
}
