package ru.javamentor.rest.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.javamentor.rest.model.User;
import ru.javamentor.rest.service.RoleService;
import ru.javamentor.rest.service.UserService;
import ru.javamentor.rest.util.UserValidator;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final RoleService roleService;

    private UserValidator userValidator;

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

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("user") User user, HttpServletRequest request) {
        userService.editUser(user, request.getParameterValues("rolesSelectedList"));
        return "redirect:/admin";
    }

//    @PostMapping("/edit")
//    public ResponseEntity<HttpStatus> updateUser(@RequestBody User user, HttpServletRequest request) {
//        userService.editUser(user, request.getParameterValues("rolesSelectedList"));
//        return ResponseEntity.ok(HttpStatus.OK);
//
//    }

    @GetMapping("/new")
    public String createUser(Model model) {
        model.addAttribute("user",new User());
//        model.addAttribute("rolesList",roleService.getAllRoles());
        return "new";
    }

    @PostMapping("/new")
    public String addUser(@ModelAttribute("user") @Valid User user, HttpServletRequest request,
    BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "redirect:/admin/new";}
        else {
            userService.addUser(user, request.getParameterValues("rolesSelectedList"));
            return "redirect:/admin";
        }
    }

    @PostMapping("/delete")
    public String removeUser(Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
