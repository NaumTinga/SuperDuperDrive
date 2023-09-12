package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService){
        this.fileService = fileService;
        this.userService = userService;
    }

    @ModelAttribute("fileDTO")
    public FileDTO getFileDTO() {
        return new FileDTO();
    }

    // POST to upload new file:
    @PostMapping("/home/file/newFile")
    public String uploadNewFile(Authentication authentication, Model model, @ModelAttribute("fileDTO") MultipartFile file) throws IOException {

        String errorMsg = null;

        //int currentUserId = this.userService.getUserById(auth.getName());
        Integer userId = getUserId(authentication);

        System.out.println("NAME: " + file.getName());

        // handle edge cases:

        // when empty file is uploaded:
        if (file.isEmpty()) {
            errorMsg = "File should not be empty!";
        }

        if (errorMsg == null) {
            // upload file to Files db by fileId:
            // return current fileId if success:
            int currentFileId = this.fileService.uploadFile(file, userId);
            if (currentFileId < 0) {
                errorMsg = "There was error uploading this file!";
            }
        }

        // show result.html page with success/fail message:
        if (errorMsg == null) {
            model.addAttribute("updateSuccess", true);
        } else {
            model.addAttribute("updateFail", errorMsg);
        }

        return "home";
    }





    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        return user.getUserId();
    }
}
