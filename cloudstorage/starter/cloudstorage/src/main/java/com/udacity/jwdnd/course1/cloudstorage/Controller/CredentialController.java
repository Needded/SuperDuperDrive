package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.Model.User;
import com.udacity.jwdnd.course1.cloudstorage.Services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.Services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CredentialController {

    private final UserService userService;
    private final CredentialService credentialService;

    public CredentialController(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @PostMapping("/credentials")
    public String postCredential(Authentication authentication, Model model, @ModelAttribute Credential credential) {
        User validUser = userService.getUser(authentication.getName());
        Integer userId = validUser.getUserId();
        credential.setUserId(userId);

        try {
            if (credential.getCredentialId() == null) {
                //Create
                credentialService.createCredential(credential);
            } else {
                //Update
                credentialService.editCredential(credential);
            }
            model.addAttribute("success", true);
            model.addAttribute("message", "New credential added!");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "System error!" + e.getMessage());
        }
        return "result";
    }

    @PostMapping("/delete/credential")
    public String deleteCredential(Model model, @RequestParam("credentialId") Integer credentialId) {

        try {
            credentialService.deleteCredential(credentialId);
            model.addAttribute("success", true);
            model.addAttribute("message", "Credential deleted!");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Error: " + e.getMessage());
        }
        return "result";
    }
}
