package com.androidhari.mymedchal;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhari.mymedchal.SupportFiles.OverviewFragment;
import com.androidhari.mymedchal.SupportFiles.PhotosFragment;
import com.androidhari.mymedchal.SupportFiles.QaFragment;
import com.androidhari.mymedchal.SupportFiles.ReviewsFragment;
import com.androidhari.mymedchal.SupportFiles.ViewPagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Classess.DetailsModel;
import Classess.QAmodels;
import Classess.Reviewmodels;
import Classess.TinyDB;

public class Details extends AppCompatActivity {

    private boolean isOpen = false;
    private ConstraintSet layout1, layout2;
    private ConstraintLayout constraintLayout;
    private ImageView imageViewPhoto;
    double totalratings, averagerating;

    String contacttxt, whatsapptxt, emailtxt, smstxt;

    CarouselView carouselView;
    NestedScrollView nestedScrollView;
    AppBarLayout appBarLayout;
    double lat,lng;
    List<String> images = new ArrayList<>();
    //This is our tablayout

    private Animator currentAnimator;

    private int shortAnimationDuration;
    private TabLayout tabLayout;
    DetailsModel  detailsModel;
    //This is our viewPager
    private ViewPager viewPager;

    String selected = "no";
    //Fragments


    OverviewFragment overviewFragment;
    ImageView logoimg;
    ReviewsFragment reviewsFragment;
    PhotosFragment photosFragment;
    QaFragment qaFragment;
    DatabaseReference mDatabase;
    DatabaseReference mDatabase2;
    TextView five, four, three, two, one, average, total, chip1, chip2;
    double count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0;
    CollapsingToolbarLayout collapsingToolbar;
    TinyDB tinyDB;
    FloatingActionButton fab;
    ArrayList<DetailsModel> arrayList= new ArrayList<DetailsModel>();
    ArrayList<DetailsModel> arrayList2 = new ArrayList<>();
    ArrayList<DetailsModel> lstArrayList = new ArrayList<DetailsModel>();;
    TextView busname, addr;

    int[] sampleImages;
    Toolbar toolbar;
    Button call,whatsapp,sms,ask,email,favbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        tinyDB = new TinyDB(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        logoimg = (ImageView)findViewById(R.id.imageView2);
        call = (Button) findViewById(R.id.call);
        whatsapp = (Button)findViewById(R.id.whatsapp);
        sms = (Button)findViewById(R.id.sms);
        ask  = (Button)findViewById(R.id.qanda);
        favbtn =(Button)findViewById(R.id.fav);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        five = (TextView) findViewById(R.id.fivestar);
        four = (TextView) findViewById(R.id.fourstar);
        three = (TextView) findViewById(R.id.threestar);
        two = (TextView) findViewById(R.id.twostar);
        one = (TextView) findViewById(R.id.onestar);
        average = (TextView) findViewById(R.id.average);
        total = (TextView) findViewById(R.id.total);
        chip1 = (TextView) findViewById(R.id.chip1);
        chip2 = (TextView) findViewById(R.id.chip2);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nested);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(false);
        busname = (TextView) findViewById(R.id.busname);
        addr = (TextView) findViewById(R.id.addr);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("reviews");
        mDatabase.keepSynced(true);

