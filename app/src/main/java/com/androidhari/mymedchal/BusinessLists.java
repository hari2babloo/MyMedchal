package com.androidhari.mymedchal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import Classess.DetailsModel;
import Classess.Signup;
import Classess.TinyDB;

public class BusinessLists extends AppCompatActivity {

    RecyclerView mRecycleView;
    private FirebaseRecyclerAdapter<DetailsModel, NewsViewHolder> mPeopleRVAdapter;
    DatabaseReference mDatabase;
    TinyDB tinyDB;
    android.support.v7.widget.Toolbar toolbar;
    // private FirebaseRecyclerAdapter<Signup, TaskViewHolder> adapter;
    private FirebaseRecyclerOptions<Signup> options;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_category);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tinyDB = new TinyDB(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("BusinessLists").child(tinyDB.getString("location"));
        mDatabase.keepSynced(true);
        mRecycleView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase.orderByChild("subcategory").equalTo(tinyDB.getString("subcatkey")).addListenerForSingleValueEvent(new ValueEventListener() {
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

        Query query = mDatabase.orderByChild("subcategory").equalTo(tinyDB.getString("subcatkey"));

        //
        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<DetailsModel>().setQuery(query,  DetailsModel.class).build();


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
                        Intent intent = new Intent(getApplicationContext(), Details.class);

                        tinyDB.putString("key",getRef(position).getKey());
                        startActivity(intent);
                        Log.e("ress", getRef(position).getKey());
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
            TextView post_desc = (TextView)mView.findViewById(R.id.statusmsg);
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


    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //handle the home button onClick event here.
                startActivity(new Intent(BusinessLists.this,SubCategory.class));
                return true;

            case  R.id.homee :

                startActivity(new Intent(BusinessLists.this,Main2Activity.class));


        }

        return super.onOptionsItemSelected(item);
    }


}











