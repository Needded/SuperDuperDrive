package com.udacity.jwdnd.course1.cloudstorage.Mapper;

import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS")
    List<Credential> getAllCredentials();

    @Select("SELECT * FROM CREDENTIALS WHERE url = #{url} AND userid = #{userId}")
    Credential getCredential(String url, Integer userId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    Credential getCredentialId(Integer credentialId);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credential> getAllCredentialByUserId(Integer userId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    Credential retrieveKeyByCredentialId(Integer credentialId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    void insert(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, password = #{password} where credentialid = #{credentialId}")
    void update(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    void delete(Integer credentialId);
}
