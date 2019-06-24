package com.androidhari.mymedchal;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Classess.LocationsModel;
import Classess.TinyDB;

public class ManageLists extends AppCompatActivity {

    Spinner spinner;
    String spinnerlocation, datasnapshot;
    List<String> items = new ArrayList<String>();

    ProgressDialog pd;
    TinyDB tinyDB;
    DatabaseReference rootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_lists);
        pd = new ProgressDialog(this);
        tinyDB = new TinyDB(this);
        spinner = (Spinner)findViewById(R.id.spinner);

        rootRef = FirebaseDatabase.getInstance().getReference();
        pd.setCancelable(false);
        pd.setMessage("Getting Locations");
        pd.show();


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
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);

                Log.e("Spinner", items.get(position));
                spinnerlocation = items.get(position);
                tinyDB.putString("location", spinnerlocation);

//                getgriddata();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//


//        grid=(GridView).findViewById(R.id.grid);

    }
}
