package com.acpay.acapymembers.bottomNavigationFragement.Store;

import com.acpay.acapymembers.bottomNavigationFragement.Costs.Transitions;

import java.util.List;

public class StoreReturnedOrders {
    int id;String date;String details;
    List<storeProducts> list;

    public StoreReturnedOrders() {
    }

    public StoreReturnedOrders(int id, String date, String details, List<storeProducts> list) {
        this.id = id;
        this.date = date;
        this.details = details;
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<storeProducts> getList() {
        return list;
    }

    public void setList(List<storeProducts> list) {
        this.list = list;
    }
}
