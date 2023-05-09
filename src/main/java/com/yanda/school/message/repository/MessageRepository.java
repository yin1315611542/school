package com.yanda.school.message.repository;

import com.yanda.school.message.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @program: school
 * @description:
 * @Author: yinhd
 * @create: 2023-05-09 17:39
 **/
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {
}
