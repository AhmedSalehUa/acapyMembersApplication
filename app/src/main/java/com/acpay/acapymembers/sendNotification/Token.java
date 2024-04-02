package com.acpay.acapymembers.sendNotification;

public class Token {
    private String token;
    private String userID;
    private String username;

    public Token() {

    }

    public Token(String token, String userID, String username) {
        this.token = token;
        this.userID = userID;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
