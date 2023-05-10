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
        publish.setStartingTime(LocalDateTime.now());
        publish.setMark(false);
        publishRepository.save(publish);
    }

    @Override
    public void deletePublish(Long id) {
        publishRepository.deleteById(id);
    }

    @Override
    public void ModifyPublish(Publish publish) {
        publishRepository.save(publish);
    }

    @Override
    public List<Publish> queryPublishAll() {
        return publishRepository.findAll().stream().filter(e->e.getReceiver()==null).collect(Collectors.toList());
    }

    @Override
    public List<Publish> queryPublishByType(ModuleType type) {
        if (!ObjectUtils.isEmpty(type)){
            return jpaQueryFactory.selectFrom(QPublish.publish).where(QPublish.publish.type.eq(type).and(QPublish.publish.receiver.isNull())).fetch();
        }else {
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
    @Override
    public List<Publish> queryPublishByContent(String content){
        return jpaQueryFactory.selectFrom(QPublish.publish).where(QPublish.publish.content.like(content).or(QPublish.publish.describes.like(content).or(QPublish.publish.title.like(content)))).fetch();
    }

    @Override
    public Boolean acceptOrders() {
        return null;
    }

    @Override
    public Boolean cancelOrder(Long orderId) {
        Publish publish = publishRepository.getById(orderId);
        publish.setReceiver(0L);
        publish.setId(orderId);
        publishRepository.save(publish);
        return true;
    }

    @Override
    public Boolean savePicture() {
        return null;
    }
}
