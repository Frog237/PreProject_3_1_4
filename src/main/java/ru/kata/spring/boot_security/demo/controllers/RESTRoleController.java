package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;


@RestController
@RequestMapping("/api/roles")
public class RESTRoleController {

    private final UserService userService;

    @Autowired
    public RESTRoleController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<Role> getAllRoles() {
        return userService.getAllRoles();
    }
}
