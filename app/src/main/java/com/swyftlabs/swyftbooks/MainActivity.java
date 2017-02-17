package com.swyftlabs.swyftbooks;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

import org.w3c.dom.Text;

import java.lang.reflect.Array;
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
    private ArrayList<ResultItem> items = new ArrayList<>();
    private LinearLayoutManager manager;

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






    public void initializeRemoteConfig(){
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("AWS_ACCESS_KEY_ID", "AKIAJWKQAZX4GB63XKKA");
        defaults.put("AWS_SECRET_KEY","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");
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
                    Log.i("AppInfo", "Got remoteconfig");
                    Log.i("AppInfo", awsSecretKey);
                    Log.i("AppInfo", awsSecretKeyID);
                }else {
                    awsSecretKeyID = remoteConfig.getString("AWS_ACCESS_KEY_ID");
                    awsSecretKey = remoteConfig.getString("AWS_SECRET_KEY");
                    amazonRequest = new AmazonRequest(awsSecretKeyID, awsSecretKey);
                    Log.i("AppInfo", "succcess");
                }
            }
        });
    }


    public void setRecyclerViewListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                manager.setSmoothScrollbarEnabled(true);
                counter.setText(manager.findFirstVisibleItemPosition()+1 + "/" + manager.getItemCount());
            }
        });
    }

    public void setSearchBarListener() {
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    if(searchBar.getText().toString().length() == 0){
                        showToast("Please enter an ISBN, Title or Author.");
                        return false;
                    }
                    amazonRequest.sendRequest(getApplicationContext(), searchBar.getText().toString(), 1, new ServerCallback() {
                        @Override
                        public void onSuccess(ArrayList<ResultItem> items) {
                            counter.setVisibility(View.VISIBLE);
                            items = amazonRequest.getItems();
                            adapter.dataSetChaged(items);
                            setRecyclerViewListener();
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
                segue(BarcodeScannerActivity.class);
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
