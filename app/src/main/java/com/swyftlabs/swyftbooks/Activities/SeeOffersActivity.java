package com.swyftlabs.swyftbooks.Activities;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;
import com.swyftlabs.swyftbooks.Adapters.RetailerResultsAdapter;
import com.swyftlabs.swyftbooks.Classes.RetailerResultsItem;
import com.swyftlabs.swyftbooks.ContentProviders.DatabaseUtil;
import com.swyftlabs.swyftbooks.Requests.AbeBooksRequest;
import com.swyftlabs.swyftbooks.Requests.CheggRequest;
import com.swyftlabs.swyftbooks.Requests.CommissionJunctionRequest;
import com.swyftlabs.swyftbooks.Classes.Offer;
import com.swyftlabs.swyftbooks.R;
import com.swyftlabs.swyftbooks.Classes.ResultItem;
import com.swyftlabs.swyftbooks.Requests.ECampusRequest;
import com.swyftlabs.swyftbooks.Requests.ServerCallback;
import com.swyftlabs.swyftbooks.Requests.ValoreBooksRequest;
import com.swyftlabs.swyftbooks.Requests.VolleyRequestSingleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

public class SeeOffersActivity extends AppCompatActivity {

    private ImageView bookImage;

    private TextView titleView;
    private TextView authorView;
    private TextView isbnView;
    private TextView editionView;
    private TextView publisherView;
    private TextView bindingView;
    private TextView listPriceView;

    private RecyclerView recyclerView;

    private ValoreBooksRequest valoreBooksRequest;
    private AbeBooksRequest abeBooksRequest;
    private CommissionJunctionRequest commissionJunctionRequest;
    private ECampusRequest eCampusRequest;
    private CheggRequest cheggRequest;

    private LinearLayoutManager layoutManager;
    private RetailerResultsAdapter adapter;
    
    private ResultItem item;
    private volatile ArrayList<RetailerResultsItem> retailerResultsItems = new ArrayList<>();
    private HashSet<String> retailers = new HashSet<>();

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
        recyclerView = (RecyclerView) findViewById(R.id.priceResults);

        item = getIntent().getParcelableExtra("item");

        DatabaseUtil.updateContact(getApplicationContext(), item);
        removeDuplicates();

        Picasso.with(getApplicationContext()).load(item.getBookImageLink()).fit().into(bookImage);
        titleView.setText(item.getBookTitle());
        authorView.setText(item.getBookAuthor());
        isbnView.setText((item.getBookISBN()==null) ? item.getBookEAN() : item.getBookISBN());
        editionView.setText("Edition: "+((item.getBookEdition() == null) ? "" : item.getBookEdition()));
        publisherView.setText("Publisher: "+((item.getBookPublisher() == null) ? "" : item.getBookPublisher()));
        bindingView.setText("Binding: "+((item.getBookBinding() == null) ? "" : item.getBookBinding()));
        listPriceView.setText((item.getBookListPrice() == null) ? "" : "List Price: $"+Double.parseDouble(item.getBookListPrice())/100.00);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new RetailerResultsAdapter(retailerResultsItems, getLayoutInflater());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        valoreBooksRequest = new ValoreBooksRequest();
        commissionJunctionRequest = new CommissionJunctionRequest();
        abeBooksRequest = new AbeBooksRequest();
        eCampusRequest = new ECampusRequest();
        cheggRequest = new CheggRequest();
        getOffers();
        retailers.removeAll(retailers);
    }

    public void removeDuplicates(){
        ArrayList<ResultItem> items = DatabaseUtil.getContacts(this);
        HashSet<String> isbns = new HashSet<>();
        for(ResultItem item : items){
            if(isbns.contains(item.getBookISBN())){
                DatabaseUtil.delete(this, item.getId());
                DatabaseUtil.findContact(this, item.getId());
            }else{
                isbns.add(item.getBookISBN());
            }
        }
    }

    public void getOffers(){
        valoreBooksRequest.setRequestURL(isbnView.getText().toString());
        valoreBooksRequest.sendRequest(getApplicationContext(), isbnView.getText().toString(), new ServerCallback() {

            @Override
            public <T> void onSuccess(ArrayList<T> items) {

                ArrayList<Offer> offers = (ArrayList<Offer>) items;
                if(retailers.contains(offers.get(0).getRetailer())){
                    return;
                }else{
                    retailers.add(offers.get(0).getRetailer());
                }
                adapter.notifyDataSetChanged();
                retailerResultsItems.add(new RetailerResultsItem(offers));
            }

            @Override
            public void onSuccess(HashMap<String, ArrayList<Offer>> items) {

            }
        });
        //Barnes and Noble Only
        commissionJunctionRequest.setRequestURL(item.getBookISBN());
        commissionJunctionRequest.sendRequest(getApplicationContext(), "", new ServerCallback() {

            @Override
            public <T> void onSuccess(ArrayList<T> items) {

            }

            @Override
            public void onSuccess(HashMap<String, ArrayList<Offer>> items) {
                for(String key: items.keySet()){
                    if(retailers.contains(key)){
                        continue;
                    }else{
                        retailers.add(key);
                    }
                    retailerResultsItems.add(new RetailerResultsItem(items.get(key)));
                }
                adapter.notifyDataSetChanged();
            }
        });

        commissionJunctionRequest.setRequestURL(item.getBookEAN());
        commissionJunctionRequest.sendRequest(getApplicationContext(), "", new ServerCallback() {

            @Override
            public <T> void onSuccess(ArrayList<T> items) {
            }

            @Override
            public void onSuccess(HashMap<String, ArrayList<Offer>> items) {
                for(String key: items.keySet()){
                    if(retailers.contains(key)){
                        continue;
                    }else{
                        retailers.add(key);
                    }
                    retailerResultsItems.add(new RetailerResultsItem(items.get(key)));
                }
                adapter.notifyDataSetChanged();
            }
        });

        abeBooksRequest.setRequestURL(item.getBookISBN());
        abeBooksRequest.sendRequest(getApplicationContext(), "", new ServerCallback() {
            @Override
            public <T> void onSuccess(ArrayList<T> items) {
                retailerResultsItems.add(new RetailerResultsItem((ArrayList<Offer>)items));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(HashMap<String, ArrayList<Offer>> items) {
            }
        });

        eCampusRequest.setRequestURL(item.getBookISBN());
        eCampusRequest.sendRequest(getApplicationContext(), "", new ServerCallback() {
            @Override
            public <T> void onSuccess(ArrayList<T> items) {
                retailerResultsItems.add(new RetailerResultsItem((ArrayList<Offer>)items));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(HashMap<String, ArrayList<Offer>> items) {

            }
        });

        cheggRequest.setRequestURL(item.getBookISBN());
        cheggRequest.sendRequest(getApplicationContext(), "", new ServerCallback() {
            @Override
            public <T> void onSuccess(ArrayList<T> items) {
                retailerResultsItems.add(new RetailerResultsItem((ArrayList<Offer>)items));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(HashMap<String, ArrayList<Offer>> items) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
