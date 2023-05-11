package com.yanda.school.message.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageRefEntity implements Serializable {

    private String _id;


    private String messageId;


    private Integer receiverId;


    private Boolean readFlag;


    private Boolean lastFlag;
}
