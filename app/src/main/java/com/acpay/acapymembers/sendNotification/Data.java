package com.acpay.acapymembers.sendNotification;

public class Data {
    private String user;
    private String body;
    private String method;

    public Data(String user, String body,String method) {
        this.user = user;
        this.body = body;
        this.method = method;
    }

    public Data() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String methode) {
        this.method = methode;
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
