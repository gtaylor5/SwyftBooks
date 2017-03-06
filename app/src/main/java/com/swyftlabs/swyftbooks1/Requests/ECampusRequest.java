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

/**
 * Created by Gerard on 2/21/2017.
 */

public class ECampusRequest implements RetailerRequest {

    private String xmlFile;
    private String requestURL = "";
    private VolleyRequestSingleton volleyRequestSingleton;
    private ArrayList<Offer> offers = new ArrayList<>();
    private String isbn = "";


    public ECampusRequest() {

    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public void setRequestURL(String string) {
        requestURL =  "http://www.ecampus.com/botpricexml.asp?isbn="+ string;
        setIsbn(string);
    }

    @Override
    public void parseFeed() throws Exception {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xmlFile));

        while(parser.next() != parser.END_DOCUMENT){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String tagName = parser.getName();
            if(tagName.equals("item")) {
                readChild(parser);
            }
        }

    }

    @Override
    public Offer readChild(XmlPullParser parser) throws Exception {
        String currentTagName = parser.getName();

        parser.next();
        while(!currentTagName.equals(parser.getName())){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                parser.next();
                continue;
            }

            String name = parser.getName();
            if (name.equals("NewPrice")) {
                offers.add(setOffer(readText(parser).replaceAll("[$]",""), TransactionType.BUY));
                continue;
            }else if(name.equals("UsedPrice")){
                offers.add(setOffer(readText(parser), TransactionType.BUY));
                continue;
            }else if(name.equals("MarketPlacePrice")){
                offers.add(setOffer(readText(parser), TransactionType.BUY));
                continue;
            }else if(name.equals("eBookPrice")){
                offers.add(setOffer(readText(parser), TransactionType.RENT));
                continue;
            }else if(name.equals("RentalPrice")){
                offers.add(setOffer(readText(parser), TransactionType.RENT));
                continue;
            }else if(name.equals("Rental2Price")){
                offers.add(setOffer(readText(parser), TransactionType.RENT));
                continue;
            }else if(name.equals("Rental3Price")){
                offers.add(setOffer(readText(parser), TransactionType.RENT));
                continue;
            }else {
                parser.next();
            }
        }
        return null;
    }

    public Offer setOffer(String price, TransactionType type){
        Offer offer = new Offer();
        offer.setPrice(price);
        offer.setRetailer("eCampus.com");
        offer.setType(type);
        offer.setLink("http://www.kqzyfj.com/click-8044180-5029466-1390403653000?ISBN="+isbn);
        return offer;
    }

    @Override
    public String readText(XmlPullParser parser) throws Exception {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    @Override
    public void sendRequest(Context context, String input, final ServerCallback callback) {
        volleyRequestSingleton = VolleyRequestSingleton.getInstance(context.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                xmlFile = response;
                try{
                    parseFeed();
                    callback.onSuccess(offers);
                }catch(Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        volleyRequestSingleton.addToRequestQueue(stringRequest);
    }

}
