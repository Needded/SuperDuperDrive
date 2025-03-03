package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import com.udacity.jwdnd.course1.cloudstorage.Model.Note;
import com.udacity.jwdnd.course1.cloudstorage.Model.User;
import com.udacity.jwdnd.course1.cloudstorage.Services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.Services.NoteService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/notes")
    public String postNote(Authentication authentication, Model model, @ModelAttribute Note note) {
        User user = this.userService.getUser(authentication.getName());
        Integer userid = user.getUserId();
        note.setUserId(userid);

        try {
            if (note.getNoteId() == null) {
                noteService.createNote(note);
            } else {
                noteService.editNote(note);
            }
            model.addAttribute("success", true);
            model.addAttribute("message", "New note added!");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "System error!" + e.getMessage());
        }
        return "result";
    }

    @PostMapping("/notes/delete")
    public String deleteNote(Model model, @RequestParam ("NoteId") Integer note) {

        try {
            noteService.deleteNote(note);
            model.addAttribute("success", true);
            model.addAttribute("message", "Note deleted!");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "System error!" + e.getMessage());
        }
        return "result";
    }

}
