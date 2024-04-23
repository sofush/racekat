package com.example.racekat.service;

import com.example.racekat.entity.Cat;
import com.example.racekat.entity.Role;
import com.example.racekat.entity.User;
import com.example.racekat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepo;

    @Autowired
    public UserService(UserRepository repo) {
        this.userRepo = repo;
    }

    public void addUser(String username, String password, String name, String about)
        throws DataAccessException, IllegalArgumentException
    {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username must be a non-empty string.");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password must a non-empty string.");
        }

        User user = new User(
            username,
            password,
            Role.USER,
            name,
            about,
            null
        );

        this.userRepo.addUser(user);
    }

    public User findUserByUsername(String username) throws DataAccessException {
        return this.userRepo.findUserByUsername(username);
    }

    public List<User> findAllUsers() throws DataAccessException {
        return this.userRepo.findAllUsers();
    }

    @PreAuthorize("#owner == authentication.principal.username || hasRole('ADMIN')")
    public void addCat(String owner, String name, String breed, LocalDate dob, boolean male)
        throws DataAccessException, IllegalArgumentException
    {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Cat name must be a non-empty string.");
        }

        Cat cat = new Cat(
            null,
            owner,
            name,
            breed,
            dob,
            male);

        this.userRepo.addCat(cat);
    }

    @PreAuthorize("#owner == authentication.principal.username || hasRole('ADMIN')")
    public void updateCat(Integer id, String owner, String name, String breed, LocalDate dob, Boolean male)
        throws DataAccessException
    {
        Cat cat = new Cat(
            id,
            owner,
            name,
            breed,
            dob,
            male);

        this.userRepo.updateCat(cat);
    }

    @PreAuthorize("#username == authentication.principal.username || hasRole('ADMIN')")
    public void updateUser(String username, String password, String name, String about)
        throws DataAccessException
    {
        User user = this.findUserByUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setAbout(about);

        this.userRepo.updateUser(user);
    }

    public Cat findCatById(int id) {
        return this.userRepo.findCatById(id);
    }

    public void deleteCatById(int id)
        throws DataAccessException, AccessDeniedException
    {
        Cat cat = this.findCatById(id);
        SecurityContext ctx = SecurityContextHolder.getContext();

        if (!cat.getOwner().equals(ctx.getAuthentication().getName())) {
            boolean isAdmin = ctx
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            if (!isAdmin)
                throw new AccessDeniedException("Cat is not owned by this user.");
        }

        this.userRepo.deleteCat(cat);
    }

    @PreAuthorize("#username == authentication.principal.username || hasRole('ADMIN')")
    public void deleteUser(String username)
        throws DataAccessException, AccessDeniedException
    {
        this.userRepo.deleteUser(username);
    }
}
