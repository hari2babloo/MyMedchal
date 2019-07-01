package com.androidhari.mymedchal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Classess.DetailsModel;
import Classess.LocationsModel;
import Classess.TinyDB;

public class Search extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    AutoCompleteTextView autoCompleteTextView;
    EditText editText;
    AdapterFish Adapter;

    TinyDB tinyDB;

    String charseq;
    ProgressDialog pd;
    DatabaseReference mdatabase;
    RecyclerView recyclerView;
    ArrayList<DetailsModel> filterdata = new ArrayList<DetailsModel>();
    ArrayList<String> filterdatakey = new ArrayList<>();
    Query query;
    Spinner spinner;
    String spinnerlocation;
    List<String> items = new ArrayList<String>();
    Button buttonname, buttonbusiness;
    Boolean aBoolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
      //  getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(Search.this);
            }
        });
        tinyDB = new TinyDB(this);
        pd = new ProgressDialog(this);

        editText = (EditText) findViewById(R.id.editText);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        aBoolean = true;
        buttonname = (Button) findViewById(R.id.buttonname);
        buttonbusiness = (Button) findViewById(R.id.buttonbusiness);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        buttonname.setBackgroundColor(getResources().getColor(R.color.blue2));
        buttonbusiness.setBackgroundColor(getResources().getColor(R.color.white));
        buttonname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aBoolean = true;

                buttonname.setBackgroundColor(getResources().getColor(R.color.blue2));
                buttonbusiness.setBackgroundColor(getResources().getColor(R.color.white));


                Fetchwithname();
            }
        });

        buttonbusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aBoolean = false;

                buttonbusiness.setBackgroundColor(getResources().getColor(R.color.blue2));
                buttonname.setBackgroundColor(getResources().getColor(R.color.white));

                FetchwithBusiness();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 1) {
                    charseq = s.toString();
                    Log.e("firstchar", s.toString());

                    if (aBoolean) {
                        Fetchwithname();
                    } else if (!aBoolean) {

                        FetchwithBusiness();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        pd.setCancelable(false);
        pd.setMessage("Getting Locations");
        pd.show();

        //       scoresRef.keepSynced(false);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
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


                // getgriddata();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (TextUtils.isEmpty(tinyDB.getString("location")) || tinyDB.getString("location").equalsIgnoreCase("Select a Location")) {


            spinner.performClick();
        } else {

            String location = tinyDB.getString("location");

            if (location != null) {
                int spinnerPosition = adapter.getPosition(location);
                spinner.setSelection(spinnerPosition);
            }
        }


//


//        grid=(GridView).findViewById(R.id.grid);

    }

    private void FetchwithBusiness() {

        if (!TextUtils.isEmpty(charseq) && charseq.length() > 1) {


            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("SubCategory");

            Query query = database.orderByChild("name").startAt(charseq.toString().toUpperCase()).endAt(charseq.toString().toLowerCase() + "\uf8ff").limitToFirst(5);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    filterdata.clear();
                    filterdatakey.clear();
                    Log.e("Values", dataSnapshot.toString());
                    //Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's inside the method.
                    for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                        //Get the suggestion by childing the key of the string you want to get.
                        String suggestion = suggestionSnapshot.child("name").getValue(String.class);
                        DetailsModel detailsModel = suggestionSnapshot.getValue(DetailsModel.class);
                        filterdatakey.add(suggestionSnapshot.getKey());
                        filterdata.add(detailsModel);


                        Log.e("Values", suggestion);
                    }

                    Adapter = new AdapterFish(Search.this, filterdata);
                    Adapter.setHasStableIds(false);
                    recyclerView.setAdapter(Adapter);
                    recyclerView.scrollToPosition(0);
                    recyclerView.setHasFixedSize(false);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Search.this, LinearLayoutManager.VERTICAL, false));
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {

            Toast.makeText(Search.this, "Enter Text Value", Toast.LENGTH_SHORT).show();
        }
    }

    private void Fetchwithname() {

        if (!TextUtils.isEmpty(charseq) && charseq.length() > 1) {


            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("BusinessLists").child("Athvelly");

            Query query = database.orderByChild("name").startAt(charseq.toString().toUpperCase()).endAt(charseq.toString().toLowerCase() + "\uf8ff").limitToFirst(10);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    filterdata.clear();
                    filterdatakey.clear();
                    Log.e("Values", dataSnapshot.toString());
                    //Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's inside the method.
                    for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                        //Get the suggestion by childing the key of the string you want to get.
                        String suggestion = suggestionSnapshot.child("name").getValue(String.class);
                        DetailsModel detailsModel = suggestionSnapshot.getValue(DetailsModel.class);
                        filterdatakey.add(suggestionSnapshot.getKey());
                        filterdata.add(detailsModel);


                        Log.e("Values", suggestion);
                    }

                    Adapter = new AdapterFish(Search.this, filterdata);
                    Adapter.setHasStableIds(false);
                    recyclerView.setAdapter(Adapter);
                    recyclerView.scrollToPosition(0);
                    recyclerView.setHasFixedSize(false);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Search.this, LinearLayoutManager.VERTICAL, false));
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {

            Toast.makeText(Search.this, "Enter Text", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<DetailsModel> data = new ArrayList<>();

        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;

        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(Context context, ArrayList<DetailsModel> data) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.list_layout, parent, false);
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


            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (aBoolean) {

                        Log.e("key", filterdatakey.get(position));
                        tinyDB.putString("key", filterdatakey.get(position));
                        startActivity(new Intent(Search.this, Details.class));
                    } else if (!aBoolean) {
                        Log.e("key", filterdatakey.get(position));
                        tinyDB.putString("name", filterdata.get(position).getName());
                        tinyDB.putString("subcatkey", filterdata.get(position).getName());

                        startActivity(new Intent(Search.this, BusinessLists.class));
                    }


                }
            });

            if (aBoolean) {
                myHolder.name.setText(current.getName());
                myHolder.desc.setText(current.getDescription());
                Glide.with(Search.this).load(current.getImg()).diskCacheStrategy(DiskCacheStrategy.DATA).into(myHolder.imageView);


            } else if (!aBoolean) {
                myHolder.name.setText(current.getName());
                myHolder.desc.setText(current.getDesc());
                Glide.with(Search.this).load(current.getImg()).diskCacheStrategy(DiskCacheStrategy.DATA).into(myHolder.imageView);
            }


        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {
            TextView name, desc;
            ImageView imageView;
            View line;

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name_text);
                desc = itemView.findViewById(R.id.status_text);
                imageView = itemView.findViewById(R.id.profile_image);

            }

        }

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

}

