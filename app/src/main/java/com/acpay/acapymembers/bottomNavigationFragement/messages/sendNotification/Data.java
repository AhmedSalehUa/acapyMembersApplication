package com.acpay.acapymembers.bottomNavigationFragement.messages.sendNotification;

public class Data {
    private String user;

    private String body;

    public Data(String user, String body) {
        this.user = user;
        this.body = body;
    }

    public Data() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
