package com.example.carculator.commons;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VueController implements ErrorController {
    private final String ERROR_PATH = "/error";
    @RequestMapping(value = ERROR_PATH)
    public String redirect() {
        System.out.println("redirect");
        return "forward:/";
    }
}