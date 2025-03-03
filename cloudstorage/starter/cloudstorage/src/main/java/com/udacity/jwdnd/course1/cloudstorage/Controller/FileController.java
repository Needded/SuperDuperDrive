package com.udacity.jwdnd.course1.cloudstorage.Controller;


import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import com.udacity.jwdnd.course1.cloudstorage.Model.User;
import com.udacity.jwdnd.course1.cloudstorage.Services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.Services.UserService;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/file/upload")
    public String uploadFile (Authentication authentication, Model model, @RequestParam("fileId") MultipartFile multipartFile){

        User user = this.userService.getUser(authentication.getName());
        Integer userId = user.getUserId();

        if (multipartFile.isEmpty()) {
            model.addAttribute("success", false);
            model.addAttribute("error", true);
            model.addAttribute("message", "File not selected to upload");
            return "result";
        }

        if (fileService.isFilenameAvailable(multipartFile.getOriginalFilename(), userId)) {

            model.addAttribute("success", false);
            model.addAttribute("error", true);
            model.addAttribute("message", "file name already exists");
            return "result";
        }
        try {
            fileService.createFile(multipartFile, userId);
            model.addAttribute("success", true);
            model.addAttribute("message", "New File added successfully");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "System error!" + e.getMessage());
        }
        return "result";

    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile (@PathVariable("fileId") Integer fileId){

        File file = fileService.getFileById(fileId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = " + file.getFileName());
        httpHeaders.add("Cache-control", "no-cache, no-store, must-revalidate");
        httpHeaders.add("Pragma", "no-cache");
        httpHeaders.add("Expires", "0");
        ByteArrayResource resource = new ByteArrayResource(file.getFileData());
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(resource);

    }

    @PostMapping("/files/delete")
    public String deleteFile (@RequestParam("fileId") Integer fileId, Model model){

        try {
            fileService.deleteFile(fileId);
            model.addAttribute("success", true);
            model.addAttribute("message", "file Deleted");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "System error!" + e.getMessage());
        }
        return "result";
    }

}
