package com.acpay.acapymembers.Order;

import android.view.View;


public class Order {
    private String orderNum;
    private String Date;
    private String Time;
    private String Place;
    private String location;
    private String fixType;
    private String classMatter;
    private String DliverCost;
    private String notes;
    private String file;



    public Order(String orderNum, String Date, String Time, String Place, String location, String fixType, String classMatter, String DliverCost, String notes, String file) {
        this.orderNum = orderNum;
        this.Date = Date;
        this.Time = Time;
        this.Place = Place;
        this.location = location;
        this.fixType = fixType;
        this.classMatter = classMatter;
        this.DliverCost = DliverCost;
        this.notes = notes;
        this.file = file;

    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFixType() {
        return fixType;
    }

    public void setFixType(String fixType) {
        this.fixType = fixType;
    }

    public String getClassMatter() {
        return classMatter;
    }

    public void setClassMatter(String classMatter) {
        this.classMatter = classMatter;
    }

    public String getDliverCost() {
        return DliverCost;
    }

    public void setDliverCost(String dliverCost) {
        DliverCost = dliverCost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

}
