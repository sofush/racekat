package com.example.racekat.entity;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String username, password, name, about;
    private Role role;
    private List<Cat> cats;

    public User() {}

    public User(String username, String password, Role role, String name, String about, List<Cat> cats) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.about = about;
        this.cats = cats;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.length() < 5 || username.length() > 20) {
            throw new IllegalArgumentException("Username must be 5-20 characters long.");
        }

        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 3) {
            throw new IllegalArgumentException("Password must be at least 3 characters long.");
        }

        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<Cat> getCats() {
        return cats;
    }

    public void setCats(List<Cat> cats) {
        this.cats = cats;
    }

    public List<String> getAuthorities() {
        // En Spring Security "autoritet" bestemmer hvad en besøgende må og ikke må i systemet (herunder hvilke
        // ruter de må tilgå). Spring Security roller er bare autoriteter der starter med et "ROLE_" præfiks så
        // vidt jeg forstår.
        //
        // Her giver vi alle brugerne en rolle som bruger (administratorer får også rollen). Administratorer får
        // derudover også rollen som administrator.
        List<String> authorities = new ArrayList<>(List.of("ROLE_USER"));

        if (this.getRole() == Role.ADMIN) {
            authorities.add("ROLE_ADMIN");
        }

        return authorities;
    }
}
