package com.yanda.school.publish;

import com.yanda.school.moudel.ModuleType;
import com.yanda.school.moudel.ModuleTypeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "publish")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @Convert(converter = ModuleTypeConverter.class)
    private ModuleType type;

    private LocalDateTime startingTime;

    private LocalDateTime endOfTime;

    private String startingPlace;

    private String destination;

    private String describes;

    private String img;

    private String mobilePhoneNo;

    private Long publisher;

    private Long receiver;

    private Boolean mark;

}
