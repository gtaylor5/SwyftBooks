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

public class AbeBooksRequest implements RetailerRequest {
    private String xmlFile;
    private String requestURL = "";
    private VolleyRequestSingleton volleyRequestSingleton;
    private ArrayList<Offer> offers = new ArrayList<>();

    public AbeBooksRequest () {

    }

    @Override
    public void setRequestURL(String string) {
       requestURL = "http://search2.abebooks.com/search?isbn="+string+"&clientkey=759b57aa-22c0-4d15-ad4d-328de084e968";
    }

    @Override
    public void parseFeed() throws Exception {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xmlFile));
        while(parser.next() != parser.END_DOCUMENT){
            while(parser.next() != parser.END_DOCUMENT){
                if(parser.getEventType() != XmlPullParser.START_TAG){
                    continue;
                }
                String tagName = parser.getName();
                if(tagName.equals("Book")) {
                    offers.add(readChild(parser));
                }
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
            String name = parser.getName();
            if (name.equals("listingPrice")) {
                offer.setPrice(readText(parser));
            }else if(name.equals("listingUrl")){
                offer.setLink("http://"+readText(parser));
            } else {
                parser.next();
            }
        }
        offer.setRetailer("AbeBooks");
        offer.setType(TransactionType.BUY);
        return offer;
    }

    @Override
    public String readText(XmlPullParser parser) throws Exception {
        return parser.nextText();
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
