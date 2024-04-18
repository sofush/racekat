package com.example.racekat.controller;

import com.example.racekat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.LocalDate;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register/user")
    public String registerUser(Model model) {
        return "register-user";
    }

    @PostMapping("/register/user")
    public String registerUserPost(@ModelAttribute("username") String username,
                                   @ModelAttribute("password") String password,
                                   @ModelAttribute("repeat-password") String repeat_password,
                                   @ModelAttribute("name") String name,
                                   @ModelAttribute("about") String about,
                                   Model model
    ) {
        if (!password.contentEquals(repeat_password)) {
            model.addAttribute("message", "Password and repeat password do not match.");
            return "register-user-error";
        }

        try {
            this.userService.addUser(username, password, name, about);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "register-user-error";
        }

        return "register-user-success";
    }

    @GetMapping("/register/cat")
    public String registerCat(Model model, Principal principal) {
        model.addAttribute("owner", principal.getName());
        return "register-cat";
    }

    @PostMapping("/register/cat")
    public String registerCatPost(@ModelAttribute("owner") String owner,
                                  @ModelAttribute("name") String name,
                                  @ModelAttribute("breed") String breed,
                                  @ModelAttribute("dob") LocalDate dob,
                                  @ModelAttribute("male") String male,
                                  Model model
    ) {
        if (owner.isEmpty()) {
            model.addAttribute("message", "Cat must be given an owner.");
            return "register-cat-error";
        }

        if (name.isEmpty()) {
            model.addAttribute("message", "Cat must be given a name.");
            return "register-cat-error";
        }

        try {
            boolean isMale = male != null && male.contentEquals("on");
            this.userService.addCat(owner, name, breed, dob, isMale);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "register-cat-error";
        }

        return "register-cat-success";
    }
}
