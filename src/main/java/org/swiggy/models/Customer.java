package org.swiggy.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer {
    private final String customerId;
    private final String name;
    private String phoneNumber;
    private String email;
    private final List<Address> savedAddresses; // 1 Customer -> Many Addresses
    private List<Order> orderHistory;     // 1 Customer -> Many Orders

    public Customer(String customerId, String name, String phoneNumber) {
        this.customerId = customerId;
        this.name = name;
        this.phoneNumber = phoneNumber;

        // CRITICAL: Initialize lists to empty ArrayLists to prevent NullPointerException!
        this.savedAddresses = new ArrayList<>();
        this.orderHistory = new ArrayList<>();
    }

    public void addAddress(Address address){
        savedAddresses.add(address);
    }
    public void removeAddress(Address address){
        savedAddresses.remove(address);
    }
    public List<Address> getAllAddress(){
        return Collections.unmodifiableList(savedAddresses);
    }
    public void changeEmail(String email) {
        this.email = email;
    }

    public void changePhone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
