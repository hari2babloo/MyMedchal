package com.androidhari.mymedchal;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Classess.FeedModel;

public class Collapsablelayout extends AppCompatActivity {


    DatabaseReference databaseReference;

    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collapsablelayout);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("offers").child("feeds");
        FeedModel feedModel  =new FeedModel();
        feedModel.setTitle("Medchal News");
        feedModel.setDesc("Description");
        feedModel.setTimestamp("Time");
        feedModel.setImage("https://www.androidhive.info/wp-content/uploads/2016/05/android-welcome-intro-slider-with-bottom-dots.png");
        feedModel.setImaagearray("Yes");
        feedModel.setContributer("JIO");
        feedModel.setTopic("Topic");
        databaseReference.push().setValue(feedModel);

//        databaseReference = FirebaseDatabase.getInstance().getReference().child("offers").child("feeds").child("images");
//        FeedModel feedModel  =new FeedModel();
//        feedModel.setKey("-LisGromqfEBDjcAxwV5");
//        feedModel.setImgdesc("Description");
//        feedModel.setUrl("https://image.flaticon.com/sprites/new_packs/148705-essential-collection.png");
//        databaseReference.push().setValue(feedModel);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("datasnapshop",dataSnapshot.toString());
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        key = ds.getKey();
                        FeedModel feedModel = ds.getValue(FeedModel.class);
//                       Log.e("feedmodel", String.valueOf(ds));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
