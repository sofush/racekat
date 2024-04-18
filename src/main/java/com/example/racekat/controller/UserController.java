package com.example.racekat.controller;

import com.example.racekat.entity.User;
import com.example.racekat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register/user")
    public String registerUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("repeat-password", "");
        return "register-user";
    }

    @PostMapping("/register/user")
    public String registerUserPost(@ModelAttribute("user") User user,
                                   @ModelAttribute("repeat-password") String repeat_password,
                                   Model model
    ) {
        if (!user.getPassword().contentEquals(repeat_password)) {
            model.addAttribute("message", "Password and repeat password do not match.");
            return "register-user-error";
        }

        try {
            this.userService.addUser(user);
        } catch (DataAccessException e) {
            model.addAttribute("message", "Username is taken.");
            return "register-user-error";
        }

        return "register-user-success";
    }
}
