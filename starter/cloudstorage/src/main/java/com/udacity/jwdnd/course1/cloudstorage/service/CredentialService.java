package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private final UserMapper userMapper;
    private final CredentialMapper credentialMapper;

    public CredentialService(UserMapper userMapper, CredentialMapper credentialMapper) {
        this.userMapper = userMapper;
        this.credentialMapper = credentialMapper;
    }

    public void getCredentialById(Integer credentialId){
        credentialMapper.getCredential(credentialId);
    }
    public List<Credential> getAllCredentials(Integer userId){
        return (List<Credential>) credentialMapper.getAllCredentials(userId);
    }

    public void createCredentials(Credential credential, String username){
        Integer userId = userMapper.getUser(username).getUserId();
        credential.setUserId(userId);
        credentialMapper.insert(credential);
    }
    
    public void update(Credential credential){
         credentialMapper.updateCredential(credential);
    }


    public void  deleteCredentialById(Integer credentialId){
        credentialMapper.deleteCredential(credentialId);
    }

}
