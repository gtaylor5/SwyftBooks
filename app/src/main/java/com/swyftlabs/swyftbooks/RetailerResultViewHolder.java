package com.swyftlabs.swyftbooks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Gerard on 2/20/2017.
 */

public class RetailerResultViewHolder<RetailerResultItem> extends RecyclerView.ViewHolder {

    private Button buyPrice;
    private Button sellPrice;
    private Button rentPrice;

    private ImageView retailerLogo;

    private View v;

    public RetailerResultViewHolder(View item) {
        super(item);

        buyPrice = (Button) item.findViewById(R.id.goToBuy);
        rentPrice = (Button) item.findViewById(R.id.goToRent);
        sellPrice = (Button) item.findViewById(R.id.goToSell);

    }

    public void setViews(final RetailerResultsItem item){

    }
}
