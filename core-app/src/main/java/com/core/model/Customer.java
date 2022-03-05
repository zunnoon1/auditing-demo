package com.core.model;

public class Customer {
    private Long id;
    private String firstName;
    private String lastname;
    private String address;
    private String phoneNumber;

    public Customer(Long id, String firstName, String lastname, String address, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastname = lastname;
        this.address = address;
        this.phoneNumber = phoneNumber;

    }
    public Customer() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
