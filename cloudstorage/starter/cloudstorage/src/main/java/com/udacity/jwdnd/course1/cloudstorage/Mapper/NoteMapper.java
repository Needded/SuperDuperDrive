package com.udacity.jwdnd.course1.cloudstorage.Mapper;

import com.udacity.jwdnd.course1.cloudstorage.Model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES")
    List<Note> getAllNotes();

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    List<Note> getNoteById(Integer noteId);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getAllNoteByUserId(Integer userId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    void insert (Note note);

    @Update("UPDATE NOTES set notetitle= #{noteTitle}, notedescription = #{noteDescription}, userid = #{userId} WHERE noteId = #{noteId}")
    void update(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    void delete(Integer id);

}
