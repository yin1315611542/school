package com.yanda.school.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "message")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MessageEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String uuid;

    //发送者id
    private Integer senderId;
    //发送者头像
	private String senderPhoto="https://static-1258386385.cos.ap-beijing.myqcloud.com/img/System.jpg";
    //发送者名字
	private String senderName = "校园小助手";
    //发送时间
    private Date sendTime = new Date();
    //发送消息
    private String msg;
    //是否已读
    private Integer readMark = 0;
}
