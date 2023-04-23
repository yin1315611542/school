package com.yanda.school.config;



import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.persistence.EntityManager;


/**
* 使用QueryDSL的功能时，会依赖使用到JPAQueryFactory，而JPAQueryFactory在这里依赖使用EntityManager，
* 所以在主类中做如下配置，使得Spring自动帮我们注入EntityManager与自动管理JPAQueryFactory
* @author NJ
* @create 2022/12/5 15:12
*/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public JPAQueryFactory jpaQuery(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
