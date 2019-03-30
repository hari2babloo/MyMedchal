package com.androidhari.mymedchal;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.core.Context;

import Classess.Signup;

public class SubCategory extends AppCompatActivity {

    RecyclerView mRecycleView;
    FirebaseRecyclerAdapter<Signup,PeopleVH> mPeopleRVAdapter;
   // private FirebaseRecyclerAdapter<Signup, TaskViewHolder> adapter;
    private FirebaseRecyclerOptions<Signup> options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_category);


        mRecycleView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));



        //loadTasks();


    }

    protected void onStart() {

        super.onStart();
        mPeopleRVAdapter.startListening();

    }

    protected void onStop() {

mPeopleRVAdapter.stopListening();
        super.onStop();
    }


    private void loadTasks() {

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Locations").child("Athvelly").child("Shopping").child("Clothing") ;
        Query personsQuery = personsRef.orderByKey();


        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Signup>().setQuery(personsQuery, Signup.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Signup, PeopleVH>(personsOptions) {
            @Override
            protected void onBindViewHolder(PeopleVH holder, int position, Signup model) {
                holder.setDetails(model.imgurl,model.userid);
                Log.e("Hello","Hello");
            }

            @Override
            public PeopleVH onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardviewtemplate, parent, false);

                return new PeopleVH(view);
            }
        };

        mRecycleView.setAdapter(mPeopleRVAdapter);

    }


    private class PeopleVH extends RecyclerView.ViewHolder {
        View mView;

        public PeopleVH(View itemView) {
            super(itemView);
            mView = itemView;
        }


        public void setDetails(String imgurl,String uid) {
            TextView user_name = (TextView) mView.findViewById(R.id.text1);
            TextView user_status = (TextView) mView.findViewById(R.id.text);

            user_name.setText(imgurl);
            user_status.setText(uid);
        }
    }



}
