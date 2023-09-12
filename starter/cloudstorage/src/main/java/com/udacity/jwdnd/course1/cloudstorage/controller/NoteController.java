package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/notes")
public class NoteController {


    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }



    @GetMapping
    public String getAllNotes(Authentication authentication, Model model) {
        Integer userId = getUserId(authentication);
        model.addAttribute("notes", this.noteService.getAllNotes(userId));
        return "home";
    }

    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        return user.getUserId();
    }

    @PostMapping
    public String createNote(Note note, Principal principal, Model model) {
        String username = principal.getName();

        if (note.getNoteTitle() == null || note.getNoteDescription() == null) {
            model.addAttribute("result", "notSaved");
            return "result";
        } else {
            if (note.getNoteId() != null) {
                // Note with noteId exists, update it
                noteService.update(note);
                model.addAttribute("result", "success");
            } else {
                // Note with noteId is null, create a new note
                noteService.createNote(note, username);
                model.addAttribute("result", "success");
            }
            return "result";
        }
    }

    @GetMapping(value = "/delete/{noteId}")
    public String deleteNoteById(@PathVariable Integer noteId, Model model) {
        noteService.deleteNoteById(noteId);
        model.addAttribute("result", "success");
        return "result";
    }

}
