package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private NoteService noteService;
    private CredentialService credencialService;
    private final UserService userService;
    private final EncryptionService encryptionService;
    private final FileService fileService;


    public HomeController(NoteService noteService, UserService userService, CredentialService credencialService, EncryptionService encryptionService, FileService fileService){
        this.userService = userService;
        this.noteService = noteService;
        this.credencialService = credencialService;
        this.encryptionService = encryptionService;
        this.fileService = fileService;

    }

    private List<File> fileList = new ArrayList<>();

    @ModelAttribute("fileDTO")
    public FileDTO getFileDTO() {
        return new FileDTO();
    }

    @GetMapping()
    public String getHomePage(Authentication authentication, Model model) {
        Integer userId = getUserId(authentication);
        model.addAttribute("note", new Note());
        model.addAttribute("credential", new Credential());

        List<Note> notes = noteService.getAllNotes(userId);
        List<Credential> credentials = credencialService.getAllCredentials(userId);
        fileList = this.fileService.getAllFiles(userId);
        model.addAttribute("credentials", credentials);
        model.addAttribute("notes", notes);
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("files", fileList);

        return "home";
    }

    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        return user.getUserId();
    }


}
