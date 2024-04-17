package com.example.racekat.service;

import com.example.racekat.entity.User;
import com.example.racekat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;

    @Autowired
    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User findUserByUsername(String username) {
        return this.repo.findUserByUsername(username);
    }
}
