package com.swyftlabs.swyftbooks;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    RelativeLayout logoutTab;
    RelativeLayout shareTab;
    RelativeLayout scanTab;
    RelativeLayout historyTab;

    TextView logoutText;
    TextView shareText;
    TextView scanText;
    TextView historyText;

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

        setTabs();

    }

    public void setTabs(){

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Avenir-Book.otf");
        logoutText.setTypeface(font);
        shareText.setTypeface(font);
        scanText.setTypeface(font);
        historyText.setTypeface(font);

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
                share.putExtra(Intent.EXTRA_TEXT, "Save money on your books!\nAndroid: https://play.google.com/store/apps/details?id=com.swyftlabs.swyftbooks\n" +
                        "iOS: https://itunes.apple.com/us/app/swyftbooks-search.-save.-sell/id1139931160?ls=1&mt=8");
                startActivity(Intent.createChooser(share, "Share via"));

            }
        });

    }

    public void segue(Class con){
        Intent intent = new Intent(MainActivity.this, con);
        startActivity(intent);
    }

}
