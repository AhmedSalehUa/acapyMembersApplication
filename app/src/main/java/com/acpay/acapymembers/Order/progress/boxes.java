package com.acpay.acapymembers.Order.progress;

import android.widget.CheckBox;

public class boxes {
    private String name;
    private String value;
    private String notes;

    private CheckBox.OnCheckedChangeListener checkListener;
    public boxes(String name, String value, String notes) {
        this.value = value;
        this.name = name;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public CheckBox.OnCheckedChangeListener getCheckListener() {
        return checkListener;
    }

    public void setCheckListener(CheckBox.OnCheckedChangeListener checkListener) {
        this.checkListener = checkListener;
    }
}
