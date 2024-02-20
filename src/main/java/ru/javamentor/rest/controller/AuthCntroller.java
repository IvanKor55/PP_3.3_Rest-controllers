package ru.javamentor.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthCntroller {

    @GetMapping ("/login")
    public String loginPage() {
        return "/auth/login";
    }
}

