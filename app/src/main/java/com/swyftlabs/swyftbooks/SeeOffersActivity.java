package com.swyftlabs.swyftbooks;

import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SeeOffersActivity extends AppCompatActivity {

    private ImageView bookImage;

    private TextView titleView;
    private TextView authorView;
    private TextView isbnView;
    private TextView editionView;
    private TextView publisherView;
    private TextView bindingView;
    private TextView listPriceView;

    private ValoreBooksRequest valoreBooksRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_offers);

        bookImage = (ImageView) findViewById(R.id.image);

        titleView = (TextView) findViewById(R.id.title);
        authorView = (TextView) findViewById(R.id.author);
        isbnView = (TextView) findViewById(R.id.isbn);
        editionView = (TextView) findViewById(R.id.edition);
        publisherView = (TextView) findViewById(R.id.publisher);
        bindingView = (TextView) findViewById(R.id.binding);
        listPriceView = (TextView) findViewById(R.id.listPrice);

        ResultItem item = getIntent().getParcelableExtra("item");

        Picasso.with(getApplicationContext()).load(item.getBookImageLink()).fit().into(bookImage);
        titleView.setText(item.getBookTitle());
        authorView.setText(item.getBookAuthor());
        isbnView.setText((item.getBookISBN()==null) ? item.getBookEAN() : item.getBookISBN());
        editionView.setText("Edition: "+((item.getBookEdition() == null) ? "" : item.getBookEdition()));
        publisherView.setText("Publisher: "+((item.getBookPublisher() == null) ? "" : item.getBookPublisher()));
        bindingView.setText("Binding: "+((item.getBookBinding() == null) ? "" : item.getBookBinding()));
        listPriceView.setText((item.getBookListPrice() == null) ? "" : "List Price: $"+Double.parseDouble(item.getBookListPrice())/100.00);


        valoreBooksRequest = new ValoreBooksRequest();
        getOffers();

    }

    public void getOffers(){
        valoreBooksRequest.setRequestURL(isbnView.getText().toString());
        valoreBooksRequest.sendRequest(getApplicationContext(), isbnView.getText().toString(), new ServerCallback() {
            @Override
            public <T> void onSuccess(T items) {

            }

            @Override
            public <T> void onSuccess(ArrayList<T> items) {
                for(Object o : items){
                   Offer m = (o instanceof Offer) ? ((Offer) o) : null;
                   m.printOffer();
                }
            }
        });
    }

}
