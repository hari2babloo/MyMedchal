package com.androidhari.mymedchal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Classess.Signup;
import Classess.SubCategoryModel;
import Classess.TinyDB;

public class SubCategory extends AppCompatActivity {

    RecyclerView mRecycleView;
    private FirebaseRecyclerAdapter<SubCategoryModel, NewsViewHolder> mPeopleRVAdapter;
    DatabaseReference mDatabase;
    // private FirebaseRecyclerAdapter<Signup, TaskViewHolder> adapter;
    private FirebaseRecyclerOptions<Signup> options;
    TinyDB tinyDB;
Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_category);


          toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tinyDB = new TinyDB(this);
        mDatabase =  FirebaseDatabase.getInstance().getReference().child("SubCategory");



        mDatabase.keepSynced(true);

        mRecycleView = (RecyclerView) findViewById(R.id.recycler_view);

         mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));


//mDatabase.orderByValue().equalTo(tinyDB.getString("Category")).addListenerForSingleValueEvent(new ValueEventListener() {
//    @Override
//    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//        Log.e("Count ", "" + dataSnapshot.toString());
//        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//
////            //  String eventID= dataSnapshot.child("Hospitals").getKey();
////            String eventID = String.valueOf(ds.getKey().equalsIgnoreCase("Shoppings"));
////            CategoryModel post = ds.getValue(CategoryModel.class);
////            Log.e("Get Data", post.getName());
//
//
////
////            categories.add(post.getName());
////            categoriesimg.add(post.getImg());
////            Log.d("TAG", eventID);
//
//        }
//    }
//
//    @Override
//    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//    }
//
//});







        //
        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<SubCategoryModel>().setQuery(mDatabase.orderByChild("category").equalTo(tinyDB.getString("Category")),  SubCategoryModel.class).build();


        mPeopleRVAdapter = new FirebaseRecyclerAdapter<SubCategoryModel, NewsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(NewsViewHolder holder, final int position, final SubCategoryModel model) {


                holder.setTitle(model.getName());
                holder.setDesc(model.getDesc());
                holder.setimg(model.getImg());
//                holder.setImage(getBaseContext(), model.getImage());
Log.e("result",model.getName());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //final String url = model.getName();

                    Log.e("Key",model.name);
                        getSnapshots().get(position).toString();
                        Intent intent = new Intent(getApplicationContext(), BusinessLists.class);
                        tinyDB.putString("name",model.getName());
                        tinyDB.putString("subcatkey",getSnapshots().get(position).name.toString());
                        startActivity(intent);
//                        Log.e("ress", url);
                    }
                });
            }

            @Override
            public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardviewsubcategory_template, parent, false);

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
    public class NewsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public NewsViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setimg(String img){

            ImageView img2 = (ImageView)mView.findViewById(R.id.appImage);
            Glide.with(SubCategory.this).load(img).diskCacheStrategy(DiskCacheStrategy.DATA).into(img2);
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
                startActivity(new Intent(SubCategory.this,Main2Activity.class));
                return true;

            case  R.id.homee :

                startActivity(new Intent(SubCategory.this,Main2Activity.class));


        }

        return super.onOptionsItemSelected(item);
    }
    }











