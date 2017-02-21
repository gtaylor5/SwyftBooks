package com.swyftlabs.swyftbooks;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gerard on 2/20/2017.
 */

public class CommissionJunctionRequest implements RetailerRequest {

    private String xmlFile;
    private String requestURL = "";
    private HashMap<String, String> header = new HashMap<>();
    private VolleyRequestSingleton requestSingleton;
    public ArrayList<Offer> items = new ArrayList<>();

    public CommissionJunctionRequest() {
        header.put("Authorization","0090ea50e79aad5591e67493c4fa6bcace035b8a2158f09b14a63256d2ec36ccd2a70519cf7e26fee2bb6c627a6c766656a7b59137678028209b492625e8e3a987/" +
                "3b7223661ea96424c9a8a0c57904a6d903bf955640a9d2b411c92c9cb191794b814a97cf20eb900a1084f25a248a8c06a3b1bed094447508b7a849ffebe34361" );
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
            if(tagName.equals("product")) {
                Offer tempOffer = readChild(parser);
                tempOffer.setType(TransactionType.RENT);
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
            String name = parser.getName();
            Log.i("AppInfo", name);
            if (name.equals("advertiser-name")) {
                offer.setRetailer(readText(parser));
                Log.i("AppInfo", readText(parser));
            } else if (name.equals("buy-url")) {
                offer.setLink(readText(parser));
            } else if (name.equals("price")) {
                offer.setPrice(readText(parser));
            } else {
                parser.next();
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                xmlFile = response;
                try {
                    parseFeed();
                    callback.onSuccess(items);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map headers =  super.getParams();
                if(headers == null){
                    headers = new HashMap();
                }
                headers.put("Authorization", "0090ea50e79aad5591e67493c4fa6bcace035b8a2158f09b14a63256d2ec36ccd2a70519cf7e26fee2bb6c627a6c766656a7b59137678028209b492625e8e3a987/3b7223661ea96424c9a8a0c57904a6d903bf955640a9d2b411c92c9cb191794b814a97cf20eb900a1084f25a248a8c06a3b1bed094447508b7a849ffebe34361");
                return headers;
            }
        };
        requestSingleton.addToRequestQueue(stringRequest);
    }

    @Override
    public void setRequestURL(String string) {
        requestURL = "https://product-search.api.cj.com/v2/product-search?website-id=8044180&advertiser-ids=joined&isbn="+string;
        Log.i("AppInfo", requestURL);
    }
}
