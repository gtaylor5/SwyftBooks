package com.swyftlabs.swyftbooks1.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swyftlabs.swyftbooks1.Classes.RetailerResultsItem;
import com.swyftlabs.swyftbooks1.R;
import com.swyftlabs.swyftbooks1.ViewHolders.RetailerResultViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Gerard on 2/21/2017.
 */

public class RetailerResultsAdapter extends RecyclerView.Adapter<RetailerResultViewHolder> {

    private ArrayList<RetailerResultsItem> items;
    private LayoutInflater layoutInflater;

    public RetailerResultsAdapter(ArrayList<RetailerResultsItem> items, LayoutInflater layoutInflater){
        this.items = items;
        Collections.sort(this.items, new Comparator<RetailerResultsItem>() {
            @Override
            public int compare(RetailerResultsItem o1, RetailerResultsItem o2) {
                return (int) (o1.getCheapestPrice() - o2.getCheapestPrice());
            }
        });
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
    public void dataSetChaged() {
        this.items.addAll(items);
        notifyDataSetChanged();
    }
}
