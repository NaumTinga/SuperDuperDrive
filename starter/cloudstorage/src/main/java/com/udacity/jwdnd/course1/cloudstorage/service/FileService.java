package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService  {

    private UserMapper userMapper;
    private final FileMapper fileMapper;

    public FileService(UserMapper userMapper, FileMapper fileMapper){
        this.userMapper = userMapper;
        this.fileMapper = fileMapper;
    }

    public List<File> getAllFiles(Integer userId){
        return (List<File>) fileMapper.getAllFiles(userId);
    }

    public File getFileById(Integer fileId){
        return  fileMapper.getFileById(fileId);
    }


    // upload new file to use in FilesController:
    public int uploadFile(MultipartFile mFile, int userId) throws IOException {

        // Convert the uploaded file under MultipartFile into FileForm
        // before storing to db:
        // filename, contenttype, filesize, userid, filedata
        File file = new File(null, mFile.getOriginalFilename(),
                mFile.getContentType(), mFile.getSize(), userId, mFile.getBytes());

        return this.fileMapper.insert(file);
    }

    public int deleteFile(Integer fileId) {
        return fileMapper.deleteFile(fileId);
    }




}
