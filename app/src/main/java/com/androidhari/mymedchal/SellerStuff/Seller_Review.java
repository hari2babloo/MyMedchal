package com.androidhari.mymedchal.SellerStuff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.animation.Animator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhari.mymedchal.R;
import com.androidhari.mymedchal.SupportFiles.ReviewsFragment;
import com.androidhari.mymedchal.SupportFiles.ScalingUtilities;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import Classess.Reviewmodels;
import Classess.TinyDB;

import static android.app.Activity.RESULT_OK;

public class Seller_Review extends AppCompatActivity {

    TextView stars,title,describe,timestamp,userid,username,img,nameofbusines,location;
    RecyclerView  mRecycleView;
    private FirebaseRecyclerAdapter<Reviewmodels, NewsViewHolder> mPeopleRVAdapter;
    DatabaseReference mDatabase;
    String picturepath;
    TextView reviewtxt,addimage;
    String imageuploaded = "none";
    ImageView imageView;
    private int shortAnimationDuration;
    Animator currentAnimator;
    ProgressDialog pd;
    TinyDB tinyDB;
    private static int RESULT_LOAD_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller__review);
        pd = new ProgressDialog(this);
        tinyDB = new TinyDB(this);
        mRecycleView = (RecyclerView)findViewById(R.id.recyclerview);
        reviewtxt = (TextView)findViewById(R.id.review);

       
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(Seller_Review.this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("reviews");
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("key").equalTo(tinyDB.getString("key")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("data",dataSnapshot.toString());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Reviewmodels ss = ds.getValue(Reviewmodels.class);

//                    stars.setText(ss.getStars());
//                    Log.e("reviews", ss.getStars());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Seller_Review.this, "No Data Found", Toast.LENGTH_SHORT).show();
            }
        });


        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Reviewmodels>().setQuery(mDatabase.orderByChild("key").equalTo(tinyDB.getString("key")).limitToLast(50),  Reviewmodels.class).build();
        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Reviewmodels, NewsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(NewsViewHolder holder, final int position, final Reviewmodels model) {
                holder.post_title.setText(model.title);
                holder.post_desc.setText("Owner Replies :  " +model.getDescription());
                holder.username.setText(model.getUsername());
                holder.ratingBar.setRating(Float.parseFloat(String.valueOf(model.getStars())));
                Glide.with(Seller_Review.this).load(model.image).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.image);
                Glide.with(Seller_Review.this).load(model.profilepic).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.usrimg);

                holder.reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        final String key = String.valueOf(getSnapshots().getSnapshot(position).getKey());

                        Log.e("Check",key);
                        final Dialog dialog = new Dialog(Seller_Review.this,R.style.DialoTheme);

                        dialog.setContentView(R.layout.addqa);
                        dialog.setTitle("Add Reply...");

                        final EditText editText = (EditText)dialog.findViewById(R.id.editText);

                        final Button submit = (Button)dialog.findViewById(R.id.submit);

                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (TextUtils.isEmpty(editText.getText())){
                                    View parentLayout = dialog.findViewById(R.id.parent);
                                    Snackbar.make(parentLayout, "please add your Answer", Snackbar.LENGTH_LONG)
                                            .setAction("CLOSE", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    dialog.dismiss();
                                                }
                                            })
                                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                            .show();


                                }
                                else {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                                    DatabaseReference myRef = database.getReference("reviews").child(key);
                                    myRef.child("ans").setValue(editText.getText().toString());
                                    dialog.cancel();
                                    dialog.dismiss();
                                }
                            }
                        });


                        Button dialogButton = (Button) dialog.findViewById(R.id.edit);
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
//                holder.setImage(getBaseContext(), model.getImage());

                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shortAnimationDuration = getResources().getInteger(
                                android.R.integer.config_shortAnimTime);
                        final Dialog fbDialogue = new Dialog(Seller_Review.this, android.R.style.Theme_Black_NoTitleBar);

                        fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
                        fbDialogue.setContentView(R.layout.image_fullscreen);

                        if (currentAnimator != null) {
                            currentAnimator.cancel();
                        }



                        final ImageView expandedImageView = (ImageView)fbDialogue.findViewById(
                                R.id.expanded_image);
                        Glide.with(Seller_Review.this).load(model.image).diskCacheStrategy(DiskCacheStrategy.DATA).into(expandedImageView);
                        fbDialogue.setCancelable(true);
                        fbDialogue.show();
                    }
                });

//                holder.setImage(getBaseContext(), model.getImage());
                Log.e("result", String.valueOf(model.getStars()));
            }

            @Override
            public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.seller_reply, parent, false);

                return new NewsViewHolder(view);
            }
        };

        mRecycleView.setAdapter(mPeopleRVAdapter);



    }



    public static class NewsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageView usrimg,image;
        TextView username,reply;
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
            reply = (TextView)itemView.findViewById(R.id.reply);
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
