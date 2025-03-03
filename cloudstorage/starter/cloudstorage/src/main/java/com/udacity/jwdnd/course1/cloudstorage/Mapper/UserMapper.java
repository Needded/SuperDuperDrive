package com.udacity.jwdnd.course1.cloudstorage.Mapper;

import com.udacity.jwdnd.course1.cloudstorage.Model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{userName}")
    User getUser(String userName);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{userName}, #{salt}, #{password}, #{firstName}, #{lastName})")
    int insert(String userName, String salt, String password, String firstName, String lastName);
}
