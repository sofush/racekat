package com.example.racekat.security;

import com.example.racekat.entity.User;
import com.example.racekat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DatabaseUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public DatabaseUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.findUserByUsername(username);

        if (user == null) throw new UsernameNotFoundException("User with the given username could not be found.");

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList()
        );
    }
}
