package com.yanda.school.user.repository;


import com.yanda.school.publish.Publish;
import com.yanda.school.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select if(COUNT(*),true,false) from user where root = 1",nativeQuery = true)
    boolean haveRootUser();

    @Query(value = "select id from user where openid = ?1",nativeQuery = true)
    Long searchByOpenId(String openId);
}
