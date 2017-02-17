package com.swyftlabs.swyftbooks;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gerard on 2/16/2017.
 *
 *
 * Generalized Amazon request. This request will work for ISBN, Title or Author.
 *
 *
 */

public class AmazonRequest {

    private String xmlFile;
    private String signedRequestURL = "";
    private final String ENDPOINT = "ecs.amazonaws.com";
    private SignedRequestsHelper helper;



    private ArrayList<ResultItem> items = new ArrayList<>();


    public AmazonRequest(String keyID, String secretKey) {

        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, keyID, secretKey);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<ResultItem> getItems() {
        return items;
    }


    public ArrayList<ResultItem> parseFeed() throws Exception {
        ArrayList<ResultItem> results = new ArrayList<>();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = xmlPullParserFactory.newPullParser();
        parser.setInput(new StringReader(xmlFile));
        while(parser.next() != parser.END_DOCUMENT){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String tagName = parser.getName();
            if(tagName.equals("Item")) {
                results.add(readChild(parser));
            }
        }
        return results;
    }

    public ResultItem readChild(XmlPullParser parser) throws Exception{
        String currentTagName = parser.getName();
        ResultItem item = new ResultItem();
        parser.next();
        while(!currentTagName.equals(parser.getName())){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                parser.next();
                continue;
            }
            String name = parser.getName();
            if(name.equals("LargeImage")){
                item.images.add(readImageLink(parser));
            }else if(name.equals("Author")){
                item.authors.add(readText(parser));
            }else if(name.equals("Binding")){
                item.setBookBinding(readText(parser));
            }else if(name.equals("EAN")){
                item.setBookEAN(readText(parser));
            }else if(name.equals("Edition")){
                item.setBookEdition(readText(parser));
            }else if(name.equals("ISBN")){
                item.setBookISBN(readText(parser));
            }else if(name.equals("ListPrice")){
                item.setBookListPrice(readListPrice(parser));
            }else if(name.equals("Publisher")){
                item.setBookPublisher(readText(parser));
            }else if(name.equals("Title")){
                item.setBookTitle(readText(parser));
            }else{
                parser.next();
            }
        }
        return item;
    }


    private String readListPrice(XmlPullParser parser) throws Exception {
        parser.next();
        return readText(parser);
    }

    private String readImageLink(XmlPullParser parser) throws Exception{
        parser.next();
        return readText(parser);
    }

    private String readText(XmlPullParser parser) throws Exception{
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    public void generateSignedRequestURL(String input, int pageNum) {
        Map<String, String> params = new HashMap<>();
        params.put("Service", "AWSECommerceService");
        params.put("AssociateTag", "swyftbooksapp-20");
        params.put("Operation", "ItemSearch");
        params.put("ResponseGroup", "ItemAttributes,Offers, Images");
        params.put("SearchIndex", "Books");
        params.put("Keywords", input);
        params.put("ItemPage", String.valueOf(pageNum));
        signedRequestURL = helper.sign(params);
    }

    public void sendRequest(Context context, String input, int pageNum, final ServerCallback callback) {
        generateSignedRequestURL(input, pageNum);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
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
        requestQueue.add(stringRequest);
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


}
