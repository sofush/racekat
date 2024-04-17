package com.example.racekat.repository;

import com.example.racekat.entity.Role;
import com.example.racekat.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbc;

    @Autowired
    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;

        if (this.jdbc != null) {
            this.jdbc.execute("""
                CREATE TABLE IF NOT EXISTS User(
                    username VARCHAR(20),
                    password TEXT NOT NULL,
                    role INTEGER NOT NULL,
                    name TEXT,
                    about TEXT,
                    PRIMARY KEY (username)
                );
                """);
        }
    }

    public User findUserByUsername(String username) {
        String sql = """
            SELECT username, password, role, name, about
            FROM User
            WHERE username = ?;
            """;

        List<User> users = this.jdbc.query(
            sql,
            (rs, rowNum) -> new User(
                rs.getString("username"),
                rs.getString("password"),
                Role.values()[rs.getInt("role")],
                rs.getString("name"),
                rs.getString("about")
            ),
            username
        );

        if (users.isEmpty()) return null;

        return users.getFirst();
    }
}
