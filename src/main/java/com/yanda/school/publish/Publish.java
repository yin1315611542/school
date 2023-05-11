package com.yanda.school.publish;

import com.yanda.school.moudel.ModuleType;
import com.yanda.school.moudel.ModuleTypeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "publish")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //标题
    private String title;
    //内容
    private String content;
    //单子类型（悬赏，跑腿等）
    @Convert(converter = ModuleTypeConverter.class)
    private ModuleType type;
    //建单时间
    private LocalDateTime startingTime;
    //截单时间
    private LocalDateTime endOfTime;
    //其实地点
    private String startingPlace;
    //目的地
    private String destination;
    //订单描述
    private String describes;
    //订单中的图片
    private String img;
    //订单中的图片  @Transient表示不在表中有对应字段
    @Transient
    private List<String> imgs;
    //电话号码
    private String mobilePhoneNo;
    //发布人id
    private Long publisher;
    //接收人id
    private Long receiver;
    //此订单是否已经被完成
    private Boolean mark;
    //价格
    private String price;

}
