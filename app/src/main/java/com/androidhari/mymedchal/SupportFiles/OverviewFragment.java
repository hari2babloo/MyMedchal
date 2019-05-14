package com.androidhari.mymedchal.SupportFiles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhari.mymedchal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Classess.DetailsModel;
import Classess.TinyDB;


/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment {

    TextView desc,services,timing,email,website,workingdays,addr,awards,since,cost,contact,propname;
    TinyDB tinyDB;
    public OverviewFragment() {
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
        View v = inflater.inflate(R.layout.fragment_overview, container, false);
        tinyDB = new TinyDB(getContext());
        // Inflate the layout for this fragment
        desc =(TextView)v.findViewById(R.id.desc);
        services = (TextView)v.findViewById(R.id.serv);
        timing = (TextView)v.findViewById(R.id.timing);
        email = (TextView)v.findViewById(R.id.email);
        website = (TextView)v.findViewById(R.id.website);
        workingdays = (TextView)v.findViewById(R.id.workdays);
        addr = (TextView)v.findViewById(R.id.addr);
        awards = (TextView)v.findViewById(R.id.award);
        since = (TextView)v.findViewById(R.id.since);
        cost = (TextView)v.findViewById(R.id.rate);
        contact = (TextView)v.findViewById(R.id.contact);
        propname = (TextView)v.findViewById(R.id.propname);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("BusinessLists").child(tinyDB.getString("location"));
        mDatabase.keepSynced(true);
        mDatabase.orderByKey().equalTo(tinyDB.getString("key")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("data",dataSnapshot.toString());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DetailsModel ss = ds.getValue(DetailsModel.class);
                    Log.e("detailsmodel", ss.email);
                    desc.setText(ss.description);
                    services.setText(ss.services);
                    timing.setText(ss.timingsfrom  + "   " +ss.timingsto);
                    email.setText(ss.email);
                    website.setText(ss.website);
                    workingdays.setText(ss.workindays);
                    addr.setText(ss.address+",\n" +ss.landmark + ",\n" +ss.lane+",\n"+ss.colony);
                    awards.setText(ss.awards);
                    since.setText("Establishment Year "+ ss.since);
                    cost.setText("Cost " +ss.cost);
                    contact.setText(ss.contact +", "+ss.contact2 +", "+ss.contact3);
                    propname.setText("Proprietor name : " +ss.propname);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}
