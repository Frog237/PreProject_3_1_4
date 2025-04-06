package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/user")
    public String user(Model model, Authentication authentication) {
        // Получаем текущего пользователя
        User currentUser = userService.findByEmail(authentication.getName());

        model.addAttribute("user", currentUser);
        model.addAttribute("userRoles", currentUser.getRoles());
        return "user";
    }
}
