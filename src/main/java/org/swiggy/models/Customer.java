package org.swiggy.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * Represents a customer in the food delivery system.
 *
 * Customer is an Entity.
 * Identity is determined by customerId.
 *
 * Responsibilities:
 * - Manage profile information
 * - Manage delivery addresses
 */
public class Customer {
    private final int customerId;

    private String name;

    private String email;

    private String phoneNumber;

    private final List<Address> savedAddresses;

    private Address defaultAddress;
    private final Cart cart;

    public Customer(int customerId, String name, String phoneNumber, String email) {
        this.customerId = validateCustomerId(customerId);
        this.name = validateName(name);
        this.email = validateEmail(email);
        this.phoneNumber = validatePhoneNumber(phoneNumber);
        this.savedAddresses = new ArrayList<>();
        this.cart = new Cart();
    }

    public void removeAddress(Address address) {

        Objects.requireNonNull(
                address,
                "Address cannot be null."
        );

        if (!savedAddresses.contains(address)) {
            throw new IllegalArgumentException(
                    "Address does not exist."
            );
        }

        if (address.equals(defaultAddress)) {
            throw new IllegalStateException(
                    "Default address cannot be removed. Please choose another default address first."
            );
        }

        savedAddresses.remove(address);

        if (savedAddresses.isEmpty()) {
            defaultAddress = null;
        }
    }
    public void changeDefaultAddress(Address address) {

        Objects.requireNonNull(
                address,
                "Address cannot be null."
        );

        if (!savedAddresses.contains(address)) {
            throw new IllegalArgumentException(
                    "Address does not exist."
            );
        }

        defaultAddress = address;
    }

    public void updatePrimaryAddress(){}
    public List<Address> getAllAddress(){
        return Collections.unmodifiableList(savedAddresses);
    }
    public void changeEmail(String email) {
        this.email = email;
    }

    public void changePhone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    /**
     * Updates the customer's name.
     */
    public void updateName(String name) {

        this.name = validateName(name);
    }
    /**
     * Updates the customer's email address.
     */
    public void updateEmail(String email) {

        this.email = validateEmail(email);
    }
    /**
     * Updates the customer's phone number.
     */
    public void updatePhoneNumber(String phoneNumber) {

        this.phoneNumber = validatePhoneNumber(phoneNumber);
    }
    public Cart getCart(){
        return cart;
    }
    private int validateCustomerId(int customerId){
        if (customerId<=0){
            throw new IllegalArgumentException("Customer ID must be greater than zero");
        }
        return customerId;
    }
    private String validateRequiredText(String value,
                                        String fieldName){
        Objects.requireNonNull(value,fieldName + " cannot be null");
        value= value.trim();
        if (value.isBlank()){
            throw new IllegalArgumentException(
                    fieldName+" cannot be blank");
        }
        return value;
    }

    private String validateName(String name){
        name = validateRequiredText(name,"Name");
        if(name.length()<3){
            throw new IllegalArgumentException("Name must contain at least 3 characters.");
        }
        return name;
    }
    private String validateEmail(String email){
        email = validateRequiredText(email,"Email");
        if (!email.contains("@")||!email.contains(".")){
            throw new IllegalArgumentException("Invalid email address.");
        }
        return email;
    }

    private String validatePhoneNumber(String phoneNumber){
        phoneNumber = validateRequiredText(phoneNumber,"Phone number");

        if(!phoneNumber.matches("\\d{10}")){
            throw new IllegalArgumentException(
                    "Phone number must contain exactly 10 digits."
            );
        }
        return phoneNumber;
    }
    public void addAddress(Address address) {

        Objects.requireNonNull(
                address,
                "Address cannot be null."
        );

        if (savedAddresses.contains(address)) {
            throw new IllegalArgumentException(
                    "Address already exists."
            );
        }

        savedAddresses.add(address);

        if (defaultAddress == null) {
            defaultAddress = address;
        }
    }
    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Address getDefaultAddress() {
        return defaultAddress;
    }

    public List<Address> getAddresses() {
        return Collections.unmodifiableList(savedAddresses);
    }
    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (!(object instanceof Customer)) {
            return false;
        }

        Customer other = (Customer) object;

        return this.customerId == other.customerId;
    }
    @Override
    public int hashCode() {

        return Objects.hash(customerId);
    }
    @Override
    public String toString() {

        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", addresses=" + savedAddresses +
                ", defaultAddress=" + defaultAddress +
                '}';
    }
}
