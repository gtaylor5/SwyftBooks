package com.swyftlabs.swyftbooks.ViewHolders;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.swyftlabs.swyftbooks.Activities.SeeOffersActivity;
import com.swyftlabs.swyftbooks.Classes.ResultItem;
import com.swyftlabs.swyftbooks.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference userRef = database.getReference("Users").child(user.getUid());
                    Map<String, String> data = new HashMap<>();
                    data.put("Author", bookAuthor.getText().toString());
                    data.put("ISBN", bookISBN.getText().toString());
                    data.put("Search Date", (new SimpleDateFormat("MMM DD, YYYY")).format(new Date()));
                    data.put("Title", bookTitle.getText().toString());
                    userRef.child("Searches").child(bookISBN.getText().toString()).setValue(data);
                }

                Intent intent = new Intent(v.getContext(), SeeOffersActivity.class);
                intent.putExtra("item", item);
                v.getContext().startActivity(intent);
            }
        });
    }

}
