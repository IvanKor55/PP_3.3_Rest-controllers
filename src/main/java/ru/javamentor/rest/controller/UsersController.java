package ru.javamentor.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javamentor.rest.model.User;
import ru.javamentor.rest.service.UserService;

@RestController
@RequestMapping("/user")
public class UsersController {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserRest(@RequestParam(value = "id", required = false) Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping
    public String getUser(Model model, @RequestParam(value = "id", required = false) Long id) {
        model.addAttribute("user",userService.getUser(id));
        return "/user";
    }
}
