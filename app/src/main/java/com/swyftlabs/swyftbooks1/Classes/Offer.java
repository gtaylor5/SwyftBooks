package com.swyftlabs.swyftbooks1.Classes;

import android.util.Log;

/**
 * Created by Gerard on 2/19/2017.
 */

public class Offer {

    private TransactionType type;
    private String price;
    private String secondPrice;
    private String link;
    private String condition;
    private String quantity;
    private String retailer;



    public Offer () {

    }



    public void printOffer(){
        Log.i("AppInfo", type + " "+ ((price==null)?secondPrice:price) + " " + getRetailer());
    }

    public String getPrice(boolean f){
        return ((getPrice()==null)?getSecondPrice() : getPrice());
    }

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public String getSecondPrice() {
        return secondPrice;
    }

    public void setSecondPrice(String secondPrice) {
        this.secondPrice = secondPrice;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getPriceAsDouble(){
        return Double.parseDouble((getPrice() == null) ? getSecondPrice() : getPrice());
    }

}
