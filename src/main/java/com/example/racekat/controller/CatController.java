package com.example.racekat.controller;

import com.example.racekat.entity.Cat;
import com.example.racekat.service.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class CatController {
    private final CatService catService;

    @Autowired
    public CatController(CatService catService) {
        this.catService = catService;
    }

    @GetMapping("/register/cat")
    public String registerCat(Model model) {
        model.addAttribute("cat", new Cat());
        return "register-cat";
    }

    @PostMapping("/register/cat")
    public String registerCatPost(@ModelAttribute("cat") Cat cat, Principal principal, Model model) {
        if (cat.getName().isEmpty()) {
            model.addAttribute("message", "Cat must be given a name.");
            return "register-cat-error";
        }

        cat.setOwner(principal.getName());
        this.catService.addCat(cat);
        return "register-cat-success";
    }
}
