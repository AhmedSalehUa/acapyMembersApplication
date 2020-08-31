package com.acpay.acapymembers.bottomNavigationFragement.Costs;

import android.view.View;

import java.util.List;


public class TransitionsDetails {
    private String orderNum;
    private String Date;
    private String Time;
    private String Place;
    private String location;
    private List<Transitions> list;


    public TransitionsDetails(String orderNum, String date, String time, String place, String location, List<Transitions> list) {
        this.orderNum = orderNum;
        Date = date;
        Time = time;
        Place = place;
        this.location = location;
        this.list = list;
    }

    public List<Transitions> getList() {
        return list;
    }

    public void setList(List<Transitions> list) {
        this.list = list;
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

}
