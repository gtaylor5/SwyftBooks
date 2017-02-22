package com.swyftlabs.swyftbooks.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swyftlabs.swyftbooks.Classes.Offer;
import com.swyftlabs.swyftbooks.Classes.ResultItem;
import com.swyftlabs.swyftbooks.Classes.RetailerResultsItem;
import com.swyftlabs.swyftbooks.R;
import com.swyftlabs.swyftbooks.ViewHolders.RetailerResultViewHolder;

import java.util.ArrayList;

/**
 * Created by Gerard on 2/21/2017.
 */

public class RetailerResultsAdapter extends RecyclerView.Adapter<RetailerResultViewHolder> {

    private ArrayList<RetailerResultsItem> items;
    private LayoutInflater layoutInflater;

    public RetailerResultsAdapter(ArrayList<RetailerResultsItem> items, LayoutInflater layoutInflater){
        this.items = items;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public RetailerResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.retailerprices, parent, false);
        return new RetailerResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RetailerResultViewHolder holder, int position) {
        RetailerResultsItem item = items.get(position);
        holder.setViews(item);
    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    public void dataSetChaged(ArrayList<RetailerResultsItem> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }
}
