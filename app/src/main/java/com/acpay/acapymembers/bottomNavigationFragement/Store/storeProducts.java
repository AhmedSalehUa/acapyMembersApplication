package com.acpay.acapymembers.bottomNavigationFragement.Store;

public class storeProducts {
    int storeProductId;
    String name;
    String amount;
    int productId;

    public storeProducts() {
    }

    public storeProducts(int storeProductId, String name, String amount, int productId) {
        this.storeProductId = storeProductId;
        this.name = name;
        this.amount = amount;
        this.productId = productId;
    }

    public int getStoreProductId() {
        return storeProductId;
    }

    public void setStoreProductId(int storeProductId) {
        this.storeProductId = storeProductId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
