package com.udacity.jwdnd.course1.cloudstorage.Services;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Note;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public void createNote(Note note) {
        noteMapper.insert(note);
    }

    public void editNote(Note note) {
        noteMapper.update(note);
    }

    public void deleteNote(Integer noteId) {
        noteMapper.delete(noteId);
    }

    public Object getAllNotesByUserId(Integer userId) {
        return noteMapper.getAllNoteByUserId(userId);
    }
}
