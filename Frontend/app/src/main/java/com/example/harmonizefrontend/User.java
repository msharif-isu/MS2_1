package com.example.harmonizefrontend;

public class User {
    private String username;
    String profileURL;

    User(String username, String profileURL) {
        this.username = username;
        this.profileURL = profileURL;
    }

    protected String getUsername() {
        return username;
    }

    protected String getPfp() {
        return profileURL;
    }
}
