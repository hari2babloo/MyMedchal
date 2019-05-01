package com.androidhari.mymedchal.SupportFiles;

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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

import Classess.QAmodels;
import Classess.Reviewmodels;


/**
 * A simple {@link Fragment} subclass.
 */
public class QaFragment extends Fragment {


    RecyclerView mRecycleView;
    private FirebaseRecyclerAdapter<QAmodels, NewsViewHolder> mPeopleRVAdapter;
    DatabaseReference mDatabase;


    public QaFragment() {
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

        View v = inflater.inflate(R.layout.fragment_qa, container, false);
        // Inflate the layout for this fragment
        mRecycleView = (RecyclerView)v.findViewById(R.id.recyclerview);

        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Q&A");
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("nameofbusiness").equalTo("GA Mobiles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("data",dataSnapshot.toString());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    QAmodels ss = ds.getValue(QAmodels.class);

//                    stars.setText(ss.getStars());
                    Log.e("reviews", ss.getAns());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
            }
        });


        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<QAmodels>().setQuery(mDatabase,  QAmodels.class).build();


        mPeopleRVAdapter = new FirebaseRecyclerAdapter<QAmodels, NewsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(NewsViewHolder holder, final int position, final QAmodels model) {

                holder.question.setText(model.getQuestion());
                holder.username.setText(model.getUsername());
                holder.answer.setText(model.getAns());
                Glide.with(getContext()).load(model.profilepic).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.usrimg);

//                holder.setImage(getBaseContext(), model.getImage());
                Log.e("result",model.getAns());

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
        TextView question;
        TextView answer;
        public NewsViewHolder(View itemView){
            super(itemView);
            question = (TextView)itemView.findViewById(R.id.title);
            answer = (TextView)itemView.findViewById(R.id.desc);
            username = (TextView)itemView.findViewById(R.id.username);
            usrimg  = (ImageView)itemView.findViewById(R.id.usrimg);


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
