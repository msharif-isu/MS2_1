package com.example.harmonizefrontend;

/**
 * Class which holds data for any member who is a part of the application
 */
public class Member {

    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String bio;

    /**
     * Constructor for the member
     * @param id
     * @param firstName
     * @param lastName
     * @param username
     * @param bio
     */
    Member(int id, String firstName, String lastName, String username, String bio) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.bio = bio;
    }

    // Getters
    protected int getid() {
        return id;
    }

    protected String getFirstName() {
        return firstName;
    }

    protected String getLastName() {
        return lastName;
    }

    protected String getUsername() {
        return username;
    }

    protected String getBio() {
        return bio;
    }
}
