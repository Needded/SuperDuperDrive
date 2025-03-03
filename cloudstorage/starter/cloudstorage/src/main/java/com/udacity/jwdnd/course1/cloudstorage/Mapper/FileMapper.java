package com.udacity.jwdnd.course1.cloudstorage.Mapper;

import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata)" +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    void insert(File file);

    @Select("SELECT * FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
    File getFile(String fileName, Integer userId);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    File getFileById(Integer fileId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getAllFilesByUserId(Integer userId);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    void delete(Integer fileId);

}
