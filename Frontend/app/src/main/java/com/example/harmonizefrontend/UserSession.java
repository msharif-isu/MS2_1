package com.example.harmonizefrontend;

public class UserSession {
    private static UserSession instance;
    private Member currentUser;

    private String jwtToken;

    private UserSession() {

    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public Member getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Member currentUser) {
        this.currentUser = currentUser;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
