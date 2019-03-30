package com.androidhari.mymedchal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.androidhari.mymedchal.SupportFiles.GridAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Classess.CategoryModel;
import Classess.Signup;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<String> items = new ArrayList<String>();
    List<String> categories = new ArrayList<String>();
    FirebaseAuth mAuth;
    DatabaseReference ddd;
    DatabaseReference rootRef;
    FirebaseAuth.AuthStateListener mAuthListener;
    Spinner spinner;
    String spinnerlocation;
    ProgressDialog pd;
    GridView grid;
    Integer[] imageId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);




        grid = (GridView)findViewById(R.id.grid);
        imageId = new Integer[]{R.drawable.common_full_open_on_phone,R.drawable.common_full_open_on_phone,R.drawable.common_full_open_on_phone,R.drawable.common_full_open_on_phone,R.drawable.common_full_open_on_phone,R.drawable.common_full_open_on_phone,R.drawable.common_full_open_on_phone,R.drawable.common_full_open_on_phone,R.drawable.common_full_open_on_phone, R.drawable.common_full_open_on_phone,R.drawable.common_full_open_on_phone};
        pd=new ProgressDialog(this);




     //   items.add("Hwllo");




        rootRef = FirebaseDatabase.getInstance().getReference();
        //     DatabaseReference eventIdRef = rootRef.child("locations");

        pd.setCancelable(false);
        pd.setMessage("Getting Locations");
        pd.show();

        //       scoresRef.keepSynced(false);

        DatabaseReference sss = rootRef.child("Locations");

        sss.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("keys",dataSnapshot.toString());

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String eventID = ds.getKey();
                    items.add(eventID);
                    Log.d("TAG2", eventID);
                }
                Log.d("TAG2", String.valueOf(items));

                //  rootRef.keepSynced(true);

                bindspinnerData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        enablePersistence();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        rootRef.keepSynced(true);




        //  DatabaseReference rootRef2 = FirebaseDatabase.getInstance().getReference();





    }

    private void getgriddata() {

        ddd = FirebaseDatabase.getInstance().getReference().child("Locations").child(spinnerlocation);



        ddd.keepSynced(true);


        //   keepSynced();





        categories.clear();

        ddd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.e("Count " ,""+dataSnapshot
                        .getChildrenCount());
                for(DataSnapshot ds : dataSnapshot.getChildren()) {



                    //  String eventID= dataSnapshot.child("Hospitals").getKey();
                    String eventID = ds.getKey();
                    CategoryModel post = ds.getValue(CategoryModel.class);
                    Log.e("Get Data", post.getName());
                    categories.add(eventID);
                    Log.d("TAG", eventID);


                }
                Log.d("ARAY", String.valueOf(categories));
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
        GridAdapter adapter2 = new GridAdapter(this, categories, imageId);
        grid.setAdapter(adapter2);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Toast.makeText(Main2Activity.this, categories.get(position), Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(Main2Activity.this,SubCategory.class));
                //Toast.makeText(this, "You Clicked at " +categories.get(position), Toast.LENGTH_SHORT).show();

            }
        });
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


        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new  ArrayAdapter<String>(this,
                R.layout.spinner_item,items);
        adapter.setDropDownViewResource(R.layout.appbar_spinner_dropdown);




        spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("Spinner",items.get(position));
                    spinnerlocation = items.get(position);
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
        }
        else if (id==R.id.spinner){

          //a   spinner = (Spinner) MenuItemCompat.getActionView(item);



        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
