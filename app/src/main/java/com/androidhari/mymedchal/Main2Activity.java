package com.androidhari.mymedchal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.luseen.spacenavigation.SpaceOnLongClickListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Classess.CategoryModel;
import Classess.FeedModel;
import Classess.LocationsModel;
import Classess.Signup;
import Classess.TinyDB;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<String> items = new ArrayList<String>();
    List<String> categories = new ArrayList<String>();
    List<String> categoriesimg = new ArrayList<String>();
    ArrayList<CategoryModel> categoryModels = new ArrayList<>();

    FirebaseAuth mAuth;
    private AdapterFish Adapter;
    DatabaseReference ddd;
    DatabaseReference rootRef;

    Spinner spinner;
    String spinnerlocation, datasnapshot;
    ProgressDialog pd;

    RecyclerView  recyclerView;
    List<String> filterdata= new ArrayList<String>();

    GridView grid;

    TextView navusername,navmobno;
    ImageView profimage;
    TinyDB tinyDB;
    CollapsingToolbarLayout collapsingToolbar;
    private AdView  mAdView,mAdView2;
    CarouselView carouselView;

    SpaceNavigationView spaceNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitleEnabled(false);
        enablePersistence();


        MobileAds.initialize(Main2Activity.this,"ca-app-pub-3574852791589889~6439948019");


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mAuth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView)findViewById(R.id.grid);
        carouselView = (CarouselView) findViewById(R.id.carouselView);

        carousalsetup();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hview = navigationView.getHeaderView(0);
        navusername = (TextView)hview.findViewById(R.id.headname);
        navmobno = (TextView)hview.findViewById(R.id.headname2);
        profimage = (ImageView)hview.findViewById(R.id.imageView);

        mAdView2 =  hview.findViewById(R.id.adView2);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest2);

       // Toast.makeText(getApplicationContext(), "Long press center button to show badge example", Toast.LENGTH_LONG).show();
        spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("Feeds", R.drawable.ic_newspaper));

        spaceNavigationView.addSpaceItem(new SpaceItem("HOME", R.drawable.ic_favorite_heart_button));
        spaceNavigationView.addSpaceItem(new SpaceItem("add",R.drawable.ic_add_button_inside_black_circle));
        spaceNavigationView.addSpaceItem(new SpaceItem("manage",R.drawable.ic_meeting));

        spaceNavigationView.shouldShowFullBadgeText(true);
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
        spaceNavigationView.showIconOnly();

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Log.d("onCentreButtonClick ", "onCentreButtonClick");
                spaceNavigationView.shouldShowFullBadgeText(true);
                startActivity(new Intent(Main2Activity.this,Search.class));

            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                Log.d("onItemClick ", "" + itemIndex + " " + itemName);

                if (itemName.equalsIgnoreCase("HOME")){

                    startActivity(new Intent(Main2Activity.this,FavList.class));

                }
                else
                if (itemName.equalsIgnoreCase("add")){

                    startActivity(new Intent(Main2Activity.this,Request.class));

                }
                else
                if (itemName.equalsIgnoreCase("manage")){

                    startActivity(new Intent(Main2Activity.this,ManageLists.class));

                }
                else
                if (itemName.equalsIgnoreCase("Feeds")){

                    startActivity(new Intent(Main2Activity.this,Feed.class));

                }

            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Log.d("onItemReselected ", "" + itemIndex + " " + itemName);
                if (itemName.equalsIgnoreCase("HOME")){

                    startActivity(new Intent(Main2Activity.this,FavList.class));

                }
            }
        });

        spaceNavigationView.setSpaceOnLongClickListener(new SpaceOnLongClickListener() {
            @Override
            public void onCentreButtonLongClick() {
//                Toast.makeText(MainActivity.this, "onCentreButtonLongClick", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(int itemIndex, String itemName) {
                Toast.makeText(Main2Activity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });
        tinyDB = new TinyDB(this);
//        grid = (GridView) findViewById(R.id.grid);
//        items.add("Athvelly");
        pd = new ProgressDialog(this);
        rootRef = FirebaseDatabase.getInstance().getReference();

        Log.e("username",tinyDB.getString("uid"));
        Log.e("username",tinyDB.getString("username"));

        if (TextUtils.isEmpty(tinyDB.getString("uname"))){
        DatabaseReference sss = rootRef.child("users");
        Log.e("username","came inside");

        sss.orderByKey().equalTo(tinyDB.getString("uid")).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.e("keys", dataSnapshot.toString());
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                String eventID = ds.getKey();
                Signup signup = ds.getValue(Signup.class);
                //items.add(locationsModel.name);
                tinyDB.putString("uname",signup.username);
                tinyDB.putString("uimage",signup.imgurl);
                tinyDB.putString("ugender",signup.gender);
                tinyDB.putString("uphone",signup.phone);
                Log.d("userdetails", signup.username);


            }
            Log.d("TAG2", String.valueOf(items));
            //  rootRef.keepSynced(true);
//            bindspinnerData();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }


    });

}

    pd.setCancelable(false);
    pd.setMessage("Getting Locations");
    pd.show();

    //       scoresRef.keepSynced(false);

    DatabaseReference sss = rootRef.child("locations");

    sss.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.e("keys", dataSnapshot.toString());

            items.add("Select a Location");

            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                String eventID = ds.getKey();
                LocationsModel locationsModel = ds.getValue(LocationsModel.class);
                items.add(locationsModel.name);
                Log.d("TAG2", locationsModel.name);
            }
            Log.d("TAG2", String.valueOf(items));
            //  rootRef.keepSynced(true);
            bindspinnerData();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }


    });



        //  DatabaseReference rootRef2 = FirebaseDatabase.getInstance().getReference();


    }

    private void carousalsetup() {


        final DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference().child("offers").child("feeds");


       databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists()){
                   Log.e("photosdata",dataSnapshot.getKey());
                   //              Photosmodel ss = dataSnapshot.getValue(Photosmodel.class);
//                Log.e("reviews", ss.getImgurl());

                   for (DataSnapshot ds : dataSnapshot.getChildren()) {


                       Log.e("PhotoKeys",ds.getKey());


                       FeedModel ss2 = ds.getValue(FeedModel.class);

                       filterdata.add(ss2.getImage());


//                    stars.setText(ss.getStars());
//                       Log.e("photosname", ss2.getImage());

                   }

                   dispatchInformations(filterdata);
               }

               else {

                   carouselView.setVisibility(View.GONE);
               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
//        FeedModel feedModel  =new FeedModel();
//        feedModel.setTitle("Medchal News");
//        feedModel.setDesc("Description");
//        feedModel.setTimestamp("Time");
//        feedModel.setImage("https://www.androidhive.info/wp-content/uploads/2016/05/android-welcome-intro-slider-with-bottom-dots.png");
//        feedModel.setImaagearray("Yes");
//        feedModel.setContributer("JIO");
//        feedModel.setTopic("Topic");
//        databaseReference.push().setValue(feedModel);
    }

    public void dispatchInformations(List<String> test) {

        //images = test;

        if (carouselView != null) {

            int imgsize;

            if (filterdata.size()>4){

                imgsize=4;
            }
            else {

                imgsize= filterdata.size();
            }

            carouselView.setImageListener(imageListener);
            carouselView.setPageCount(imgsize);
        }
        Log.e("Received From Fragment", String.valueOf(test));

        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {

            startActivity(new Intent(Main2Activity.this,Feed.class));
//                viewPager.setCurrentItem(2,true);
//                appBarLayout.setExpanded(false);
                //  nestedScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });


    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Glide.with(getApplicationContext())
                    .load(filterdata.get(position))
                    .centerCrop()
                    .placeholder(R.drawable.backpack)
                    .into(imageView);
            //      Glide.with(Details.this).load(images.get(position)).diskCacheStrategy(DiskCacheStrategy.DATA).into(imageView);
        }
    };

    private void getgriddata() {
        ddd = FirebaseDatabase.getInstance().getReference().child("Category");
        ddd.keepSynced(true);
        categories.clear();
        ddd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Count ", "" + dataSnapshot.toString());
                datasnapshot = dataSnapshot.toString();
                categories.clear();
                categoriesimg.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //  String eventID= dataSnapshot.child("Hospitals").getKey();
                    String eventID = ds.getKey();
                    CategoryModel post = ds.getValue(CategoryModel.class);


                    categoryModels.add(post);
                    Log.e("Get Data", ds.getValue().toString());
                    categories.add(post.getName());
                    categoriesimg.add(post.getImg());
                    Log.d("TAG", eventID);
                }
                Log.d("ARAY", String.valueOf(categoriesimg));
                Log.e("received", String.valueOf(categories));


                Adapter = new AdapterFish(Main2Activity.this, categoryModels);
                Adapter.setHasStableIds(false);
                recyclerView.setAdapter(Adapter);

                //mRVFishPrice.getLayoutManager().scrollToPosition(0);
                recyclerView.setHasFixedSize(false);

                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Main2Activity.this, 4);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setNestedScrollingEnabled(false);
