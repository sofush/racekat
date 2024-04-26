package com.example.racekat.controller;

import com.example.racekat.entity.Cat;
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
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/members")
    public String displayMembers(Model model) {
        List<User> users;
        try {
            users = this.userService.findAllUsers();
        } catch (Exception e) {
            return "display-members-error";
        }

        model.addAttribute("users", users);
        return "display-members";
    }

    @GetMapping("/user/{username}")
    public String displayUser(@PathVariable String username, Model model) {
        User user;
        try {
            user = this.userService.findUserByUsername(username);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "display-user-error";
        }

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

        model.addAttribute("owner", owner);
        return "register-cat-success";
    }

    @GetMapping("/update/cat/{id}")
    public String updateCat(
        @PathVariable("id") Integer id,
        Principal principal,
        Model model
    ) {
        Cat cat = this.userService.findCatById(id);

        SecurityContext ctx = SecurityContextHolder.getContext();
        boolean isAdmin = ctx
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        if (!cat.getOwner().contentEquals(principal.getName()) && !isAdmin) {
            model.addAttribute("message", "Cat is not owned by this account.");
            return "update-cat-error";
        }

        model.addAttribute("id", id);
        model.addAttribute("owner", cat.getOwner());
        model.addAttribute("breed", cat.getBreed());
        model.addAttribute("dob", cat.getDob());
        model.addAttribute("name", cat.getName());
        model.addAttribute("male", cat.getMale());
        return "update-cat";
    }

    @PostMapping("/update/cat")
    public String updateCatPost(@ModelAttribute("id") Integer id,
                                @ModelAttribute("owner") String owner,
                                @ModelAttribute("name") String name,
                                @ModelAttribute("breed") String breed,
                                @ModelAttribute("dob") LocalDate dob,
                                @ModelAttribute("male") String male,
                                Model model
    ) {
        try {
            boolean isMale = male != null && male.contentEquals("on");
            this.userService.updateCat(id, owner, name, breed, dob, isMale);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "update-cat-error";
        }

        model.addAttribute("owner", owner);
        return "update-cat-success";
    }

    @GetMapping("/update/user/{username}")
    public String updateUser(@PathVariable("username") String username,
                             Model model
    ) {
        User user = this.userService.findUserByUsername(username);
        SecurityContext ctx = SecurityContextHolder.getContext();
        boolean isAdmin = ctx
            .getAuthentication()
            .getAuthorities()
            .stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!ctx.getAuthentication().getName().equals(username) && !isAdmin) {
            model.addAttribute(
                "message",
                "You do not have permission to edit this user's profile."
            );
            return "update-user-error";
        }

        model.addAttribute("username", username);
        model.addAttribute("password", user.getPassword());
        model.addAttribute("name", user.getName());
        model.addAttribute("about", user.getAbout());
        return "update-user";
    }

    @PostMapping("/update/user")
    public String updateUser(@ModelAttribute("username") String username,
                             @ModelAttribute("password") String password,
                             @ModelAttribute("repeat-password") String repeat_password,
                             @ModelAttribute("name") String name,
                             @ModelAttribute("about") String about,
                             Model model
    ) {
        if (!password.contentEquals(repeat_password)) {
            model.addAttribute("message", "Password and repeat password do not match.");
            return "update-user-error";
        }

        try {
            this.userService.updateUser(username, password, name, about);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "update-user-error";
        }

        return "redirect:/user/" + username;
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
