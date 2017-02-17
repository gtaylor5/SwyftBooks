package com.swyftlabs.swyftbooks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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

    public void setViews(ResultItem item){
        bookTitle.setText(item.getBookTitle());
        bookAuthor.setText(item.getBookAuthor());
        bookISBN.setText(item.getBookISBN());
        Picasso.with(v.getContext()).load(item.getBookImageLink()).into(bookImage);
    }

}
