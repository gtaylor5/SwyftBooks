package com.swyftlabs.swyftbooks1.Requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.swyftlabs.swyftbooks1.Classes.Offer;
import com.swyftlabs.swyftbooks1.Classes.TransactionType;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gerard on 2/28/2017.
 */

public class AmazonPriceRequest {

    private String xmlFile;
    private String signedRequestURL = "";
    private final String ENDPOINT = "ecs.amazonaws.com";
    private SignedRequestsHelper helper;

    private ArrayList<Offer> items = new ArrayList<>();

    private VolleyRequestSingleton requestSingleton;

    public AmazonPriceRequest(String keyID, String secretKey) {
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, keyID, secretKey);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Offer> getItems() {
        ArrayList<Offer> temp = new ArrayList<>();
        temp.addAll(items);
        items.removeAll(items);
        return temp;
    }


    public ArrayList<Offer> parseFeed() throws Exception {
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = xmlPullParserFactory.newPullParser();
        parser.setInput(new StringReader(xmlFile));
        while(parser.next() != parser.END_DOCUMENT){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String tagName = parser.getName();
            if(tagName.equals("Item")) {
                items.addAll(readChild(parser));
            }
        }
        return items;
    }

    public ArrayList<Offer> readChild(XmlPullParser parser) throws Exception{
        String currentTagName = parser.getName();
        ArrayList<Offer> offers = new ArrayList<>();
        String offerLink = "";
        String sellLink = "";
        parser.next();
        while(!currentTagName.equals(parser.getName())){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                parser.next();
                continue;
            }
            String name = parser.getName();
            if(name.equals("MoreOffersUrl")){
                offerLink = readText(parser);
            }else if(name.equals("DetailPageURL")){
                sellLink = readText(parser);
            }else if(name.equals("TradeInValue")){
                Offer offer = new Offer();
                offer.setLink(offerLink);
                offer.setType(TransactionType.SELL);
                parser.nextTag();
                offer.setPrice(String.format("%.2f", Double.parseDouble(readText(parser))/100));
                offers.add(offer);
            }else if(name.equals("LowestNewPrice")){
                Offer offer = new Offer();
                offer.setLink(offerLink);
                offer.setType(TransactionType.BUY);
                parser.nextTag();
                offer.setPrice(String.format("%.2f", Double.parseDouble(readText(parser))/100));
                offers.add(offer);
            }else if(name.equals("LowestUsedPrice")){
                Offer offer = new Offer();
                offer.setLink(offerLink);
                offer.setType(TransactionType.BUY);
                parser.nextTag();
                offer.setPrice(String.format("%.2f", Double.parseDouble(readText(parser))/100));
                offers.add(offer);
            }else{
                parser.next();
            }
        }
        for(Offer offer : offers){
            if(offer.getType() == TransactionType.BUY || offer.getType() == TransactionType.RENT) {
                offer.setLink(offerLink);
            }else{
                offer.setLink(sellLink);
            }
            offer.setRetailer("Amazon");
        }
        return offers;
    }

    private String readText(XmlPullParser parser) throws Exception{
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    public void generateSignedRequestURLISBN(String input){
        System.out.println("Map form example:");
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("AssociateTag", "swyftbooksapp-20");
        params.put("Operation", "ItemLookup");
        params.put("ResponseGroup", "ItemAttributes,OfferSummary,Offers");
        params.put("SearchIndex", "Books");
        params.put("ItemId", input);
        params.put("IdType", "ISBN");
        signedRequestURL = helper.sign(params);
    }

    public void sendRequest(Context context, String input, final ServerCallback callback) {
        if(input == null){
            return;
        }
        generateSignedRequestURLISBN(input);

        requestSingleton = VolleyRequestSingleton.getInstance(context.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, signedRequestURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                xmlFile = response;
                try {
                    items = parseFeed();
                    callback.onSuccess(items);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestSingleton.addToRequestQueue(stringRequest);
    }
}
