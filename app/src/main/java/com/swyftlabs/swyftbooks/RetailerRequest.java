package com.swyftlabs.swyftbooks;
import android.content.Context;

import java.util.ArrayList;
import org.xmlpull.v1.*;
public interface RetailerRequest {

  void setRequestURL(String string);
  void parseFeed() throws Exception;
  Offer readChild(XmlPullParser parser) throws Exception;
  String readText(XmlPullParser parser) throws Exception;
  void sendRequest(Context context, String input, final ServerCallback callback);

}
