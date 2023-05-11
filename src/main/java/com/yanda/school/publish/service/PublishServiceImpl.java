package com.yanda.school.publish.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yanda.school.moudel.ModuleType;
import com.yanda.school.publish.Publish;
import com.yanda.school.publish.QPublish;
import com.yanda.school.publish.repository.PublishRepository;
import com.yanda.school.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublishServiceImpl implements PublishService {
    @Autowired
    PublishRepository publishRepository;
    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Override
    public void createPublish(Publish publish) {
        //设置发单时间
        publish.setStartingTime(LocalDateTime.now());
        //设置单子完成状态 0 -未完成 1-已完成
        publish.setMark(false);
        //jpa连接数据库 进行单子的创建
        publishRepository.save(publish);
    }

    //删除订单
    @Override
    public void deletePublish(Long id) {
        publishRepository.deleteById(id);
    }

    //修改订单
    @Override
    public void ModifyPublish(Publish publish) {
        publishRepository.save(publish);
    }

    //查询所有为未接单的订单
    @Override
    public List<Publish> queryPublishAll() {
        return publishRepository.findAll().stream().filter(e -> e.getReceiver() == null).collect(Collectors.toList());
    }

    //按类型订单
    @Override
    public List<Publish> queryPublishByType(ModuleType type) {
        if (!ObjectUtils.isEmpty(type)) {
            return jpaQueryFactory.selectFrom(QPublish.publish)
                    .where(QPublish.publish.type.eq(type).and(QPublish.publish.receiver.isNull()))
                    .fetch();
        } else {
            return jpaQueryFactory.selectFrom(QPublish.publish).where(QPublish.publish.receiver.isNull()).fetch();
        }
    }

    @Override
    public Publish queryPublishById(Long id) {
        return publishRepository.getById(id);
    }

    @Override
    public List<Publish> queryPublishByPublisher(User user) {
        return jpaQueryFactory.selectFrom(QPublish.publish).where(QPublish.publish.publisher.eq(user.getId())).fetch();
    }

    @Override
    public List<Publish> queryPublishByReceiver(User user) {
        return jpaQueryFactory.selectFrom(QPublish.publish).where(QPublish.publish.receiver.eq(user.getId())).fetch();
    }
    //通过关键字查询订单，使用like语句 like 标题 描述 内容三项
    @Override
    public List<Publish> queryPublishByContent(String content) {
        return jpaQueryFactory.selectFrom(QPublish.publish)
                .where(QPublish.publish.content.like(content)
                        .or(QPublish.publish.describes.like(content).or(QPublish.publish.title.like(content))))
                .fetch();
    }
   //完成订单
    @Override
    public Boolean completeOrders(Long orderId) {
        Publish publish = publishRepository.getById(orderId);
        //将此单设位已完成
        publish.setMark(true);
        publishRepository.save(publish);
        return true;
    }
   //退单
    @Override
    public Boolean cancelOrder(Long orderId) {
        Publish publish = publishRepository.getById(orderId);
        //将接受者设为null 表此单无人接收，即完成退单操作
        publish.setReceiver(null);
        publish.setId(orderId);
        publishRepository.save(publish);
        return true;
    }

    @Override
    public Boolean savePicture() {
        return null;
    }
}