        RatingBindData();
        UIbindData();
 //       parsedata();

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
                viewPager.setCurrentItem(position, false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);

    }


    private void RatingBindData() {
        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("reviews");
        mDatabase2.orderByChild("key").equalTo(tinyDB.getString("key")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.e("data2", dataSnapshot.getValue().toString());

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Reviewmodels reviewmodels = ds.getValue(Reviewmodels.class);

                    Log.e("stars", String.valueOf(reviewmodels.stars));

                    if (reviewmodels.stars.equalsIgnoreCase("5")) {
                        count5 = count5 + 1;
                        if (count5 == 1) {
                            five.setText(String.valueOf(count5));

                        } else {

                            five.setText(String.valueOf(count5));
                        }


                    } else if (reviewmodels.stars.equalsIgnoreCase("4")) {
                        count4 = count4 + 1;
                        if (count4 == 1) {
                            four.setText(String.valueOf(count4));

                        } else {

                            four.setText(String.valueOf(count4));
                        }
                    } else if (reviewmodels.stars.equalsIgnoreCase("3")) {
                        count3 = count3 + 1;
                        if (count3 == 1) {

                            three.setText(String.valueOf(count3));
                        } else {
                            three.setText(String.valueOf(count3));
                        }
                    } else if (reviewmodels.stars.equalsIgnoreCase("2")) {
                        count2 = count2 + 1;
                        if (count2 == 1) {
                            two.setText(String.valueOf(count2));
                        } else {
                            two.setText(String.valueOf(count2));
                        }

                    } else if (reviewmodels.stars.equalsIgnoreCase("1")) {

                        count1 = count1 + 1;
                        if (count1 == 1) {
                            one.setText(String.valueOf(count1));
                        } else {
                            one.setText(String.valueOf(count1));
                        }
                    }

                    totalratings = count1 + count2 + count3 + count4 + count5;
                    total.setText(String.format("%.0f", totalratings));

                    averagerating = ((count1 * 1) + (count2 * 2) + (count3 * 3) + (count4 * 4) + (count5 * 5)) / totalratings;


                    Log.e("Average", String.valueOf(averagerating));
                    Log.e("count", String.valueOf(count3));



                    if (Double.isNaN(averagerating)){

                        average.setText("0");
                    }else {

                        average.setText(String.format("%.1f", averagerating));
                    }


                    Log.e("Reviews", String.valueOf(ds.getChildrenCount()));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void UIbindData() {

        Log.e("Key",tinyDB.getString("key"));
        Log.e("location",tinyDB.getString("location"));
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("BusinessLists").child(tinyDB.getString("location"));
        mDatabase2.orderByKey().equalTo(tinyDB.getString("key")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("UI Bind", dataSnapshot.toString());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    detailsModel = ds.getValue(DetailsModel.class);
                    detailsModel.setKey(ds.getKey());


                    busname.setText(detailsModel.name);
                    addr.setText(detailsModel.address);
                    chip1.setText(detailsModel.category);
                    chip2.setText(detailsModel.subcategory);
                    Glide.with(Details.this).load(detailsModel.img).apply(RequestOptions.centerCropTransform()).into(logoimg);
                    contacttxt = detailsModel.contact;
                    whatsapptxt = detailsModel.whatsapp;
                    emailtxt = detailsModel.email;
                       lat = Double.valueOf(detailsModel.lat);
                     lng = Double.valueOf(detailsModel.lng);
                    Log.e("Name of Busines", detailsModel.getName());
                }




                parsedata();

                logoimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shortAnimationDuration = getResources().getInteger(
                                android.R.integer.config_shortAnimTime);
                        final Dialog fbDialogue = new Dialog(Details.this, android.R.style.Theme_Black_NoTitleBar);

                        fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
                        fbDialogue.setContentView(R.layout.image_fullscreen);

                        if (currentAnimator != null) {
                            currentAnimator.cancel();
                        }



                        final ImageView expandedImageView = (ImageView)fbDialogue.findViewById(R.id.expanded_image);
                        Glide.with(Details.this).load(detailsModel.img).diskCacheStrategy(DiskCacheStrategy.DATA).into(expandedImageView);
                        fbDialogue.setCancelable(true);
                        fbDialogue.show();
                    }
                });
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_DIAL);
                        String p = "tel:" + contacttxt;
                        i.setData(Uri.parse(p));
                             startActivity(i);
                    }
                });

                whatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri mUri = Uri.parse("https://api.whatsapp.com/send?phone=+91"+whatsapptxt+"&text='Hey there, I found your business on MyMedchal'");
                        Intent intent = new Intent("android.intent.action.VIEW", mUri);
                        intent.setPackage("com.whatsapp");
                        startActivity(intent);
                    }
                });

                sms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contacttxt));
                        intent.putExtra("sms_body", "Hey there, I found your Business on MyMedchal");
                        startActivity(intent);
                    }
                });

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + busname.getText() + ")";
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                        startActivity(intent);
                    }
                });

                favbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        favbtnfunction();










                    }
                });

                ask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(Details.this,R.style.DialoTheme);

                        dialog.setContentView(R.layout.addqa);
                        dialog.setTitle("Add Question...");

                        final EditText editText = (EditText)dialog.findViewById(R.id.editText);

                        final Button submit = (Button)dialog.findViewById(R.id.submit);

                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (TextUtils.isEmpty(editText.getText())){
                                    View parentLayout = dialog.findViewById(R.id.parent);
                                    Snackbar.make(parentLayout, "please add your question", Snackbar.LENGTH_LONG)
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

                                    DatabaseReference myRef = database.getReference("Q&A");
                                    QAmodels category = new QAmodels();
                                    category.setAns("none");
                                    category.setTimestanp(System.currentTimeMillis());
                                    category.setUserid(tinyDB.getString("uid"));
                                    category.setUsername(tinyDB.getString("uname"));
                                    category.setProfilepic(tinyDB.getString("uimage"));
                                    category.setNameofbusiness(tinyDB.getString("key"));
                                    category.setLocation(tinyDB.getString("location"));
                                    category.setKey(tinyDB.getString("key"));
                                    category.setQuestion(editText.getText().toString());
                                    myRef.push().setValue(category);
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

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Details.this, "No Data Found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void favbtnfunction() {


        if (selected.equalsIgnoreCase("yes")){

            for (int i = 0; i < lstArrayList.size(); i++) {

                if (detailsModel.getName().equalsIgnoreCase(lstArrayList.get(i).getName())) {

                    lstArrayList.remove(i);
                    int img = R.drawable.ic_heart_open;

                    String key = "fav";

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(Details.this);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Gson gson = new Gson();

                    String json = gson.toJson(lstArrayList);

                    editor.remove(key).commit();
                    editor.putString("fav", json);
                    editor.commit();

                    favbtn.setCompoundDrawablesWithIntrinsicBounds(0, img, 0, 0);
                    favbtn.setText("Favourite");

                    selected = "no";
//                                    Log.e("found",lstArrayList.get(i).getName());
                }

            }
        }

        else if (selected.equalsIgnoreCase("no")){



            lstArrayList.add(detailsModel);
            String key = "fav";

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(Details.this);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            Gson gson = new Gson();

            String json = gson.toJson(lstArrayList);

            editor.remove(key).commit();
            editor.putString("fav", json);
            editor.commit();

            int img = R.drawable.ic_favorite_heart_button;

            favbtn.setCompoundDrawablesWithIntrinsicBounds(0,img,0,0);
            favbtn.setText("Added");

            selected = "yes";
        }
    }

    private void parsedata() {


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(Details.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("fav", null);



         if (json==null){


         }else {
             Type type = new TypeToken<List<DetailsModel>>() {}.getType();
             lstArrayList = gson.fromJson(json, type);
             if(lstArrayList!=null && !lstArrayList.isEmpty() ){



                 for (int i = 0; i < lstArrayList.size(); i++) {
                     String ss = lstArrayList.get(i).name;

                     if (ss.equalsIgnoreCase(detailsModel.getName())){

                         int img = R.drawable.ic_favorite_heart_button;

                         favbtn.setCompoundDrawablesWithIntrinsicBounds(0,img,0,0);
                         favbtn.setText("Added");
                         selected = "yes";
                     }
                      Log.e("fav Values",ss);

                 }
             }
         }


       // UIbindData();
       //  lstArrayList = gson.fromJson(json,new TypeToken<List<DetailsModel>>(){}.getType());


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


    public void dispatchInformations(List<String> test) {

        images = test;

        if (carouselView != null) {

            int imgsize;

            if (images.size()>4){

                imgsize=4;
            }
            else {

                imgsize= images.size();
            }

            carouselView.setImageListener(imageListener);
            carouselView.setPageCount(imgsize);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //handle the home button onClick event here.
                startActivity(new Intent(Details.this,BusinessLists.class));
                return true;

            case  R.id.homee :

                startActivity(new Intent(Details.this,Main2Activity.class));


        }

        return super.onOptionsItemSelected(item);
    }
}

