package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.Base64;


@Controller
@RequestMapping("credentials")
public class CredentialController {

    private final UserService userService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public CredentialController(UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }


    @GetMapping
    public String getAllCredentials(Authentication authentication, Model model){
        Integer userId = getUserId(authentication);
        model.addAttribute("credentials", credentialService.getAllCredentials(userId));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    @PostMapping
    public String createCredential(Credential credential, Principal principal, Model model) {
        String username = principal.getName(); // Get the currently logged-in user's username

        if (credential.getUrl() == null || credential.getUsername() == null || credential.getPassword() == null) {
            model.addAttribute("result", "notSaved");
            return "result";
        } else {
            // Generate a random encryption key
            SecureRandom random = new SecureRandom();
            byte[] keyBytes = new byte[16];
            random.nextBytes(keyBytes);
            String encryptionKey = Base64.getEncoder().encodeToString(keyBytes);
            // Encrypt the password with the generated key
            String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encryptionKey);
            // Set the encrypted password and key back to the credential object
            credential.setPassword(encryptedPassword);
            credential.setKey(encryptionKey);


            if (credential.getCredentialId() != null) {
                credentialService.update(credential);
                model.addAttribute("result", "success");
            } else {
                credentialService.createCredentials(credential, username);
                model.addAttribute("result", "success");
            }

            model.addAttribute("encryptionService", encryptionService);
        }
        return "result";
    }

    @GetMapping(value = "/delete/{credentialId}")
    public String deleteNoteById(@PathVariable Integer credentialId, Model model) {
        credentialService.deleteCredentialById(credentialId);
        model.addAttribute("result", "success");
        return "result";
    }



    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        return user.getUserId();
    }
}
