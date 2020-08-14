package com.acpay.acapymembers;

public class User {
    public String username;
    public String userId;
    public String token;
    private String status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String userId, String token) {
        this.username = username;
        this.userId = userId;
        this.token = token;
    }

    public User(String username, String userId, String token, String status) {
        this.username = username;
        this.userId = userId;
        this.token = token;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
