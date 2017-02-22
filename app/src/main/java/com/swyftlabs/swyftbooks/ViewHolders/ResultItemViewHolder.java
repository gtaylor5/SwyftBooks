package com.swyftlabs.swyftbooks.ViewHolders;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.swyftlabs.swyftbooks.Activities.SeeOffersActivity;
import com.swyftlabs.swyftbooks.Classes.ResultItem;
import com.swyftlabs.swyftbooks.R;

/**
 * Created by Gerard on 2/16/2017.
 */

public class ResultItemViewHolder extends RecyclerView.ViewHolder {

    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookISBN;

    private ImageView bookImage;

    private Button seeOffers;

    private View v;

    public ResultItemViewHolder(View view) {
        super(view);
        bookTitle = (TextView) view.findViewById(R.id.title);
        bookAuthor = (TextView) view.findViewById(R.id.author);
        bookISBN = (TextView) view.findViewById(R.id.isbn);
        seeOffers = (Button) view.findViewById(R.id.seeOffers);
        bookImage = (ImageView) view.findViewById(R.id.bookimage);
        v = view;
    }

    public void setViews(final ResultItem item){
        bookTitle.setText(item.getBookTitle());
        bookAuthor.setText(item.getBookAuthor());
        bookISBN.setText(item.getBookISBN());
        Picasso.with(v.getContext()).load(item.getBookImageLink()).fit().into(bookImage);
        seeOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SeeOffersActivity.class);
                intent.putExtra("item", item);
                v.getContext().startActivity(intent);
            }
        });
    }

}
