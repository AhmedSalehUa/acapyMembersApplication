
package com.acpay.acapymembers.bottomNavigationFragement.messages;

public class Message {

    private String text;
    private String name;
    private String photoUrl;
    private String date;
    private String time;

    public Message() {
    }

    public Message(String text, String name, String photoUrl, String date, String time) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.date = date;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
