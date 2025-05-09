package com.bookify.app.model;

public class Booking {
    private int id;
    private int customerId;
    private int destinationId;
    private String bookingDate;
    private String travelDate;
    private int numberOfPeople;
    private double pricePerPerson;
    private double totalPrice;
    private String status;
    private String notes;

    // Additional properties for UI display
    private String customerName;
    private String destinationName;

    public Booking() {
    }

    public Booking(int id, int customerId, int destinationId, String bookingDate,
            String travelDate, int numberOfPeople, double pricePerPerson, double totalPrice,
            String status, String notes) {
        this.id = id;
        this.customerId = customerId;
        this.destinationId = destinationId;
        this.bookingDate = bookingDate;
        this.travelDate = travelDate;
        this.numberOfPeople = numberOfPeople;
        this.pricePerPerson = pricePerPerson;
        this.totalPrice = totalPrice;
        this.status = status;
        this.notes = notes;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public double getPricePerPerson() {
        return pricePerPerson;
    }

    public void setPricePerPerson(double pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    @Override
    public String toString() {
        return "Booking #" + id + " - " + destinationName + " for " + customerName;
    }
}