package org.swiggy.models;

import org.swiggy.enums.AddressType;

import java.util.Objects;
/**
 * Represents an immutable delivery address.
 *
 * Address is a Value Object.
 * Equality is determined by all of its values.
 */

public final class Address {
    private final String houseNumber;
    private final String street;
    private final String area;
    private final String city;
    private final String state;
    private final String postalCode;
    //private final AddressType addressType;


    // Parameterized Constructor
    public Address(String houseNumber,String street,String area, String city, String state, String postalCode) {
        this.houseNumber = normalizeOptionalField(houseNumber);
        this.street = normalizeOptionalField(street);

        this.area = validateRequiredField(area, "Area");
        this.city = validateRequiredField(city, "City");
        this.state = validateRequiredField(state, "State");
        this.postalCode = validateRequiredField(postalCode, "Postal Code");
    }
    public String getFullAddress() {
        return houseNumber + ", " +
                street + ", " +
                city + ", " +
                state + " - " +
                postalCode;
    }

    private String validateRequiredField(String value, String fieldName){
        Objects.requireNonNull(value,fieldName + "cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be blank."
            );
        }

        return value;
    }
    private String normalizeOptionalField(String value) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }
    public String getHouseNumber() {
        return houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public String getArea() {
        return area;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }


    @Override
    public String toString(){
        return getFullAddress();
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (!(object instanceof Address)) {
            return false;
        }

        Address address = (Address) object;

        return houseNumber.equals(address.houseNumber)
                && street.equals(address.street)
                && area.equals(address.area)
                && city.equals(address.city)
                && state.equals(address.state)
                && postalCode.equals(address.postalCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                houseNumber,
                street,
                area,
                city,
                state,
                postalCode
        );
    }

}
