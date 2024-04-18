package com.example.racekat.repository;

import com.example.racekat.entity.Cat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CatRepository {
    private final JdbcTemplate jdbc;

    @Autowired
    public CatRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;

        if (this.jdbc != null) {
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
