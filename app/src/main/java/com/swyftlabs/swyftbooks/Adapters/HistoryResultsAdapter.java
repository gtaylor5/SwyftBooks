package com.swyftlabs.swyftbooks.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swyftlabs.swyftbooks.Classes.ResultItem;
import com.swyftlabs.swyftbooks.R;
import com.swyftlabs.swyftbooks.ViewHolders.HistoryItemViewHolder;
import com.swyftlabs.swyftbooks.ViewHolders.ResultItemViewHolder;

import java.util.ArrayList;

/**
 * Created by Gerard on 2/27/2017.
 */

public class HistoryResultsAdapter extends RecyclerView.Adapter<HistoryItemViewHolder>{

    private ArrayList<ResultItem> results;
    private LayoutInflater layoutInflater;

    public HistoryResultsAdapter(LayoutInflater layoutInflater, ArrayList<ResultItem> results) {
        this.layoutInflater = layoutInflater;
        this.results = results;
    }

    @Override
    public HistoryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.historyitem, parent, false);
        return new HistoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryItemViewHolder holder, int position) {
        ResultItem item = results.get(position);
        holder.setViews(item);
    }

    @Override
    public int getItemCount() {
        return this.results.size();
    }

    public void dataSetChaged(ArrayList<ResultItem> items) {
        this.results = items;
        notifyDataSetChanged();
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }
}
