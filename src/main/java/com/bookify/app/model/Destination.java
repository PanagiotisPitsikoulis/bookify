package com.bookify.app.model;

public class Destination {
    private int id;
    private String name;
    private String country;
    private String description;
    private double pricePerPerson;
    
    public Destination() {
    }
    
    public Destination(int id, String name, String country, String description, double pricePerPerson) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.description = description;
        this.pricePerPerson = pricePerPerson;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public double getPricePerPerson() {
        return pricePerPerson;
    }
    
    public void setPricePerPerson(double pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }
    
    @Override
    public String toString() {
        return name + ", " + country + " ($" + pricePerPerson + ")";
    }
} 