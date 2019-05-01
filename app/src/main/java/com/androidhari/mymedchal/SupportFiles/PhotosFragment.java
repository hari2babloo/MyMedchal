package com.androidhari.mymedchal.SupportFiles;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidhari.mymedchal.Details;
import com.androidhari.mymedchal.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Classess.Photosmodel;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosFragment extends Fragment {


    private FirebaseRecyclerAdapter<Photosmodel, NewsViewHolder> mPeopleRVAdapter;
    DatabaseReference mDatabase;
    DatabaseReference mDatabase2;

    private AdapterFish Adapter;
    RecyclerView mRVFishPrice;
    List<String> filterdata= new ArrayList<String>();


    //getPickupDeliveryOrders data = new getPickupDeliveryOrders();
    final ArrayList<String> dd = new ArrayList<>();


    private Animator currentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.

    ImageView expandedImageView;
    LinearLayout container;
    private int shortAnimationDuration;
    public PhotosFragment() {
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
        RecyclerView mRecycleView;
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_photos, container, false);
        //      stars = (TextView)v.findViewById(R.id.stars);
        mRVFishPrice = (RecyclerView)v.findViewById(R.id.recyclerview);
        expandedImageView  = (ImageView)v.findViewById(R.id.expanded_image);
        container = (LinearLayout)v.findViewById(R.id.container);
        mRVFishPrice.setHasFixedSize(true);
        mRVFishPrice.setLayoutManager(new GridLayoutManager(getContext(),3));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("photos");


        mDatabase.keepSynced(true);
        mDatabase.orderByKey().equalTo("-LbOhR4pW4FoDiSbQU5k").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e("photosdata",dataSnapshot.getKey());
                //              Photosmodel ss = dataSnapshot.getValue(Photosmodel.class);
//                Log.e("reviews", ss.getImgurl());

                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    Log.e("PhotoKeys",ds.getKey());


                    Photosmodel ss2 = ds.getValue(Photosmodel.class);

                    filterdata.add(ss2.imgurl);


//                    stars.setText(ss.getStars());
                    Log.e("photosname", ss2.getImgurl());

                }

                ((Details)getActivity()).dispatchInformations(filterdata);
                DiplayDaata();
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





//        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Photosmodel>().setQuery(mDatabase,  Photosmodel.class).build();
//
//
//        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Photosmodel, NewsViewHolder>(personsOptions) {
//            @Override
//            protected void onBindViewHolder(final NewsViewHolder holder, final int position, final Photosmodel model) {
//
//                DatabaseReference ref = mPeopleRVAdapter.getRef(position);
//                String key = ref.toString();
//
//                ref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//
//
//                            Log.e("2PhotoKeys",ds.getKey());
//
//
//                            Photosmodel ss2 = ds.getValue(Photosmodel.class);
//
////                    stars.setText(ss.getStars());
//                            Log.e("2photosname", ss2.getImgurl());
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
////                Log.e("Keysvalesus",key);
//
//
//                Glide.with(getContext()).load(model.imgurl).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.imageView);
//
//
//                holder.imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Log.e("photosposition",model.getImgurl());
//
//                        shortAnimationDuration = getResources().getInteger(
//                                android.R.integer.config_shortAnimTime);
//                        final Dialog fbDialogue = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar);
//
//                        fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
//                        fbDialogue.setContentView(R.layout.image_fullscreen);
//
//                        if (currentAnimator != null) {
//                            currentAnimator.cancel();
//                        }
//
//
//
//                        final ImageView expandedImageView = (ImageView)fbDialogue.findViewById(
//                                R.id.expanded_image);
//                        Glide.with(getContext()).load(model.getImgurl()).diskCacheStrategy(DiskCacheStrategy.DATA).into(expandedImageView);
//                        fbDialogue.setCancelable(true);
//                        fbDialogue.show();
//                    }
//                });
//                 Log.e("photosurl",model.imgurl);
//            }
//
//            @Override
//            public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.photos_template_fragment, parent, false);
//                return new NewsViewHolder(view);
//            }
//        };
//        mRVFishPrice.setAdapter(mPeopleRVAdapter);



        return v;
    }

    private void DiplayDaata() {

        Adapter = new AdapterFish(getContext(), filterdata);
        Adapter.setHasStableIds(false);
        mRVFishPrice.setAdapter(Adapter);
        mRVFishPrice.scrollToPosition(0);
        mRVFishPrice.setHasFixedSize(false);
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }


    public static class NewsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageView imageView;
        public NewsViewHolder(View itemView){
            super(itemView);
            imageView  = (ImageView) itemView.findViewById(R.id.image);
//            mView = itemView;
        }



    }



    @Override
    public void onStart() {
        super.onStart();
//        mPeopleRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
  //      mPeopleRVAdapter.stopListening();
    }

    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<String> data = Collections.emptyList();
        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;
        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(Context context, List<String> data) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.photos_template_fragment, parent, false);
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
            final String current = data.get(position);

            // final modelPickuplist current = data.get(position);
            //  holder.getLayoutPosition();
            //    setHasStableIds(true);

            Log.e("imgurl",current);
            Glide.with(getContext()).load(current).diskCacheStrategy(DiskCacheStrategy.DATA).into(((MyHolder) holder).servicename);


                ((MyHolder) holder).servicename.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        shortAnimationDuration = getResources().getInteger(
                                android.R.integer.config_shortAnimTime);
                        final Dialog fbDialogue = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar);

                        fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
                        fbDialogue.setContentView(R.layout.image_fullscreen);

                        if (currentAnimator != null) {
                            currentAnimator.cancel();
                        }



                        final ImageView expandedImageView = (ImageView)fbDialogue.findViewById(
                                R.id.expanded_image);
                        Glide.with(getContext()).load(current).diskCacheStrategy(DiskCacheStrategy.DATA).into(expandedImageView);
                        fbDialogue.setCancelable(true);
                        fbDialogue.show();
                    }
                });

        }
        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {

            ImageView expresimg, servicename;
            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                //expresimg = itemView.findViewById(R.id.expresimg);
                  servicename = (ImageView)itemView.findViewById(R.id.image);


            }
        }

        }
}
