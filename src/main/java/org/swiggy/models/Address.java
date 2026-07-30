package org.swiggy.models;

public class Address {
    private final String street;
    private final String city;
    private final String state;
    private final String zipCode;
    private double latitude;
    private double longitude;
    private final String houseNumber;

    // Parameterized Constructor
    public Address(String houseNumber,String street, String city, String state, String zipCode, double latitude, double longitude) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }
    public String getFullAddress() {
        return houseNumber + ", " +
                street + ", " +
                city + ", " +
                state + " - " +
                zipCode;
    }
}
