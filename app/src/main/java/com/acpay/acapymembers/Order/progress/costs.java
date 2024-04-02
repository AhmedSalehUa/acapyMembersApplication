package com.acpay.acapymembers.Order.progress;

public class costs {
   private String amount;
    private String details;
    private String date;
    public costs(String amount, String details, String date) {
         this.details=details;
         this.amount=amount;
         this.date =date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
