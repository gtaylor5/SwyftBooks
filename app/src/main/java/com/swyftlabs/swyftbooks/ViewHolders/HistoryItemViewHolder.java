package com.swyftlabs.swyftbooks.ViewHolders;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
 * Created by Gerard on 2/27/2017.
 */

public class HistoryItemViewHolder  extends RecyclerView.ViewHolder{

    private ImageView bookImage;

    private TextView titleView;
    private TextView authorView;
    private TextView isbnView;
    private TextView editionView;
    private TextView publisherView;
    private TextView bindingView;
    private TextView listPriceView;

    private View v;
    private Animation animation;


    public HistoryItemViewHolder(View view) {
        super(view);
        bookImage = (ImageView) view.findViewById(R.id.image);

        titleView = (TextView) view.findViewById(R.id.title);
        authorView = (TextView) view.findViewById(R.id.author);
        isbnView = (TextView) view.findViewById(R.id.isbn);
        editionView = (TextView) view.findViewById(R.id.edition);
        publisherView = (TextView) view.findViewById(R.id.publisher);
        bindingView = (TextView) view.findViewById(R.id.binding);
        listPriceView = (TextView) view.findViewById(R.id.listPrice);
        animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce);
        v = view;
    }

    public void setViews(final ResultItem item){
        Picasso.with(v.getContext()).load(item.getBookImageLink()).fit().into(bookImage);
        titleView.setText(item.getBookTitle());
        authorView.setText(item.getBookAuthor());
        isbnView.setText((item.getBookISBN()==null) ? item.getBookEAN() : item.getBookISBN());
        editionView.setText("Edition: "+((item.getBookEdition() == null) ? "" : item.getBookEdition()));
        publisherView.setText("Publisher: "+((item.getBookPublisher() == null) ? "" : item.getBookPublisher()));
        bindingView.setText("Binding: "+((item.getBookBinding() == null) ? "" : item.getBookBinding()));
        listPriceView.setText((item.getBookListPrice() == null) ? "" : "List Price: $"+Double.parseDouble(item.getBookListPrice())/100.00);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(animation);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference userRef = database.getReference("Users").child(user.getUid());
                    Map<String, String> data = new HashMap<>();
                    data.put("Author", authorView.getText().toString());
                    data.put("ISBN", isbnView.getText().toString());
                    data.put("Search Date", (new SimpleDateFormat("MMM dd, YYYY")).format(new Date()));
                    data.put("Title", titleView.getText().toString());
                    userRef.child("Searches").child(isbnView.getText().toString()).setValue(data);
                }

                Intent intent = new Intent(v.getContext(), SeeOffersActivity.class);
                intent.putExtra("item", item);
                v.getContext().startActivity(intent);
            }
        });

    }

}
