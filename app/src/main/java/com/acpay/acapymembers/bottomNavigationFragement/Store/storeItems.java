package com.acpay.acapymembers.bottomNavigationFragement.Store;

import android.view.View;

import com.acpay.acapymembers.bottomNavigationFragement.Costs.Transitions;

import java.util.List;

public class storeItems {
    int id;String date;String details;
    private List<Transitions> list;

    private View.OnClickListener editeBtn;

    public storeItems() {
    }

    public storeItems(int id, String date, String details, List<Transitions> list) {
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

    public List<Transitions> getList() {
        return list;
    }

    public void setList(List<Transitions> list) {
        this.list = list;
    }

    public View.OnClickListener getEditeBtn() {
        return editeBtn;
    }

    public void setEditeBtn(View.OnClickListener editeBtn) {
        this.editeBtn = editeBtn;
    }
}
