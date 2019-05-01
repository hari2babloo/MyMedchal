package com.androidhari.mymedchal.SupportFiles;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhari.mymedchal.BusinessLists;
import com.androidhari.mymedchal.Details;
import com.androidhari.mymedchal.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Classess.DetailsModel;
import Classess.Reviewmodels;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment {

    TextView stars,title,describe,timestamp,userid,username,img,nameofbusines,location;
    RecyclerView  mRecycleView;
    private FirebaseRecyclerAdapter<Reviewmodels, NewsViewHolder> mPeopleRVAdapter;
    DatabaseReference mDatabase;
    TextView reviewtxt;
    public ReviewsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reviews, container, false);
  //      stars = (TextView)v.findViewById(R.id.stars);
        mRecycleView = (RecyclerView)v.findViewById(R.id.recyclerview);
        reviewtxt = (TextView)v.findViewById(R.id.review);

        reviewtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext(),R.style.DialoTheme);

                dialog.setContentView(R.layout.writereview);
                dialog.setTitle("Title...");

                // set the custom dialog components - text, image and button
//                TextView text = (TextView) dialog.findViewById(R.id.text);
//                text.setText("Android custom dialog example!");
//                ImageView image = (ImageView) dialog.findViewById(R.id.image);
//                image.setImageResource(R.drawable.bellcon);

                Button dialogButton = (Button) dialog.findViewById(R.id.buttonok);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }

        });

        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
         mDatabase = FirebaseDatabase.getInstance().getReference().child("reviews");
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("nameofbusiness").equalTo("GA Mobiles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("data",dataSnapshot.toString());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Reviewmodels ss = ds.getValue(Reviewmodels.class);

//                    stars.setText(ss.getStars());
                    Log.e("reviews", ss.getStars());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
            }
        });


        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Reviewmodels>().setQuery(mDatabase,  Reviewmodels.class).build();
        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Reviewmodels, NewsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(NewsViewHolder holder, final int position, final Reviewmodels model) {
                holder.post_title.setText(model.getStars());
                holder.post_desc.setText(model.getDescription());
                holder.username.setText(model.getUsername());
                                holder.ratingBar.setRating(Float.parseFloat(model.getStars()));
                Glide.with(getContext()).load(model.image).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.image);
//                holder.setImage(getBaseContext(), model.getImage());
                Log.e("result",model.getStars());
            }

            @Override
            public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.review_template_fragment, parent, false);

                return new NewsViewHolder(view);
            }
        };

        mRecycleView.setAdapter(mPeopleRVAdapter);



        return v;
    }


    public static class NewsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageView usrimg,image;
        TextView username;
        RatingBar ratingBar;
        TextView post_title;
        TextView post_desc;
        public NewsViewHolder(View itemView){
            super(itemView);
             post_title = (TextView)itemView.findViewById(R.id.title);
             post_desc = (TextView)itemView.findViewById(R.id.desc);
            username = (TextView)itemView.findViewById(R.id.username);
            usrimg  = (ImageView)itemView.findViewById(R.id.usrimg);
            image  = (ImageView)itemView.findViewById(R.id.image);
            ratingBar = (RatingBar)itemView.findViewById(R.id.stars);

//            mView = itemView;
        }



    }

    @Override
    public void onStart() {
        super.onStart();
        mPeopleRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPeopleRVAdapter.stopListening();
    }
}
