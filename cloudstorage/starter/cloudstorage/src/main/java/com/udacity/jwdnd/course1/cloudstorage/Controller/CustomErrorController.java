package com.udacity.jwdnd.course1.cloudstorage.Controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError() {
        return "error"; // This will return error.html from the templates folder
    }
}

