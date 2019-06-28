package com.androidhari.mymedchal.SupportFiles;

import android.animation.Animator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhari.mymedchal.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import Classess.Reviewmodels;
import Classess.TinyDB;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment {

    TextView stars,title,describe,timestamp,userid,username,img,nameofbusines,location;
    RecyclerView  mRecycleView;
    private FirebaseRecyclerAdapter<Reviewmodels, NewsViewHolder> mPeopleRVAdapter;
    DatabaseReference mDatabase;
    String picturepath;
    TextView reviewtxt,addimage;
    String imageuploaded = "none";
    ImageView imageView;
    private int shortAnimationDuration;
    Animator currentAnimator;
    ProgressDialog pd;
    TinyDB tinyDB;
    private static int RESULT_LOAD_IMAGE = 1;
    public ReviewsFragment() {
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reviews, container, false);
  //      stars = (TextView)v.findViewById(R.id.stars);

        pd = new ProgressDialog(getContext());
        tinyDB = new TinyDB(getContext());
        mRecycleView = (RecyclerView)v.findViewById(R.id.recyclerview);
        reviewtxt = (TextView)v.findViewById(R.id.review);

        reviewtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext(),R.style.DialoTheme);

                dialog.setContentView(R.layout.writereview);
                dialog.setTitle("Title...");

                // set the custom dialog components - text, image and button
//                TextView text = (TextView) dialog.findViewById(R.id.text);
//                text.setText("Android custom dialog example!");
//                ImageView image = (ImageView) dialog.findViewById(R.id.image);
//                image.setImageResource(R.drawable.bellcon)
//
// ;
                final float ratingvalue;
                final RatingBar ratingBar = (RatingBar)dialog.findViewById(R.id.ratingBar);
                final EditText editText = (EditText)dialog.findViewById(R.id.editText);
                imageView = (ImageView)dialog.findViewById(R.id.imageView);

                 addimage = (TextView)dialog.findViewById(R.id.add);
                final Button submit = (Button)dialog.findViewById(R.id.submit);

                ratingBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    Log.e("rating", String.valueOf(ratingBar.getNumStars())+ratingBar.getRating());
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchimage();
                    }
                });
                addimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchimage();
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int noofstars = ratingBar.getNumStars();
                        final float getrating = ratingBar.getRating();
                        Log.e("ratog","Rating: "+getrating+"/"+noofstars);

                        View parentLayout = dialog.findViewById(R.id.parent);
                        if (getrating==0.0){

                            Snackbar.make(parentLayout, "Please Select Rating", Snackbar.LENGTH_LONG)
                                    .setAction("CLOSE", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                    .show();


                        }

                        else if (TextUtils.isEmpty(editText.getText().toString())){

                            editText.setError("Should not be Empty");
                        }

                        else if (imageuploaded.equalsIgnoreCase("none")) {


                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("reviews");

                            Reviewmodels category = new Reviewmodels();
                            category.setStars(String.valueOf(getrating));
                            category.setTimestanp(System.currentTimeMillis());
                            //category.setTitle("dfdsf");
                            category.setTitle(editText.getText().toString());
                            category.setUserid(tinyDB.getString("uid"));
                            category.setUsername(tinyDB.getString("uname"));
                            category.setProfilepic(tinyDB.getString("uimage"));
                            //category.setNameofbusiness("GA Mobiles");
                            category.setLocation(tinyDB.getString("location"));
                            category.setKey(tinyDB.getString("key"));
                            category.setImage(imageuploaded);
                            myRef.push().setValue(category);
                            pd.dismiss();
                            Snackbar.make(parentLayout, "Your Review has been posted", Snackbar.LENGTH_LONG)
                                    .setAction("CLOSE", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                    .show();
                            dialog.cancel();
                        }
                         else {




                            pd.setMessage("Submitting your Review..");
                            pd.setCancelable(false);
                            pd.show();


                            StorageReference storage = FirebaseStorage.getInstance().getReference();
                            //   StorageReference storageRef = storage.getReference();

//        StorageReference mountainsRef = storageRef.child("mountains.jpg");

// Create a reference to 'images/mountains.jpg'
                            final StorageReference mountainImagesRef = storage.child("reviews/"+tinyDB.getString("key")+ System.currentTimeMillis() +".jpg");

// While the file names are the same, the references point to different files
                            mountainImagesRef.getName().equals(mountainImagesRef.getName());    // true
                            mountainImagesRef.getPath().equals(mountainImagesRef.getPath());    // false
                            imageView.setDrawingCacheEnabled(true);
                            imageView.buildDrawingCache();
                            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();

                            UploadTask uploadTask = mountainImagesRef.putBytes(data);



                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                    // Continue with the task to get the download URL
                                    return mountainImagesRef.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        Log.e("Status of Upload",task.getResult().toString());
                                        imageuploaded = task.getResult().toString();

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                DatabaseReference myRef = database.getReference("reviews");
        Reviewmodels category = new Reviewmodels();
        category.setStars(String.valueOf(getrating));
        category.setTimestanp(System.currentTimeMillis());
        //category.setTitle("dfdsf");
        category.setTitle(editText.getText().toString());
        category.setUserid(tinyDB.getString("uid"));
        category.setUsername(tinyDB.getString("uname"));
        category.setProfilepic(tinyDB.getString("uimage"));
        category.setLocation(tinyDB.getString("location"));
        category.setKey(tinyDB.getString("key"));
        category.setImage(imageuploaded);
        myRef.push().setValue(category);
        pd.dismiss();
                                        View parentLayout = dialog.findViewById(R.id.parent);
                                        Snackbar.make(parentLayout, "Your Review has been posted", Snackbar.LENGTH_LONG)
                                                .setAction("CLOSE", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                    }
                                                })
                                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                                .show();
        dialog.cancel();
                                    } else {
                                        pd.dismiss();
                                        dialog.cancel();
                                        Toast.makeText(getContext(), "Please Try Again", Toast.LENGTH_SHORT).show();

                                        // Handle failures
                                        // ...
                                    }
                                }
                            });
                        }




