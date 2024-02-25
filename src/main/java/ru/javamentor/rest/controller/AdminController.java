package ru.javamentor.rest.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.javamentor.rest.model.User;
import ru.javamentor.rest.service.RoleService;
import ru.javamentor.rest.service.UserService;
import ru.javamentor.rest.util.UserValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final RoleService roleService;

    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping()
    public String getListUsers(ModelMap model) {
        model.addAttribute("user",userService.findByLogin(SecurityContextHolder.getContext()
                .getAuthentication().getName()));
        model.addAttribute("users",userService.getListUsers());
        model.addAttribute("rolesList",roleService.getAllRoles());
        return "/admin";
    }

    @ResponseBody
    @GetMapping("/users")
    public Map<String,Object> getListUsers() {
        Map<String,Object> pageAdmin = new HashMap<>();
        pageAdmin.put("user",userService.findByLogin(SecurityContextHolder.getContext()
                .getAuthentication().getName()));
        pageAdmin.put("users",userService.getListUsers());
        pageAdmin.put("rolesList",roleService.getAllRoles());
        return pageAdmin;
    }

    @PostMapping("/save")
    public ResponseEntity<Object> updateUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            userService.saveUser(user);
            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    @ResponseBody
    @GetMapping("/new")
    public ResponseEntity<User> createUser() {
        System.out.println("userService.getLastID()) = " + userService.getLastID());
        return ResponseEntity.ok(userService.getUser(userService.getLastID()));

    }

    @PostMapping("/delete")
    public ResponseEntity<HttpStatus> removeUser( @RequestParam(value = "id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
