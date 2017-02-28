package com.swyftlabs.swyftbooks.Activities;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.swyftlabs.swyftbooks.Adapters.HistoryResultsAdapter;
import com.swyftlabs.swyftbooks.Classes.ResultItem;
import com.swyftlabs.swyftbooks.ContentProviders.BookInformationContactProvider;
import com.swyftlabs.swyftbooks.ContentProviders.DatabaseUtil;
import com.swyftlabs.swyftbooks.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<ResultItem> items = new ArrayList<>();
    private HistoryResultsAdapter historyResultsAdapter;
    public static final String TABLE_NAME = "SEARCHES";

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        items.removeAll(items);
        setItems();
        recyclerView = (RecyclerView) findViewById(R.id.historyTable);
        historyResultsAdapter = new HistoryResultsAdapter(getLayoutInflater(), items);
        recyclerView.setAdapter(historyResultsAdapter);
        manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void setItems() {
        ArrayList<ResultItem> dbItems = DatabaseUtil.getContacts(getApplicationContext());
        if(dbItems != null) {
            items.addAll(dbItems);
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Uh Oh!")
                    .setMessage("There are no items in your search history. Please search for an item.")
                    .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}
