package com.swyftlabs.swyftbooks1.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swyftlabs.swyftbooks1.Classes.ResultItem;
import com.swyftlabs.swyftbooks1.R;
import com.swyftlabs.swyftbooks1.ViewHolders.ResultItemViewHolder;

import java.util.ArrayList;

/**
 * Created by Gerard on 2/16/2017.
 */

public class SearchResultsAdapter extends RecyclerView.Adapter<ResultItemViewHolder> {

    private ArrayList<ResultItem> results;
    private LayoutInflater layoutInflater;

    public SearchResultsAdapter(LayoutInflater layoutInflater, ArrayList<ResultItem> results) {
        this.layoutInflater = layoutInflater;
        this.results = results;
    }

    @Override
    public ResultItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.resultitem, parent, false);
        return new ResultItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultItemViewHolder holder, int position) {
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
}
