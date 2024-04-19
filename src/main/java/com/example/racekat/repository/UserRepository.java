package com.example.racekat.repository;

import com.example.racekat.entity.Cat;
import com.example.racekat.entity.Role;
import com.example.racekat.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

            this.jdbc.execute("""
                CREATE TABLE IF NOT EXISTS Cat(
                    id INTEGER AUTO_INCREMENT,
                    owner TEXT NOT NULL,
                    name TEXT NOT NULL,
                    breed TEXT,
                    dob DATE,
                    male BOOLEAN,
                    PRIMARY KEY (id)
                );
                """);
        }
    }

    public void addUser(User user) throws DataAccessException {
        String sql = """
            INSERT INTO User(username, password, role, name, about)
            VALUES (?, ?, ?, ?, ?);
            """;

        this.jdbc.update(
            sql,
            user.getUsername(),
            user.getPassword(),
            user.getRole().ordinal(),
            user.getName(),
            user.getAbout()
        );
    }

    public User findUserByUsername(String username) {
        String catsQuery = """
            SELECT id, owner, name, breed, dob, male
            FROM Cat
            WHERE owner = ?;
            """;

        List<Cat> cats = this.jdbc.query(
            catsQuery,
            (rs, rowNum) -> new Cat(
                rs.getInt("id"),
                rs.getString("owner"),
                rs.getString("name"),
                rs.getString("breed"),
                rs.getDate("dob").toLocalDate(),
                rs.getBoolean("male")
            ),
            username
        );

        String userQuery = """
            SELECT username, password, role, name, about
            FROM User
            WHERE username = ?;
            """;

        List<User> users = this.jdbc.query(
            userQuery,
            (rs, rowNum) -> new User(
                rs.getString("username"),
                rs.getString("password"),
                Role.values()[rs.getInt("role")],
                rs.getString("name"),
                rs.getString("about"),
                cats
            ),
            username
        );

        if (users.isEmpty()) return null;

        return users.getFirst();
    }

    public void addCat(Cat cat) throws DataAccessException {
        String sql = """
            INSERT INTO Cat(owner, name, breed, dob, male)
            VALUES (?, ?, ?, ?, ?);
            """;

        this.jdbc.update(
            sql,
            cat.getOwner(),
            cat.getName(),
            cat.getBreed(),
            cat.getDob(),
            cat.getMale()
        );
    }
}
