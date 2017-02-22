package com.swyftlabs.swyftbooks.Requests;
import android.content.Context;

import com.swyftlabs.swyftbooks.Classes.Offer;

import org.xmlpull.v1.*;
public interface RetailerRequest {

  void setRequestURL(String string);
  void parseFeed() throws Exception;
  Offer readChild(XmlPullParser parser) throws Exception;
  String readText(XmlPullParser parser) throws Exception;
  void sendRequest(Context context, String input, final ServerCallback callback);

}