//                        validateandsubmitreview();

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

        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
         mDatabase = FirebaseDatabase.getInstance().getReference().child("reviews");
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("key").equalTo(tinyDB.getString("key")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("data",dataSnapshot.toString());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Reviewmodels ss = ds.getValue(Reviewmodels.class);

//                    stars.setText(ss.getStars());
//                    Log.e("reviews", ss.getStars());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
            }
        });


        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Reviewmodels>().setQuery(mDatabase.orderByChild("key").equalTo(tinyDB.getString("key")).limitToLast(50),  Reviewmodels.class).build();
        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Reviewmodels, NewsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(NewsViewHolder holder, final int position, final Reviewmodels model) {
                holder.post_title.setText(model.title);
                holder.post_desc.setText(model.getAns());
                holder.username.setText(model.getUsername());
                holder.ratingBar.setRating(Float.parseFloat(String.valueOf(model.getStars())));
                Glide.with(getContext()).load(model.image).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.image);
                Glide.with(getContext()).load(model.profilepic).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.usrimg);


                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shortAnimationDuration = getResources().getInteger(
                                android.R.integer.config_shortAnimTime);
                        final Dialog fbDialogue = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar);

                        fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
                        fbDialogue.setContentView(R.layout.image_fullscreen);

                        if (currentAnimator != null) {
                            currentAnimator.cancel();
                        }



                        final ImageView expandedImageView = (ImageView)fbDialogue.findViewById(
                                R.id.expanded_image);
                        Glide.with(getContext()).load(model.image).diskCacheStrategy(DiskCacheStrategy.DATA).into(expandedImageView);
                        fbDialogue.setCancelable(true);
                        fbDialogue.show();
                    }
                });

//                holder.setImage(getBaseContext(), model.getImage());
                Log.e("result", String.valueOf(model.getStars()));
            }

            @Override
            public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.review_template_fragment, parent, false);

                return new NewsViewHolder(view);
            }
        };

        mRecycleView.setAdapter(mPeopleRVAdapter);



        return v;
    }

    private void validateandsubmitreview() {



    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageView usrimg,image;
        TextView username;
        RatingBar ratingBar;
        TextView post_title;
        TextView post_desc;
        public NewsViewHolder(View itemView){
            super(itemView);
             post_title = (TextView)itemView.findViewById(R.id.title);
             post_desc = (TextView)itemView.findViewById(R.id.desc);
            username = (TextView)itemView.findViewById(R.id.username);
            usrimg  = (ImageView)itemView.findViewById(R.id.usrimg);
            image  = (ImageView)itemView.findViewById(R.id.image);
            ratingBar = (RatingBar)itemView.findViewById(R.id.stars);

//            mView = itemView;
        }



    }
    private void fetchimage() {

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor =  getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturepath = cursor.getString(columnIndex);
            cursor.close();

            // ImageView imageView = (ImageView) findViewById(R.id.imgView);

//           profileimg.setImageBitmap(BitmapFactory.decodeFile(picturepath));

            Log.e("picturepath",picturepath);


            decodefile(picturepath,800,800);

        }


    }


    private String decodefile(String path, int DESIREDWIDTH , int DESIREDHEIGHT) {

        imageuploaded   = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
                //  profileimg.setImageBitmap( scaledBitmap);
//                profileimg.setImageBitmap(BitmapFactory.decodeFile(strMyImagePath));
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/TMMFOLDER");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = "tmp.png";

            File f = new File(mFolder.getAbsolutePath(), s);

            imageuploaded = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (imageuploaded == null) {
            return path;
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400, 400);
        imageView.setImageBitmap(BitmapFactory.decodeFile(imageuploaded));

        imageView.setLayoutParams(layoutParams);
        addimage.setVisibility(View.GONE);
        return imageuploaded;


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
