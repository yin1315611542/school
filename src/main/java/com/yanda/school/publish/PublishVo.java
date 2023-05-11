package com.yanda.school.publish;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishVo {
    private Long id;

    private String title;

    private String content;

    private Integer type;

    private LocalDateTime startingTime;

    private LocalDateTime endOfTime;

    private String startingPlace;

    private String destination;

    private String img;

    private String mobilePhoneNo;

    private Long publisher;

    private Long receiver;

    private Boolean mark;

    private String photo;

    private String price;
}
