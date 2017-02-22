package com.swyftlabs.swyftbooks.ViewHolders;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swyftlabs.swyftbooks.Classes.Offer;
import com.swyftlabs.swyftbooks.Classes.ResultItem;
import com.swyftlabs.swyftbooks.Classes.RetailerResultsItem;
import com.swyftlabs.swyftbooks.R;

import org.w3c.dom.Text;

/**
 * Created by Gerard on 2/20/2017.
 */

public class RetailerResultViewHolder<RetailerResultItem> extends RecyclerView.ViewHolder {

    private Button buyPrice;
    private Button sellPrice;
    private Button rentPrice;

    private TextView buyText;
    private TextView sellText;
    private TextView rentText;

    private ImageView retailerLogo;

    private LinearLayout buyOffer;
    private LinearLayout sellOffer;
    private LinearLayout rentOffer;


    private View v;

    public RetailerResultViewHolder(View item) {
        super(item);

        buyPrice = (Button) item.findViewById(R.id.goToBuy);
        rentPrice = (Button) item.findViewById(R.id.goToRent);
        sellPrice = (Button) item.findViewById(R.id.goToSell);
        retailerLogo = (ImageView) item.findViewById(R.id.retailerLogo);
        buyText = (TextView) item.findViewById(R.id.buyText);
        sellText = (TextView) item.findViewById(R.id.sellText);
        rentText = (TextView) item.findViewById(R.id.rentText);
        buyOffer = (LinearLayout) item.findViewById(R.id.buyPrice);
        sellOffer = (LinearLayout) item.findViewById(R.id.sellPrice);
        rentOffer = (LinearLayout) item.findViewById(R.id.rentPrice);
        v = item;

    }

    public void setViews(final RetailerResultsItem item){
        setImageView(item.getRetailer());
        if(item.getBuyOffers().size() != 0) {
            if(buyPrice.getVisibility() == View.INVISIBLE){
                buyPrice.setVisibility(View.VISIBLE);
            }
            sellText.setText("Sell");
            buyOffer.setVisibility(View.VISIBLE);
            rentOffer.setVisibility(View.VISIBLE);
            String price = "$"+String.format("%.2f",Double.parseDouble(item.getBuyOffers().get(0).getPrice(true)));
            buyPrice.setText(price);
            buyPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    goToSite(v, item.getBuyOffers().get(0).getLink());
                }
            });
        }else {
            buyPrice.setVisibility(View.INVISIBLE);
        }

        if(item.getRentOffers().size() != 0) {
            if(rentPrice.getVisibility() == View.INVISIBLE){
                rentPrice.setVisibility(View.VISIBLE);
            }
            sellText.setText("Sell");
            buyText.setVisibility(View.VISIBLE);
            rentText.setVisibility(View.VISIBLE);
            String price = "$"+String.format("%.2f",Double.parseDouble(item.getRentOffers().get(0).getPrice(true)));
            rentPrice.setText(price);
            rentPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToSite(v, item.getRentOffers().get(0).getLink());
                }
            });
        }else {
            rentPrice.setVisibility(View.INVISIBLE);
        }

        if(item.getSellOffers().size() != 0) {
            if(sellPrice.getVisibility() == View.INVISIBLE){
                sellPrice.setVisibility(View.VISIBLE);
            }
            sellText.setText("Sell");
            buyOffer.setVisibility(View.VISIBLE);
            rentOffer.setVisibility(View.VISIBLE);
            String price = "$"+String.format("%.2f",Double.parseDouble(item.getSellOffers().get(0).getPrice(true)));
            sellPrice.setText(price);
            sellPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToSite(v, item.getSellOffers().get(0).getLink());
                }
            });
        }else {
            sellPrice.setVisibility(View.INVISIBLE);
        }

        if(item.getOffers().size() != 0){
            if(sellPrice.getVisibility() == View.INVISIBLE){
                sellPrice.setVisibility(View.VISIBLE);
            }
            String price = "$"+String.format("%.2f",Double.parseDouble(item.getOffers().get(0).getPrice(true)));
            sellPrice.setText(price);
            ViewGroup.LayoutParams params = sellText.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            sellText.setLayoutParams(params);
            sellText.setText("Best Offer");

            ViewGroup.LayoutParams buttonParams = sellPrice.getLayoutParams();
            buttonParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            sellPrice.setLayoutParams(buttonParams);

            buyOffer.setVisibility(View.GONE);
            rentOffer.setVisibility(View.GONE);

            sellPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToSite(v, item.getOffers().get(0).getLink());
                }
            });
        }

    }

    public void goToSite(View v, String item){
        String link = item;
        Log.i("AppInfo", link);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        v.getContext().startActivity(intent);
    }

    public void setImageView(String name){
        if(name.equals("ValoreBooks")){
            retailerLogo.setImageResource(R.drawable.valore);
        }else if(name.equals("BiggerBooks.com")){
            retailerLogo.setImageResource(R.drawable.biggerbooks);
        }else if(name.equals("eCampus.com")){
            retailerLogo.setImageResource(R.drawable.ecampuslogo);
        }else if(name.equals("Textbooks.com")){
            retailerLogo.setImageResource(R.drawable.tblogo);
        }else if(name.equals("Barnes & Noble")){
            retailerLogo.setImageResource(R.drawable.bnlogo);
        }else if(name.equals("Knetbooks.com")){
            retailerLogo.setImageResource(R.drawable.knetbookslogo);
        }else if(name.equals("eBooks.com")){
            retailerLogo.setImageResource(R.drawable.ebookslogo);
        }else if(name.equals("AbeBooks")){
            retailerLogo.setImageResource(R.drawable.abebookslogoprofile);
        }else if(name.equals("Chegg")){
            retailerLogo.setImageResource(R.drawable.chegg);
        }else{
            retailerLogo.setImageResource(R.drawable.vitalsource);
        }
    }

}
