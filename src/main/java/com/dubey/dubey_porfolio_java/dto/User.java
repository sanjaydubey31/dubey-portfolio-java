package com.dubey.dubey_porfolio_java.dto;

public class User {

    private String message;
    private String firstName;
    private String lastName;
    private String email;



    public User(String message, String firstName, String lastName, String email) {
        this.message = message;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Optional: Override toString() for logging purposes
    @Override
    public String toString() {
        return "User [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
    }
}
