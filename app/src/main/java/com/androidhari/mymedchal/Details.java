package com.androidhari.mymedchal;

import android.app.TabActivity;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidhari.mymedchal.SupportFiles.OverviewFragment;
import com.androidhari.mymedchal.SupportFiles.PhotosFragment;
import com.androidhari.mymedchal.SupportFiles.QaFragment;
import com.androidhari.mymedchal.SupportFiles.ReviewsFragment;
import com.androidhari.mymedchal.SupportFiles.ViewPagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

import Classess.DetailsModel;
import Classess.Reviewmodels;

public class Details extends AppCompatActivity {

    private boolean isOpen = false ;
    private ConstraintSet layout1,layout2;
    private ConstraintLayout constraintLayout ;
    private ImageView imageViewPhoto;
    CarouselView carouselView;
    NestedScrollView nestedScrollView;
AppBarLayout appBarLayout;
    List<String> images =new ArrayList<>();
    //This is our tablayout
    private TabLayout tabLayout;
    DetailsModel snapshot;
    //This is our viewPager
    private ViewPager viewPager;

    //Fragments


    OverviewFragment overviewFragment;

    ReviewsFragment reviewsFragment;
    PhotosFragment photosFragment;
    QaFragment qaFragment;
    DatabaseReference mDatabase;
    CollapsingToolbarLayout collapsingToolbar;

    int[] sampleImages;
     Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appBarLayout = (AppBarLayout)findViewById(R.id.appbar);
        nestedScrollView = (NestedScrollView)findViewById(R.id.nested);
         collapsingToolbar = findViewById(R.id.collapsing_toolbar);
         collapsingToolbar.setTitleEnabled(false);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("reviews");
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("nameofbusiness").equalTo("GA Mobiles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("data",dataSnapshot.toString());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Reviewmodels ss = ds.getValue(Reviewmodels.class);

//                    stars.setText(ss.getStars());
//                      Log.e("imagelink", ss.getImage());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Details.this, "No Data Found", Toast.LENGTH_SHORT).show();
            }
        });


         sampleImages = new int[]{R.drawable.backpack, R.drawable.barcode, R.drawable.bitcon};



        carouselView = (CarouselView) findViewById(R.id.carouselView);
//        carouselView.setPageCount(sampleImages.length);
//
//
//

//Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position,false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);

    }






        //final ImageView imageView = findViewById(R.id.backdrop);
       // Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).apply(RequestOptions.centerCropTransform()).into(imageView);




    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        reviewsFragment=new ReviewsFragment();
        overviewFragment=new OverviewFragment();
        photosFragment=new PhotosFragment();
        qaFragment = new QaFragment();
        adapter.addFragment(overviewFragment,"Overview");
        adapter.addFragment(reviewsFragment,"Reviews");

        adapter.addFragment(photosFragment,"Photos");
        adapter.addFragment(qaFragment,"Q & A");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.call:


                // do your code
                break;

            case R.id.sms:
                // do your code
                break;

            case R.id.whatsapp:
                // do your code
                break;
            case R.id.qanda:
                // do your code
                break;

            default:
                break;
        }

    }

    public void dispatchInformations(List<String> test) {

        images = test;

        if (carouselView != null) {

            carouselView.setImageListener(imageListener);
            carouselView.setPageCount(images.size());
        }
        Log.e("Received From Fragment", String.valueOf(test));

        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                viewPager.setCurrentItem(2,true);
               appBarLayout.setExpanded(false);
          //  nestedScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });


    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Glide.with(getApplicationContext())
                    .load(images.get(position))
                    .centerCrop()
                    .placeholder(R.drawable.backpack)
                    .into(imageView);
            //      Glide.with(Details.this).load(images.get(position)).diskCacheStrategy(DiskCacheStrategy.DATA).into(imageView);
        }
    };

}
