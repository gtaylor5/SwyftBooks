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

public class CheggRequest implements RetailerRequest {


    private String xmlFile;
    private String requestURL = "";
    private VolleyRequestSingleton volleyRequestSingleton;
    private ArrayList<Offer> offers = new ArrayList<>();
    private String isbn = "";


    public CheggRequest() {

    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public void setRequestURL(String string) {
        requestURL =  "http://api.chegg.com/rent.svc?KEY=ada6c485ab35b1d2d8189fc08e5c9015&PW=2745708&R=XML&V=4.0&isbn="+string+"&with_pids=1&results_per_page=50";
        setIsbn(string);
    }

    @Override
    public void parseFeed() throws Exception {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        String xmlCopy = "";
        int loc = 0;
        for(int i = 0; i < xmlFile.length(); i++){
            if(xmlFile.charAt(i) != '<'){
                loc++;
                continue;
            }
            break;
        }
        for(int i = loc; i < xmlFile.length(); i++){
            xmlCopy += Character.toString(xmlFile.charAt(i));
        }
        xmlFile = xmlCopy;
        parser.setInput(new StringReader(xmlFile));
        while(parser.next() != parser.END_DOCUMENT){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String tagName = parser.getName();
            if(tagName.equals("Term")) {
                Offer offer = readChild(parser);
                offer.setType(TransactionType.RENT);
                offer.setRetailer("Chegg");
                offer.printOffer();
                offers.add(offer);
            } else if(tagName.equals("SellPrice")){
                Offer offer = readChild(parser);
                offer.setType(TransactionType.BUY);
                offer.setRetailer("Chegg");
                offer.printOffer();
                offers.add(offer);
            } else if(tagName.equals("Ebook")){
                Offer offer = readChild(parser);
                offer.setType(TransactionType.RENT);
                offer.setRetailer("Chegg");
                offer.printOffer();
                offers.add(offer);
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
            if(currentTagName.equals("Term")) {
                String name = parser.getName();
                if (name.equals("price")) {
                    offer.setPrice(readText(parser));
                } else if (name.equals("pid")) {
                    offer.setLink("http://chggtrx.com/click.track?CID=267582&AFID=393411&ADID=1088043&SID=&PIDs=" + readText(parser));
                } else {
                    parser.next();
                }
            }else if(currentTagName.equals("SellPrice")) {
                String name = parser.getName();
                if (name.equals("price")) {
                    offer.setPrice(readText(parser));
                } else if (name.equals("pid")) {
                    offer.setLink("http://chggtrx.com/click.track?CID=267582&AFID=393411&ADID=1088043&SID=&PIDs=" + readText(parser));
                } else {
                    parser.next();
                }
            }else if(currentTagName.equals("Ebook")){
                String name = parser.getName();
                if (name.equals("price")) {
                    offer.setPrice(readText(parser));
                } else if (name.equals("pid")) {
                    offer.setLink("http://chggtrx.com/click.track?CID=267582&AFID=393411&ADID=1088043&SID=&PIDs=" + readText(parser));
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
