package com.example.racekat.entity;

import java.time.LocalDate;

public class Cat {
    private Integer id;
    private String owner, name, breed;
    private LocalDate dob;
    private Boolean male;

    public Cat(Integer id, String owner, String name, String breed, LocalDate dob, Boolean male) {
        this.setId(id);
        this.owner = owner;
        this.name = name;
        this.breed = breed;
        this.dob = dob;
        this.male = male;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Boolean getMale() {
        return male;
    }

    public void setMale(Boolean male) {
        this.male = male;
    }
}
