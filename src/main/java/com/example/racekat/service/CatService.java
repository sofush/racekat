package com.example.racekat.service;

import com.example.racekat.entity.Cat;
import com.example.racekat.repository.CatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class CatService {
    private final CatRepository repo;

    @Autowired
    public CatService(CatRepository repo) {
        this.repo = repo;
    }

    @PreAuthorize("#cat.owner == authentication.principal.username || hasRole('ADMIN')")
    public void addCat(Cat cat) throws DataAccessException {
        this.repo.addCat(cat);
    }
}
