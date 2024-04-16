package com.example.racekat.service;

import com.example.racekat.entity.Role;
import com.example.racekat.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private JdbcTemplate jdbc;

    @Autowired
    public UserService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public User getUserByUsername(String username) {
        return new User(username, "", Role.USER);
    }
}
