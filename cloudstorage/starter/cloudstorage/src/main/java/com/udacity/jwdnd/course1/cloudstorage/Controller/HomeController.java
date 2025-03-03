package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import com.udacity.jwdnd.course1.cloudstorage.Model.Note;
import com.udacity.jwdnd.course1.cloudstorage.Model.User;
import com.udacity.jwdnd.course1.cloudstorage.Services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    private final CredentialService credentialService;
    private final FileService fileService;
    private final NoteService noteService;
    private final EncryptionService encryptionService;

    public HomeController(UserService userService, CredentialService credentialService, FileService fileService, NoteService noteService, EncryptionService encryptionService) {

        this.userService = userService;
        this.credentialService = credentialService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String getHomePage(Authentication authentication,Model model) {

        User user = this.userService.getUser(authentication.getName());
        Integer userId = user.getUserId();
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("credentials", this.credentialService.getCredentialByUserId(userId));
        model.addAttribute("notes", this.noteService.getAllNotesByUserId(userId));
        model.addAttribute("files", this.fileService.getAllFilesByUserId(userId));
        model.addAttribute("noteForm", new Note());
        model.addAttribute("deleteNote", new Note());
        model.addAttribute("credentialForm", new Credential());
        model.addAttribute("deleteCredential", new Credential());
        model.addAttribute("deleteFile", new File());

        return "home";

    }
    @PostMapping("/logout")
    public String logout() {
        return "login?logout";
    }

    @GetMapping("/logout")
    public String logoutView() {
        return "redirect:/login?logout";
    }

}
