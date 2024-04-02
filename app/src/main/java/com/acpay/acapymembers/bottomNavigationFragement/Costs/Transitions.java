package com.acpay.acapymembers.bottomNavigationFragement.Costs;

public class Transitions {

    private String amount;
    private String details;

    public Transitions(String amount, String details) {
        this.amount = amount;
        this.details = details;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
