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


    private Integer senderId;

	private String senderPhoto="https://static-1258386385.cos.ap-beijing.myqcloud.com/img/System.jpg";

	private String senderName;

    private Date sendTime;

    private String msg;

    private Integer read;
}
