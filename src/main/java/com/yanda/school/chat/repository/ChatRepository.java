package com.yanda.school.chat.repository;

import com.yanda.school.chat.Chat;
import com.yanda.school.publish.Publish;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mapper
public interface ChatRepository extends JpaRepository<Chat, Integer> {

}