//                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                        ((GridLayoutManager) mLayoutManager).getOrientation());
//                recyclerView.addItemDecoration(dividerItemDecoration);
                recyclerView.addItemDecoration(new DividerItemDecoration(Main2Activity.this,
                        DividerItemDecoration.HORIZONTAL));
                recyclerView.addItemDecoration(new DividerItemDecoration(Main2Activity.this,
                        DividerItemDecoration.VERTICAL));

//                recyclerView.setLayoutManager(new LinearLayoutManager(Main2Activity.this,LinearLayoutManager.VERTICAL,false));
                bindgriddata();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
//        categories.add("hedfaf");

    }

    private void bindgriddata() {


//
//        GridAdapter adapter2 = new GridAdapter(this, categories, categoriesimg);
//        grid.setAdapter(adapter2);
//
//        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
////                Toast.makeText(Main2Activity.this, categories.get(position), Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(Main2Activity.this, SubCategory.class);
//                tinyDB.putString("Category", categories.get(position));
//                startActivity(intent);
//                // startActivity(new Intent(Main2Activity.this,SubCategory.class));
//                //Toast.makeText(this, "You Clicked at " +categories.get(position), Toast.LENGTH_SHORT).show();
//
//            }
//        });





        navusername.setText(tinyDB.getString("uname"));

//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storage = FirebaseStorage.getInstance("gs://mymedchal-e4910.appspot.com/userimages").getReference();
//
        StorageReference storageReference  = FirebaseStorage.getInstance().getReference().child("userimages");
        //StorageReference pathReference = storageRef.child("images/stars.jpg");


        storageReference.child("FdZiqdT5yGPM4FGeLzTVMkR5rQf1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
//                Glide.with(Main2Activity.this).load(tinyDB.getString("uimage")).diskCacheStrategy(DiskCacheStrategy.DATA).into(profimage);
                Glide.with(Main2Activity.this).load(uri).diskCacheStrategy(DiskCacheStrategy.DATA).into(profimage);
                // Got the download URL for 'users/me/profile.png'
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        navmobno.setText(tinyDB.getString("uphone"));
    }


    private void enablePersistence() {
        // [START rtdb_enable_persistence]
        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // [END rtdb_enable_persistence]
    }


    @Override
    protected void onStart() {
        super.onStart();
        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }


    private void bindspinnerData() {

        pd.dismiss();


        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, items);
        adapter.setDropDownViewResource(R.layout.appbar_spinner_dropdown);


        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Spinner", items.get(position));
                spinnerlocation = items.get(position);
                tinyDB.putString("location", spinnerlocation);



                getgriddata();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
if (TextUtils.isEmpty(tinyDB.getString("location"))||tinyDB.getString("location").equalsIgnoreCase("Select a Location")){



    spinner.performClick();
}

else {

        String location =     tinyDB.getString("location");

        if (location != null) {
            int spinnerPosition = adapter.getPosition(location);
            spinner.setSelection(spinnerPosition);
        }
    }






//


//        grid=(GridView).findViewById(R.id.grid);

    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }


        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("Quit MyMedchal?")
                .setMessage("Do you want to close MyMedchal App?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.actionbar_spinner, menu);
        //      MenuItem item = menu.findItem(R.id.spinner);
//         spinner = (Spinner) MenuItemCompat.getActionView(item);


        //bindspinnerData();

        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.spinner) {

            //a   spinner = (Spinner) MenuItemCompat.getActionView(item);


        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


         if (id == R.id.favs) {

            startActivity(new Intent(Main2Activity.this,FavList.class));

        }

        else if (id==R.id.contact){

             Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "8125110147"));
             intent.putExtra("sms_body", "Hey there, I Like Your App Mymedchal");
             startActivity(intent);



        }
        else if (id==R.id.add){

            startActivity(new Intent(Main2Activity.this,Request.class));
        }
        else if (id == R.id.rate) {

        } else if (id == R.id.contact) {

        } else if (id == R.id.manage){
            startActivity(new Intent(Main2Activity.this,ManageLists.class));

        }

        else if (id == R.id.log_out) {
            mAuth.signOut();
            startActivity(new Intent(Main2Activity.this,Intro.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }



    class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<CategoryModel> data = Collections.emptyList();
        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;
        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(Context context, List<CategoryModel> data) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.gridviewitem, parent, false);
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
            final CategoryModel current = data.get(position);


            myHolder.text.setText(current.getName());
            Glide.with(Main2Activity.this).load(current.img).apply(RequestOptions.centerCropTransform()).into(myHolder.img);




            Log.e("recycleritems",data.get(position).getName());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                Intent intent = new Intent(Main2Activity.this, SubCategory.class);
                tinyDB.putString("Category", categories.get(position));
                startActivity(intent);
//                        tinyDB.putString("location",current.getLocation());
//                        tinyDB.putString("key",current.getKey());
//                        Log.e("Key",tinyDB.getString("key"));
//                        Log.e("location",tinyDB.getString("location"));

//                        startActivity(new Intent(this,Seller_Dashpage.class));
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
            TextView text;
            ImageView img;
            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);



                text = itemView.findViewById(R.id.text);

                img = itemView.findViewById(R.id.imageView);

//                view = itemView.findViewById(R.id.view);
//                edit = itemView.findViewById(R.id.edit);




            }

        }

    }



}
