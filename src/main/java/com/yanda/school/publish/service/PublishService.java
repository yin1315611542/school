package com.yanda.school.publish.service;

import com.yanda.school.moudel.ModuleType;
import com.yanda.school.publish.Publish;
import com.yanda.school.user.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PublishService {
    //存储发布内容
    public void createPublish(Publish publish);
    //修改发布内容
    public void deletePublish(Long id);
    //修改发布内容
    public void ModifyPublish(Publish publish);
    //查询发布内容
    public List<Publish> queryPublishAll();
    //按照类型查询发布内容
    public List<Publish> queryPublishByType(ModuleType moduleType);
    //按照id查询发布信息
    public Publish queryPublishById(Long id);
    //按照发布者查询发布信息
    public List<Publish> queryPublishByPublisher(User user);
    //按照接单者查询发布信息
    public List<Publish> queryPublishByReceiver(User user);
    //接单
    public Boolean acceptOrders();
    //取消接单
    public Boolean cancelOrder(Long orderId);
    //关键字查找
    public List<Publish> queryPublishByContent(String content);
    //保存图片
    public Boolean savePicture();

}
