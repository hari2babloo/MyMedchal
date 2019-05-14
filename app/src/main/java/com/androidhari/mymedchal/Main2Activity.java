package com.androidhari.mymedchal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhari.mymedchal.SupportFiles.GridAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.luseen.spacenavigation.SpaceOnLongClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Classess.CategoryModel;
import Classess.LocationsModel;
import Classess.Signup;
import Classess.TinyDB;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<String> items = new ArrayList<String>();
    List<String> categories = new ArrayList<String>();
    List<String> categoriesimg = new ArrayList<String>();

    FirebaseAuth mAuth;
    DatabaseReference ddd;
    DatabaseReference rootRef;

    Spinner spinner;
    String spinnerlocation, datasnapshot;
    ProgressDialog pd;

    GridView grid;

    TextView navusername,navmobno;
    ImageView profimage;
    TinyDB tinyDB;

    SpaceNavigationView spaceNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        enablePersistence();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        mAuth = FirebaseAuth.getInstance();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hview = navigationView.getHeaderView(0);
        navusername = (TextView)hview.findViewById(R.id.headname);
        navmobno = (TextView)hview.findViewById(R.id.headname2);
        profimage = (ImageView)hview.findViewById(R.id.imageView);


        Toast.makeText(getApplicationContext(), "Long press center button to show badge example", Toast.LENGTH_LONG).show();
        spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);

        spaceNavigationView.addSpaceItem(new SpaceItem("SEARCH", R.drawable.ic_placeholder));
        spaceNavigationView.addSpaceItem(new SpaceItem("HOME", R.drawable.ic_favorite_heart_button));
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
        grid = (GridView) findViewById(R.id.grid);
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



        rootRef.keepSynced(true);
        //  DatabaseReference rootRef2 = FirebaseDatabase.getInstance().getReference();


    }

    private void getgriddata() {
        ddd = FirebaseDatabase.getInstance().getReference().child("Category");
        ddd.keepSynced(true);
        categories.clear();
        ddd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Log.e("Count ", "" + dataSnapshot.toString());
                datasnapshot = dataSnapshot.toString();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    //  String eventID= dataSnapshot.child("Hospitals").getKey();
                    String eventID = ds.getKey();
                    CategoryModel post = ds.getValue(CategoryModel.class);
                    Log.e("Get Data", ds.getValue().toString());

                    categories.add(post.getName());
                    categoriesimg.add(post.getImg());
                    Log.d("TAG", eventID);


                }


                Log.d("ARAY", String.valueOf(categoriesimg));
                Log.e("received", String.valueOf(categories));

                bindgriddata();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


//        categories.add("hedfaf");

    }

    private void bindgriddata() {
        GridAdapter adapter2 = new GridAdapter(this, categories, categoriesimg);
        grid.setAdapter(adapter2);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

//                Toast.makeText(Main2Activity.this, categories.get(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Main2Activity.this, SubCategory.class);
                tinyDB.putString("Category", categories.get(position));
                startActivity(intent);
                // startActivity(new Intent(Main2Activity.this,SubCategory.class));
                //Toast.makeText(this, "You Clicked at " +categories.get(position), Toast.LENGTH_SHORT).show();

            }
        });


        navusername.setText(tinyDB.getString("uname"));
        Glide.with(Main2Activity.this).load(tinyDB.getString("uimage")).diskCacheStrategy(DiskCacheStrategy.DATA).into(profimage);
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

//


//        grid=(GridView).findViewById(R.id.grid);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        if (id == R.id.profile) {
            // Handle the camera action
        } else if (id == R.id.favs) {

            startActivity(new Intent(Main2Activity.this,FavList.class));

        }
        else if (id==R.id.add){

            startActivity(new Intent(Main2Activity.this,Request.class));
        }

        else if (id == R.id.rate) {

        } else if (id == R.id.contact) {

        } else if (id == R.id.log_out) {
            mAuth.signOut();
            startActivity(new Intent(Main2Activity.this,Intro.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
