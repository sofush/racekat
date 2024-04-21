package com.example.racekat.controller;

import com.example.racekat.entity.User;
import com.example.racekat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{username}")
    public String displayUser(@PathVariable String username, Model model) {
        // TODO: fjern afhængighed til User
        User user = this.userService.findUserByUsername(username);

        if (user == null) {
            model.addAttribute("message", "Could not find user with the provided username.");
            return "display-user-error";
        }

        model.addAttribute("name", user.getName());
        model.addAttribute("about", user.getAbout());
        model.addAttribute("role", user.getRole());
        model.addAttribute("cats", user.getCats());
        model.addAttribute("username", username);
        return "display-user";
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

    @GetMapping("/delete/cat/{id}")
    public String deleteCat(
        @PathVariable("id") Integer id,
        @RequestParam("username") String redirect_username,
        Model model
    ) {
        try {
            this.userService.deleteCatById(id);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "delete-cat-error";
        }

        return "redirect:/user/" + redirect_username;
    }

    @GetMapping("/delete/user/{username}")
    public String deleteUser(
        @PathVariable("username") String username,
        Model model
    ) {
        try {
            this.userService.deleteUser(username);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "delete-user-error";
        }

        SecurityContext ctx = SecurityContextHolder.getContext();

        if (ctx.getAuthentication().getName().contentEquals(username)) {
            // Log brugeren ud af systemet hvis de er i gang med at slette sin egen konto.
            ctx.setAuthentication(null);
        }

        return "delete-user-success";
    }
}
