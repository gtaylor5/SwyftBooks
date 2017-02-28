package com.swyftlabs.swyftbooks.Classes;

import com.swyftlabs.swyftbooks.Classes.Offer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Gerard on 2/19/2017.
 */

public class RetailerResultsItem {

    String retailer;
    ArrayList<Offer> offers = new ArrayList<>();
    ArrayList<Offer> buyOffers = new ArrayList<>();
    ArrayList<Offer> rentOffers = new ArrayList<>();
    ArrayList<Offer> sellOffers = new ArrayList<>();

    double cheapestPrice = Double.MAX_VALUE;

    public void setCheapestPrice(){
        sortOffers();
        if(buyOffers.size() > 0){
            if(cheapestPrice > Double.parseDouble(buyOffers.get(0).getPrice())){
                cheapestPrice = Double.parseDouble(buyOffers.get(0).getPrice());
            }
        }

        if(rentOffers.size() > 0){
            if(cheapestPrice > Double.parseDouble(rentOffers.get(0).getPrice())){
                cheapestPrice = Double.parseDouble(rentOffers.get(0).getPrice());
            }
        }

        if(offers.size() > 0){
            if(cheapestPrice > Double.parseDouble(offers.get(0).getPrice())){
                cheapestPrice = Double.parseDouble(rentOffers.get(0).getPrice());
            }
        }
    }

    public double getCheapestPrice() {
        if(cheapestPrice == Double.MAX_VALUE){
            setCheapestPrice();
        }
        return cheapestPrice;
    }

    public void sortOffers(){
        Collections.sort(buyOffers, new Comparator<Offer>() {
            @Override
            public int compare(Offer o1, Offer o2) {
                return (int)(o1.getPriceAsDouble()-o2.getPriceAsDouble());
            }
        });
        Collections.sort(rentOffers, new Comparator<Offer>() {
            @Override
            public int compare(Offer o1, Offer o2) {
                return (int)(o1.getPriceAsDouble()-o2.getPriceAsDouble());
            }
        });
        Collections.sort(sellOffers, new Comparator<Offer>() {
            @Override
            public int compare(Offer o1, Offer o2) {
                return (int)(o1.getPriceAsDouble()-o2.getPriceAsDouble());
            }
        });
        Collections.sort(offers, new Comparator<Offer>() {
            @Override
            public int compare(Offer o1, Offer o2) {
                return (int)(o1.getPriceAsDouble()-o2.getPriceAsDouble());
            }
        });
    }


    public RetailerResultsItem(ArrayList<Offer> offers){
        retailer = offers.get(0).getRetailer();
        for(Offer o : offers){
            switch(o.getType()){
                case RENT:
                    rentOffers.add(o);
                    break;
                case BUY:
                    buyOffers.add(o);
                    break;
                case SELL:
                    sellOffers.add(o);
                    break;
                case NONE:
                    this.offers.add(o);
            }
        }
        sortOffers();
    }

    public ArrayList<Offer> getBuyOffers() {
        return buyOffers;
    }

    public ArrayList<Offer> getRentOffers() {
        return rentOffers;
    }

    public ArrayList<Offer> getSellOffers() {
        return sellOffers;
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public String getRetailer() {
        return retailer;
    }
}
