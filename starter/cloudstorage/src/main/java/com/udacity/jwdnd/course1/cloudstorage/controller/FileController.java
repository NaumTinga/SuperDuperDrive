package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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


    @PostMapping("/file/upload")
    public String uploadNewFile(Authentication authentication, Model model, @ModelAttribute("fileDTO") MultipartFile file) throws IOException {
        String errorMsg = null;
        Integer userId = getUserId(authentication);
        System.out.println("NAME: " + file.getName());
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
            model.addAttribute("result", "success");
        } else {
            model.addAttribute("result", "notSaved");
        }

        return "result";
    }

    @GetMapping(value = "/delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, Model model){
        fileService.deleteFile(fileId);
        model.addAttribute("result", "success");
        return "result";
    }


    //Source = https://www.baeldung.com/spring-controller-return-image-file
    @GetMapping(value = "/view/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    ResponseEntity<byte[]> viewData(@PathVariable Integer fileId){
        File file = fileService.getFileById(fileId);
        String fileName = file.getFileName();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+fileName+"\"")
                .body(file.getFileData());
    }

    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        return user.getUserId();
    }
}
