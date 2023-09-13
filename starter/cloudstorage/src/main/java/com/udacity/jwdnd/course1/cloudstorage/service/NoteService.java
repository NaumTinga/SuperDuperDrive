package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private UserMapper userMapper;
    public NoteMapper noteMapper;

    public NoteService(UserMapper userMapper, NoteMapper noteMapper) {
        this.userMapper = userMapper;
        this.noteMapper = noteMapper;
    }


    public List<Note> getAllNotes(Integer userId){
        return (List<Note>) noteMapper.getAllNotes(userId);
    }


    public Note findByTitleAndDescription(Integer userId, String noteTitle, String noteDescription){
        return noteMapper.findByTitleAndDescription(userId, noteTitle, noteDescription);
    }


    public void createNote(Note note, String username) {
        Integer userId = userMapper.getUser(username).getUserId(); //get userId
        note.setUserId(userId);
        noteMapper.insert(note);
    }

    public void update(Note note){
        noteMapper.update(note);
    }

    public void deleteNoteById(Integer noteId) {
        noteMapper.deleteNoteById(noteId);
    }
}
