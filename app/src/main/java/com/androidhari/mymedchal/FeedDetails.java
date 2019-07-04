package com.androidhari.mymedchal;

import android.animation.Animator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import Classess.TinyDB;

public class FeedDetails extends AppCompatActivity {



    //    List<String> items = new ArrayList<String>();
    RecyclerView mRecycleView;
    LinearLayout linearLayout;
    String key;
    private int shortAnimationDuration;
    private Animator currentAnimator;
    String selecteddate;

    DatabaseReference databaseReference;
    ProgressDialog pd;
    TinyDB tinyDB;
    AdView mAdView;
    TextView toolbartitle,title,desc,location,topic,contributer,timestamp;
    Toolbar toolbar;
    private AdapterFish Adapter;
    ArrayList<FeedModel> manageListModels = new ArrayList<>();
    ArrayList<String> imagelist = new ArrayList<>();
    DatabaseReference rootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_details);

        pd = new ProgressDialog(this);
        tinyDB = new TinyDB(this);

        mRecycleView = (RecyclerView) findViewById(R.id.recycler_view);
        title =(TextView)findViewById(R.id.title);
        location = (TextView)findViewById(R.id.location);
        topic = (TextView)findViewById(R.id.topic);
        desc = (TextView)findViewById(R.id.description);
        contributer = (TextView)findViewById(R.id.contributer);
        timestamp = (TextView)findViewById(R.id.timestamp);



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");



        toolbartitle = (TextView)toolbar.findViewById(R.id.toolbar_title);
        toolbartitle.setText("Feeds");
        MobileAds.initialize(FeedDetails.this,"ca-app-pub-3574852791589889~6439948019");


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent i = getIntent();
        FeedModel feedModel = (FeedModel) i.getSerializableExtra("feeddata");

        Log.e("feedtitle",feedModel.getTitle());
        title.setText(feedModel.getTitle());
        location.setText(feedModel.getLocation());
        topic.setText(feedModel.getTopic());

        String dateString = new SimpleDateFormat("dd/MM/yyyy").format(new Date(feedModel.getTimestamp()));
        timestamp.setText(dateString);
        desc.setText(feedModel.getDesc());
        contributer.setText(feedModel.getContributer());


        if (feedModel.getImaagearray().equalsIgnoreCase("yes")){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("offers").child("feeds").child("images");
            pd.setCancelable(false);
            pd.setMessage("Getting Feeds");
            pd.show();

            databaseReference.orderByChild("key").equalTo(tinyDB.getString("feedkey")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    if (dataSnapshot.exists()){
                        manageListModels.clear();
                        Log.e("receiveddata", dataSnapshot.toString());
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String eventID = ds.getKey();





                            FeedModel manageListModel = ds.getValue(FeedModel.class);
//                        Log.e(manageListModel.getKey(),manageListModel.getLocation());
                            manageListModels.add(manageListModel);

                        }

                        pd.dismiss();
                    Adapter = new AdapterFish(FeedDetails.this, manageListModels);
                    Adapter.setHasStableIds(false);
                    mRecycleView.setAdapter(Adapter);

                    //mRVFishPrice.getLayoutManager().scrollToPosition(0);
                    mRecycleView.setHasFixedSize(false);
                    mRecycleView.setLayoutManager(new LinearLayoutManager(FeedDetails.this,LinearLayoutManager.VERTICAL,false));
                    }

                    else {
//                    linearLayout.setVisibility(View.VISIBLE);
                        pd.dismiss();
                    }


                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });

        }



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
            View view = inflater.inflate(R.layout.photos_template_fragment, parent, false);
            final MyHolder holder = new MyHolder(view);

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
            final MyHolder myHolder = (MyHolder) holder;
            //   mRVFishPrice.scrollToPosition(position);
            //    holder.setIsRecyclable(true);
            final FeedModel current = data.get(position);




//            Log.e("recycleritems",data.get(position).getLocation());

//            myHolder.name.setText(current.getTitle());
//            myHolder.location.setText(current.getLocation());
//            Log.e("imageurl",current.getImage());
//            myHolder.subcat.setText(current.getDesc());
//            String dateString = new SimpleDateFormat("dd/MM/yyyy").format(new Date(current.getTimestamp()));
//            myHolder.timestamp.setText(dateString);

            Glide.with(FeedDetails.this).load(current.getUrl()).diskCacheStrategy(DiskCacheStrategy.DATA).into(myHolder.imageView);
            myHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    shortAnimationDuration = getResources().getInteger(
                            android.R.integer.config_shortAnimTime);
                    final Dialog fbDialogue = new Dialog(FeedDetails.this, android.R.style.Theme_Black_NoTitleBar);

                    fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
                    fbDialogue.setContentView(R.layout.image_fullscreen);

                    if (currentAnimator != null) {
                        currentAnimator.cancel();
                    }



                    final ImageView expandedImageView = (ImageView)fbDialogue.findViewById(
                            R.id.expanded_image);
                    Glide.with(FeedDetails.this).load(current.getUrl()).diskCacheStrategy(DiskCacheStrategy.DATA).into(expandedImageView);
                    fbDialogue.setCancelable(true);
                    fbDialogue.show();
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    tinyDB.putString("location",current.getLocation());
//                    tinyDB.putString("key",current.getKey());
//                    Log.e("Key",tinyDB.getString("key"));
//                    Log.e("location",tinyDB.getString("location"));
//
//                    startActivity(new Intent(Feed.this,Seller_Dashpage.class));
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

            ImageView imageView;
            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);




                imageView = itemView.findViewById(R.id.imageView);
                //              subcat = itemView.findViewById(R.id.subcat);

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
                startActivity(new Intent(FeedDetails.this,Main2Activity.class));
                return true;

            case  R.id.homee :

                startActivity(new Intent(FeedDetails.this,Main2Activity.class));


        }

        return super.onOptionsItemSelected(item);
    }
}
