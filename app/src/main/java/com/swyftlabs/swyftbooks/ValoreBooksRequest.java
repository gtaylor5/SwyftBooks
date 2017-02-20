package com.swyftlabs.swyftbooks;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Gerard on 2/19/2017.
 */

public class ValoreBooksRequest implements RetailerRequest {

    private String xmlFile;
    private String requestURL = "";
    private VolleyRequestSingleton requestSingleton;
    public ArrayList<Offer> items = new ArrayList<>();


    public ValoreBooksRequest () {

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
            if(tagName.equals("rental-offer")) {
                Offer tempOffer = readChild(parser);
                tempOffer.setType(TransactionType.RENT);
                items.add(tempOffer);
            }else if(tagName.equals("sale-offer")){
                Offer tempOffer = readChild(parser);
                tempOffer.setType(TransactionType.SELL);
                items.add(tempOffer);
            }else if(tagName.equals("buy-offer")) {
                Offer tempOffer = readChild(parser);
                tempOffer.setType(TransactionType.BUY);
                items.add(tempOffer);
            }
        }
    }

    @Override
    public Offer readChild(XmlPullParser parser) throws Exception {

        String currentTagName = parser.getName();
        Offer offer = new Offer();
        parser.next();
        while(!currentTagName.equals(parser.getName())){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                parser.next();
                continue;
            }

            if(currentTagName.equals("rental-offer")) {
                String name = parser.getName();
                if (name.equals("condition")) {
                    offer.setCondition(readText(parser));
                } else if (name.equals("ninty-day-price")) {
                    offer.setSecondPrice(readText(parser));
                } else if (name.equals("quantity")) {
                    offer.setQuantity(readText(parser));
                } else if (name.equals("semester-price")) {
                    offer.setSecondPrice(readText(parser));
                } else if (name.equals("link")) {
                    offer.setLink(readText(parser));
                } else {
                    parser.next();
                }
            }else if(currentTagName.equals("sale-offer")){
                String name = parser.getName();
                if (name.equals("condition")) {
                    offer.setCondition(readText(parser));
                } else if (name.equals("price")) {
                    offer.setSecondPrice(readText(parser));
                } else if (name.equals("quantity")) {
                    offer.setQuantity(readText(parser));
                }else if (name.equals("link")) {
                    offer.setLink(readText(parser));
                } else {
                    parser.next();
                }
            }else if(currentTagName.equals("buy-offer")){
                String name = parser.getName();
                if (name.equals("condition")) {
                    offer.setCondition(readText(parser));
                } else if (name.equals("item-price")) {
                    offer.setSecondPrice(readText(parser));
                } else if (name.equals("quantity")) {
                    offer.setQuantity(readText(parser));
                }else if (name.equals("link")) {
                    offer.setLink(readText(parser));
                } else {
                    parser.next();
                }
            }
        }
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
        requestSingleton = VolleyRequestSingleton.getInstance(context.getApplicationContext());
        if(requestURL != ""){
            StringRequest stringRequest = new StringRequest(Request.Method.GET, requestURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    xmlFile = response;
                    try {
                        parseFeed();
                        callback.onSuccess(items);
                        Log.i("AppInfo", "success");
                    }catch (Exception e){
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

    @Override
    public void setRequestURL(String string) {
        this.requestURL = "http://prices.valorebooks.com/lookup-multiple-categories?SiteID=3FZG6Y&ProductCode=" + string +
                "&TrackingID=3FZG6Y&Level=Detailed&NumberToShow=35&MinimumCondition=5&ShowEditionType=yes";

    }
}
