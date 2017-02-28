package com.swyftlabs.swyftbooks.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.swyftlabs.swyftbooks.Classes.Offer;
import com.swyftlabs.swyftbooks.Requests.AmazonRequest;
import com.swyftlabs.swyftbooks.R;
import com.swyftlabs.swyftbooks.Classes.ResultItem;
import com.swyftlabs.swyftbooks.Adapters.SearchResultsAdapter;
import com.swyftlabs.swyftbooks.Requests.ServerCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    RelativeLayout logoutTab;
    RelativeLayout shareTab;
    RelativeLayout scanTab;
    RelativeLayout historyTab;

    TextView logoutText;
    TextView shareText;
    TextView scanText;
    TextView historyText;
    TextView counter;

    EditText searchBar;

    RecyclerView recyclerView;
    SearchResultsAdapter adapter;

    FirebaseRemoteConfig remoteConfig;

    private String awsSecretKeyID = "";
    private String awsSecretKey = "";

    private AmazonRequest amazonRequest;
    public ArrayList<ResultItem> items = new ArrayList<>();
    private LinearLayoutManager manager;

    private int currentPage = 1;
    private int previousPage = 1;
    private boolean loading = false;
    private int previousNumberOfItems = 0;
    private String currentSearchTerm = "";

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutTab = (RelativeLayout) findViewById(R.id.logoutTab);
        shareTab = (RelativeLayout) findViewById(R.id.shareTab);
        scanTab = (RelativeLayout) findViewById(R.id.scanTab);
        historyTab = (RelativeLayout) findViewById(R.id.historyTab);

        logoutText = (TextView) findViewById(R.id.logoutText);
        shareText = (TextView) findViewById(R.id.shareText);
        scanText = (TextView) findViewById(R.id.scanText);
        historyText = (TextView) findViewById(R.id.historyText);
        counter = (TextView) findViewById(R.id.counter);

        searchBar = (EditText) findViewById(R.id.searchbar);

        adapter = new SearchResultsAdapter(getLayoutInflater(), items);

        recyclerView = (RecyclerView) findViewById(R.id.searchResults);
        recyclerView.setAdapter(adapter);
        manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        counter.setVisibility(View.INVISIBLE);
        setTabs();
        setSearchBarListener();
        initializeRemoteConfig();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() == null){

            }else{
                previousPage = 1;
                currentPage = 1;
                searchBar.setText(result.getContents());
                sendRequest();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void initializeRemoteConfig(){
        final Map<String, Object> defaults = new HashMap<>();
        defaults.put("AWS_ACCESS_KEY_ID", "");
        defaults.put("AWS_SECRET_KEY","");
        remoteConfig = FirebaseRemoteConfig.getInstance();
        remoteConfig.setDefaults(defaults);
        FirebaseRemoteConfigSettings settings = new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(true).build();
        remoteConfig.setConfigSettings(settings);
        remoteConfig.fetch(0).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    remoteConfig.activateFetched();
                    awsSecretKeyID = remoteConfig.getString("AWS_ACCESS_KEY_ID");
                    awsSecretKey = remoteConfig.getString("AWS_SECRET_KEY");
                    amazonRequest = new AmazonRequest(awsSecretKeyID, awsSecretKey);
                }else {
                    awsSecretKeyID = defaults.get("AWS_ACCESS_KEY_ID").toString();
                    awsSecretKey = defaults.get("AWS_SECRET_KEY").toString();
                    amazonRequest = new AmazonRequest(awsSecretKeyID, awsSecretKey);
                }
            }
        });
    }

    public void setRecyclerViewListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItems = recyclerView.getChildCount();
                int totalItems = manager.getItemCount();
                counter.setText(manager.findFirstVisibleItemPosition() + 1 +" of " + totalItems);
                if(loading){
                    if(totalItems > previousNumberOfItems){
                        loading = false;
                        previousNumberOfItems = totalItems;
                    }
                }

                if(!loading && (totalItems - visibleItems) <= (manager.findFirstVisibleItemPosition() + 3)) {
                    if (currentPage == previousPage) {
                        currentPage++;
                        reloadData();
                        loading = true;
                    }
                }
            }
        });
    }

    public void sendRequest(){
        if(searchBar.getText().toString().length() == 0){
            showToast("Please enter an ISBN, Title or Author.");
            return;
        }
        if(searchBar.getText().toString().equals(currentSearchTerm)){
            return;
        }else{
            currentSearchTerm = searchBar.getText().toString();
            items.removeAll(items);
        }
        amazonRequest.sendRequest(getApplicationContext(), searchBar.getText().toString(), currentPage, new ServerCallback() {
            @Override
            public <T> void onSuccess(ArrayList<T> item) {
                items = amazonRequest.getItems();
                adapter.dataSetChaged(items);
                setRecyclerViewListener();
                if(items.size() != 0) {
                    manager.scrollToPosition(0);
                    counter.setVisibility(View.VISIBLE);
                    counter.setText(manager.findFirstVisibleItemPosition() + 1 + "/" + items.size());
                }else{
                    showToast("There were no results to show. Please try again.");
                }
            }
            @Override
            public void onSuccess(HashMap<String, ArrayList<Offer>> items) {

            }
        });
    }

    public void reloadData() {
        amazonRequest.sendRequest(getApplicationContext(), searchBar.getText().toString(), currentPage, new ServerCallback() {
            @Override
            public <T> void onSuccess(ArrayList<T> item) {
                counter.setVisibility(View.VISIBLE);
                items.addAll(amazonRequest.getItems());
                adapter.dataSetChaged(items);
                previousPage++;
            }

            @Override
            public void onSuccess(HashMap<String, ArrayList<Offer>> items) {

            }
        });
    }

    public void setSearchBarListener() {
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                View view = getCurrentFocus();
                if(view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                }
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    if(searchBar.getText().toString().length() == 0){
                        showToast("Please enter an ISBN, Title or Author.");
                        return false;
                    }
                    if(searchBar.getText().toString().equals(currentSearchTerm)){
                        return false;
                    }else{
                        currentSearchTerm = searchBar.getText().toString();
                        items.removeAll(items);
                    }
                    amazonRequest.sendRequest(getApplicationContext(), searchBar.getText().toString(), currentPage, new ServerCallback() {
                        @Override
                        public <T> void onSuccess(ArrayList<T> item) {
                            items = amazonRequest.getItems();
                            adapter.dataSetChaged(items);
                            setRecyclerViewListener();
                            if(items.size() != 0) {
                                manager.scrollToPosition(0);
                                counter.setVisibility(View.VISIBLE);
                                counter.setText(manager.findFirstVisibleItemPosition() + 1 + "/" + items.size());
                            }else{
                                showToast("There were no results to show. Please try again.");
                            }
                        }
                        @Override
                        public void onSuccess(HashMap<String, ArrayList<Offer>> items) {

                        }
                    });
                    return true;
                }
                return false;
            }
        });
    }

    public void setTabs(){

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Avenir-Book.otf");
        logoutText.setTypeface(font);
        shareText.setTypeface(font);
        scanText.setTypeface(font);
        historyText.setTypeface(font);
        searchBar.setTypeface(font);

        logoutTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    FirebaseAuth.getInstance().signOut();
                }
                segue(LoginActivity.class);
                finish();
            }
        });

        scanTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scan a Barcode");
                intentIntegrator.initiateScan();
            }
        });

        historyTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                segue(HistoryActivity.class);
            }
        });

        shareTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject));
                share.putExtra(Intent.EXTRA_TEXT, "Save money on your books!\n\nAndroid: https://play.google.com/store/apps/details?id=com.swyftlabs.swyftbooks\n\n" +
                        "iOS: https://itunes.apple.com/us/app/swyftbooks-search.-save.-sell/id1139931160?ls=1&mt=8");
                startActivity(Intent.createChooser(share, "Share via"));

            }
        });

    }

    public void segue(Class con){
        Intent intent = new Intent(MainActivity.this, con);
        startActivity(intent);
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    }

}
