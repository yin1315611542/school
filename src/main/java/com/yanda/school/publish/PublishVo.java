package com.yanda.school.publish;

import com.yanda.school.moudel.ModuleType;
import com.yanda.school.moudel.ModuleTypeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
}
