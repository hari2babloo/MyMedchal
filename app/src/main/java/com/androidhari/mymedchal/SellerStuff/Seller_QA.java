package com.androidhari.mymedchal.SellerStuff;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhari.mymedchal.R;
import com.androidhari.mymedchal.SupportFiles.QaFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import Classess.QAmodels;
import Classess.TinyDB;

public class Seller_QA extends AppCompatActivity {

    RecyclerView mRecycleView;
    private FirebaseRecyclerAdapter<QAmodels, NewsViewHolder> mPeopleRVAdapter;
    DatabaseReference mDatabase;
    TextView addqatxt;
    ProgressDialog pd;
    TinyDB tinyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller__q);
        tinyDB = new TinyDB(this);
        pd = new  ProgressDialog(this);
        mRecycleView = (RecyclerView)findViewById(R.id.recyclerview);
        addqatxt= (TextView)findViewById(R.id.addqa);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Q&A");
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("key").equalTo(tinyDB.getString("key")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("data",dataSnapshot.toString());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    QAmodels ss = ds.getValue(QAmodels.class);

//                    stars.setText(ss.getStars());
                    Log.e("reviews", ss.getAns());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Seller_QA.this, "No Data Found", Toast.LENGTH_SHORT).show();
            }
        });
        addqatxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Seller_QA.this,R.style.DialoTheme);

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




        Query query = mDatabase.orderByChild("key").equalTo(tinyDB.getString("key")).limitToLast(100);
        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<QAmodels>().setQuery(query,  QAmodels.class).build();
        mPeopleRVAdapter = new FirebaseRecyclerAdapter<QAmodels, NewsViewHolder>(personsOptions) {



            @Override
            protected void onBindViewHolder(NewsViewHolder holder, final int position, final QAmodels model) {



                holder.question.setText("Question :  " +model.getQuestion());
                holder.username.setText( "Answer :  "+model.getUsername());
                holder.answer.setText(model.getAns());

                Glide.with(Seller_QA.this).load(model.profilepic).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.usrimg);

                holder.reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String key = String.valueOf(getSnapshots().getSnapshot(position).getKey());

                        Log.e("Check",key);
                        final Dialog dialog = new Dialog(Seller_QA.this,R.style.DialoTheme);

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

                                    DatabaseReference myRef = database.getReference("Q&A").child(key);
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
                Log.e("result",model.getAns());

            }

            @Override
            public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.seller_reply, parent, false);

                return new NewsViewHolder(view);
            }
        };


        mRecycleView.setAdapter(mPeopleRVAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Seller_QA.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecycleView.setLayoutManager(linearLayoutManager);





    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageView usrimg,image;
        TextView username,reply;
        RatingBar ratingBar;
        TextView question;
        TextView answer;
        public NewsViewHolder(View itemView){
            super(itemView);
            question = (TextView)itemView.findViewById(R.id.title);
            answer = (TextView)itemView.findViewById(R.id.desc);
            username = (TextView)itemView.findViewById(R.id.username);
            usrimg  = (ImageView)itemView.findViewById(R.id.usrimg);
            reply = (TextView)itemView.findViewById(R.id.reply);
            ratingBar = (RatingBar)itemView.findViewById(R.id.stars);
            ratingBar.setVisibility(View.GONE);

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
