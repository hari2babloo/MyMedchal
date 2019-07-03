package com.androidhari.mymedchal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import Classess.FeedModel;
import Classess.ManageListModel;
import Classess.TinyDB;

public class Feed extends AppCompatActivity {



    Spinner spinner;
    String spinnerlocation, datasnapshot;
    //    List<String> items = new ArrayList<String>();
    RecyclerView mRecycleView;
    LinearLayout linearLayout;
    String key;
    String selecteddate;

    DatabaseReference databaseReference;
    ProgressDialog pd;
    TinyDB tinyDB;
    AdView mAdView;
    TextView toolbartitle;
    Toolbar toolbar;
    private AdapterFish Adapter;
    ArrayList<FeedModel> manageListModels = new ArrayList<>();
    DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);
        pd = new ProgressDialog(this);
        tinyDB = new TinyDB(this);
        spinner = (Spinner)findViewById(R.id.spinner);
        mRecycleView = (RecyclerView) findViewById(R.id.recycler_view);
        tinyDB = new TinyDB(this);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        tinyDB = new TinyDB(this);

        toolbartitle = (TextView)toolbar.findViewById(R.id.toolbar_title);
        toolbartitle.setText("Feeds");
        MobileAds.initialize(Feed.this,"ca-app-pub-3574852791589889~6439948019");


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        linearLayout = (LinearLayout)findViewById(R.id.layout);
        linearLayout.setVisibility(View.GONE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("offers").child("feeds");
        pd.setCancelable(false);
        pd.setMessage("Getting Feeds");
        pd.show();

        databaseReference.limitToLast(50).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()){
                    manageListModels.clear();
                    Log.e("keys", dataSnapshot.toString());
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String eventID = ds.getKey();


                        FeedModel manageListModel = ds.getValue(FeedModel.class);
//                        Log.e(manageListModel.getKey(),manageListModel.getLocation());
                        manageListModels.add(manageListModel);

                    }

                    pd.dismiss();
                    Adapter = new Feed.AdapterFish(Feed.this, manageListModels);
                    Adapter.setHasStableIds(false);
                    mRecycleView.setAdapter(Adapter);

                    //mRVFishPrice.getLayoutManager().scrollToPosition(0);
                    mRecycleView.setHasFixedSize(false);
                    mRecycleView.setLayoutManager(new LinearLayoutManager(Feed.this,LinearLayoutManager.VERTICAL,false));
                }

                else {
                    linearLayout.setVisibility(View.VISIBLE);
                    pd.dismiss();
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }



    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<FeedModel> data = Collections.emptyList();
        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;
        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(Context context, List<FeedModel> data) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.managelisttemplate, parent, false);
            final Feed.AdapterFish.MyHolder holder = new Feed.AdapterFish.MyHolder(view);

            return holder;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
        // Bind data

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            final AdapterFish.MyHolder myHolder = (AdapterFish.MyHolder) holder;
            //   mRVFishPrice.scrollToPosition(position);
            //    holder.setIsRecyclable(true);
            final FeedModel current = data.get(position);




//            Log.e("recycleritems",data.get(position).getLocation());

            myHolder.name.setText(current.getTitle());
            myHolder.location.setText(current.getLocation());
            myHolder.subcat.setText(current.getDesc());
//            String dateString = new SimpleDateFormat("dd/MM/yyyy").format(new Date(current.getTimestamp()));
//            myHolder.timestamp.setText(dateString);
            myHolder.imgnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    tinyDB.putString("location",current.getLocation());
//                    tinyDB.putString("key",current.getKey());
//                    Log.e("Key",tinyDB.getString("key"));
//                    Log.e("location",tinyDB.getString("location"));

//                    startActivity(new Intent(Feed.this,Seller_Dashpage.class));
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tinyDB.putString("location",current.getLocation());
                    tinyDB.putString("key",current.getKey());
                    Log.e("Key",tinyDB.getString("key"));
                    Log.e("location",tinyDB.getString("location"));

                    startActivity(new Intent(Feed.this,Seller_Dashpage.class));
                }
            });

//                myHolder.edit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//

//
//                    }
//                });
//
//                myHolder.view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });


        }
        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {
            TextView name,timestamp,location,subcat;
            ImageView imgnext;
            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);



                name = itemView.findViewById(R.id.name);
                timestamp = itemView.findViewById(R.id.timstamp);
                location = itemView.findViewById(R.id.location);
                imgnext = itemView.findViewById(R.id.imgnext);

                subcat = itemView.findViewById(R.id.subcat);

//                view = itemView.findViewById(R.id.view);
//                edit = itemView.findViewById(R.id.edit);




            }

        }

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
                startActivity(new Intent(Feed.this,Main2Activity.class));
                return true;

            case  R.id.homee :

                startActivity(new Intent(Feed.this,Main2Activity.class));


        }

        return super.onOptionsItemSelected(item);
    }

}