package com.androidhari.mymedchal;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Classess.CategoryModel;
import Classess.DetailsModel;
import Classess.Signup;
import Classess.SubCategoryModel;

public class BusinessLists extends AppCompatActivity {

    RecyclerView mRecycleView;
    private FirebaseRecyclerAdapter<DetailsModel, NewsViewHolder> mPeopleRVAdapter;
    DatabaseReference mDatabase;
    // private FirebaseRecyclerAdapter<Signup, TaskViewHolder> adapter;
    private FirebaseRecyclerOptions<Signup> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_category);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Mobiles");



        mDatabase.keepSynced(true);

        mRecycleView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Count ", "" + dataSnapshot.toString());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {











//            //  String eventID= dataSnapshot.child("Hospitals").getKey();
//            String eventID = String.valueOf(ds.getKey().equalsIgnoreCase("Shoppings"));
//            CategoryModel post = ds.getValue(CategoryModel.class);
//            Log.e("Get Data", post.getName());


//
//            categories.add(post.getName());
//            categoriesimg.add(post.getImg());
//            Log.d("TAG", eventID);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });







        //
        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<DetailsModel>().setQuery(mDatabase,  DetailsModel.class).build();


        mPeopleRVAdapter = new FirebaseRecyclerAdapter<DetailsModel, NewsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(NewsViewHolder holder, final int position, final DetailsModel model) {

                holder.setTitle(model.getName());
                holder.setDesc(model.getCategory());
//                holder.setImage(getBaseContext(), model.getImage());
                Log.e("result",model.getName());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String url = model.getName();
                        Intent intent = new Intent(getApplicationContext(), Details.class);
                        intent.putExtra("id", url);
                        startActivity(intent);
                        Log.e("ress", url);
                    }
                });
            }

            @Override
            public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardviewtemplate, parent, false);

                return new NewsViewHolder(view);
            }
        };

        mRecycleView.setAdapter(mPeopleRVAdapter);



    }
    //
//
//
//
//
    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public NewsViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title){
            TextView post_title = (TextView)mView.findViewById(R.id.text1);
            post_title.setText(title);
        }
        public void setDesc(String desc){
            TextView post_desc = (TextView)mView.findViewById(R.id.text);
            post_desc.setText(desc);
        }

    }
    //
//
    @Override
    protected void onStart() {
        super.onStart();
        mPeopleRVAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPeopleRVAdapter.stopListening();
    }


}











