package com.example.harmonizefrontend;

public class Member {

    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String bio;

    Member(int id, String firstName, String lastName, String username, String bio) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.bio = bio;
    }
}
