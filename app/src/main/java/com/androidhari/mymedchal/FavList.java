    package com.androidhari.mymedchal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Classess.DetailsModel;
import Classess.Signup;
import Classess.TinyDB;

public class FavList extends AppCompatActivity {

    RecyclerView mRecycleView;

    DatabaseReference mDatabase;
    ArrayList<DetailsModel> filterdata= new ArrayList<DetailsModel>();
    TinyDB tinyDB;
    private AdapterFish Adapter;
    RecyclerView mRVFishPrice;

    TextView toolbartitle;
    AdView mAdView;
    ArrayList<DetailsModel> lstArrayList = new ArrayList<DetailsModel>();
    android.support.v7.widget.Toolbar toolbar;

    // private FirebaseRecyclerAdapter<Signup, TaskViewHolder> adapter;
    private FirebaseRecyclerOptions<Signup> options;

    ImageView imageView;
    TextView textView3;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_list);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        tinyDB = new TinyDB(this);

        toolbartitle = (TextView)toolbar.findViewById(R.id.toolbar_title);
        toolbartitle.setText("Favourite List");
        MobileAds.initialize(FavList.this,"ca-app-pub-3574852791589889~6439948019");


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mRVFishPrice = (RecyclerView)findViewById(R.id.recycler_view); 


        imageView = (ImageView)findViewById(R.id.imageView3);
        textView3 = (TextView)findViewById(R.id.textView3);

        imageView.setVisibility(View.GONE);
        textView3.setVisibility(View.GONE);
        parsedata();


    }

    private void parsedata() {


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(FavList.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("fav", null);



        if (json==null){



        }else {
            Type type = new TypeToken<List<DetailsModel>>() {}.getType();
            lstArrayList = gson.fromJson(json, type);
            if(lstArrayList!=null && !lstArrayList.isEmpty() ){
                attachtorectcler();
            }
            else {
                textView3.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
            }
        }

    }

    private void attachtorectcler() {

        Adapter = new AdapterFish(FavList.this, lstArrayList);
        Adapter.setHasStableIds(false);

        mRVFishPrice.scrollToPosition(0);
        mRVFishPrice.setHasFixedSize(true);

//        mRVFishPrice.addItemDecoration(new DividerItemDecoration(getContext(),
//                DividerItemDecoration.HORIZONTAL));
//        mRVFishPrice.addItemDecoration(new DividerItemDecoration(getContext(),
//                DividerItemDecoration.VERTICAL));

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRVFishPrice.setLayoutManager(mLayoutManager);
        mRVFishPrice.setAdapter(Adapter);
    }


    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<DetailsModel> data = Collections.emptyList();
        int currentPos = 0;
        private android.content.Context context;
        private LayoutInflater inflater;
        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(android.content.Context context, ArrayList<DetailsModel> data) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.cardviewtemplate, parent, false);
            final AdapterFish.MyHolder holder = new AdapterFish.MyHolder(view);

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
            final DetailsModel current = data.get(position);

            // final modelPickuplist current = data.get(position);
            //  holder.getLayoutPosition();
            //    setHasStableIds(true);
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("key",current.getKey());
                    tinyDB.putString("key",current.getKey());
                    startActivity(new Intent(FavList.this,Details.class));

                }
            });

            Log.e("imgurl", String.valueOf(current));

            myHolder.appanme.setText(current.getName());
            myHolder.address.setText(current.getAddress() + "," +current.getLandmark());
            myHolder.desc.setText(current.getDescription() );
            myHolder.timings.setText("Timings : " +current.timingsfrom +"-"+current.timingsto);
            myHolder.workingdays.setText("Working : " +current.workindays);
            Glide.with(FavList.this).load(current.img).apply(RequestOptions.centerCropTransform()).into(myHolder.logoimg);


            if (current.band.equalsIgnoreCase("na")){

                myHolder.band.setVisibility(View.GONE);
            }
            else {
                myHolder.band.setText(current.band);

            }


        }
        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {
            View mView;
            TextView appanme,address,desc,timings,workingdays,band;
            ImageView logoimg;
            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                mView = itemView;
                //expresimg = itemView.findViewById(R.id.expresimg);
                appanme = (TextView)mView.findViewById(R.id.statusmsg);
                address= (TextView)mView.findViewById(R.id.text1);
                desc = (TextView)mView.findViewById(R.id.subHeadingText);
                timings = (TextView)mView.findViewById(R.id.timing);
                workingdays =(TextView)mView.findViewById(R.id.workingdays);
                logoimg = (ImageView) mView.findViewById(R.id.appImage);
                band = (TextView) mView.findViewById(R.id.band);

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
                startActivity(new Intent(FavList.this,Main2Activity.class));
                return true;

            case  R.id.homee :

                startActivity(new Intent(FavList.this,Main2Activity.class));


        }

        return super.onOptionsItemSelected(item);
    }
}