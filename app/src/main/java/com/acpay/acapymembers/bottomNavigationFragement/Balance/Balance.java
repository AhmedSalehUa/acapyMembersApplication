package com.acpay.acapymembers.bottomNavigationFragement.Balance;

public class Balance {
    int id;
    String amount;
    String date;
    String statue;
    String details;

    public Balance( ) {
    }

    public Balance(int id,String amount, String date, String statue, String details) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.statue = statue;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
