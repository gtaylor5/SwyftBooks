public interface RetailerRequest {

  void setRequestURL(String string);
  ArrayList<T> parseFeed();
  ResultItem readChild(XmlPullParser parser);
  String readText(XmlPullParser parser);
  void sendRequest(String input, final ServerCallBack callback);

}
