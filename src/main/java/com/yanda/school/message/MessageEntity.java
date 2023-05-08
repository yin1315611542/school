package com.yanda.school.message;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MessageEntity implements Serializable {

    private String _id;


    private String uuid;


    private Integer senderId;

	private String senderPhoto="https://static-1258386385.cos.ap-beijing.myqcloud.com/img/System.jpg";

	private String senderName;

    private Date sendTime;

    private String msg;
}
