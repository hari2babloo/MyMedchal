package com.androidhari.mymedchal.SellerStuff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.animation.Animator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhari.mymedchal.R;
import com.androidhari.mymedchal.SupportFiles.ScalingUtilities;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Classess.Photosmodel;
import Classess.TinyDB;


public class Seller_Photos extends AppCompatActivity {



    DatabaseReference mDatabase;
    DatabaseReference mDatabase2;
    TextView addphoto;
    private AdapterFish Adapter;
    RecyclerView mRVFishPrice;
    ImageView imageView;
    TextView addimage;
    String imageuploaded="none";
    String picturepath;
    List<String> filterdata= new ArrayList<String>();
    private static int RESULT_LOAD_IMAGE = 1;


    //getPickupDeliveryOrders data = new getPickupDeliveryOrders();
    final ArrayList<String> dd = new ArrayList<>();


    private Animator currentAnimator;

    private int shortAnimationDuration;
    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.

    ImageView expandedImageView;
    LinearLayout container;
    TinyDB  tinyDB;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller__photos);

        tinyDB = new TinyDB(this);
        pd = new ProgressDialog(this);
        mRVFishPrice = (RecyclerView)findViewById(R.id.recyclerview);
        expandedImageView  = (ImageView)findViewById(R.id.expanded_image);
        addphoto = (TextView)findViewById(R.id.addphoto);
        container = (LinearLayout)findViewById(R.id.container);
        //mRVFishPrice.setHasFixedSize(true);
        // mRVFishPrice.setLayoutManager(new GridLayoutManager(getContext(),3));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("photos");


        mDatabase.keepSynced(true);

        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(Seller_Photos.this,R.style.DialoTheme);

                dialog.setContentView(R.layout.addphoto);
                dialog.setTitle("Add Photos...");

                imageView = (ImageView)dialog.findViewById(R.id.imageView);

                addimage = (TextView)dialog.findViewById(R.id.add);
                final Button submit = (Button)dialog.findViewById(R.id.submit);


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


                        View parentLayout = dialog.findViewById(R.id.parent);
                        if (imageuploaded.equalsIgnoreCase("none")) {

                            Snackbar.make(parentLayout, "You have not selected any Photo", Snackbar.LENGTH_LONG)
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




                            pd.setMessage("Submitting your Image..");
                            pd.setCancelable(false);
                            pd.show();


                            StorageReference storage = FirebaseStorage.getInstance().getReference();
                            //   StorageReference storageRef = storage.getReference();

//        StorageReference mountainsRef = storageRef.child("mountains.jpg");

// Create a reference to 'images/mountains.jpg'
                            final StorageReference mountainImagesRef = storage.child("photos/"+tinyDB.getString("key")+"/" +System.currentTimeMillis() +".jpg");

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
                                        DatabaseReference myRef = database.getReference("photos").child(tinyDB.getString("key"));
                                        Photosmodel category = new Photosmodel();
                                        category.setTimestanp(System.currentTimeMillis());
                                        //category.setTitle("dfdsf");
                                        category.setUserid(tinyDB.getString("uid"));
                                        category.setUsername(tinyDB.getString("uname"));
                                        category.setProfilepic(tinyDB.getString("uimage"));
                                        category.setLocation(tinyDB.getString("location"));
                                        category.setKey(tinyDB.getString("key"));
                                        category.setImgurl(imageuploaded);
                                        myRef.push().setValue(category);
                                        pd.dismiss();
                                        View parentLayout = dialog.findViewById(R.id.parent);
                                        Snackbar.make(parentLayout, "Your Photo has been posted", Snackbar.LENGTH_LONG)
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
                                        Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_SHORT).show();

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


        mDatabase.orderByKey().equalTo(tinyDB.getString("key")).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e("photosdata",dataSnapshot.getKey());
                //              Photosmodel ss = dataSnapshot.getValue(Photosmodel.class);
//                Log.e("reviews", ss.getImgurl());

                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    Log.e("PhotoKeys",ds.getKey());


                    Photosmodel ss2 = ds.getValue(Photosmodel.class);

                    filterdata.add(ss2.imgurl);


//                    stars.setText(ss.getStars());
                    Log.e("photosname", ss2.getImgurl());

                }

             //   ((Details)getApplicationContext()).dispatchInformations(filterdata);
                DiplayDaata();
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





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

            Cursor cursor =  getApplicationContext().getContentResolver().query(selectedImage,
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

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400, 400);
        imageView.setImageBitmap(BitmapFactory.decodeFile(imageuploaded));

        //      imageView.setLayoutParams(layoutParams);
        addimage.setVisibility(View.GONE);
        return imageuploaded;


    }


    private void DiplayDaata() {

        Adapter = new AdapterFish(Seller_Photos.this, filterdata);
        Adapter.setHasStableIds(false);
        mRVFishPrice.scrollToPosition(0);
        mRVFishPrice.setHasFixedSize(true);
        mRVFishPrice.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        mRVFishPrice.setAdapter(Adapter);
    }






    @Override
    public void onStart() {
        super.onStart();
//        mPeopleRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //      mPeopleRVAdapter.stopListening();
    }

    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<String> data = Collections.emptyList();
        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;
        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(Context context, List<String> data) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.photos_template_fragment, parent, false);
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
            final String current = data.get(position);

            // final modelPickuplist current = data.get(position);
            //  holder.getLayoutPosition();
            //    setHasStableIds(true);

            Log.e("imgurl",current);
            Glide.with(Seller_Photos.this).load(current).apply(RequestOptions.centerCropTransform()).into(((MyHolder) holder).servicename);


            ((AdapterFish.MyHolder) holder).servicename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    shortAnimationDuration = getResources().getInteger(
                            android.R.integer.config_shortAnimTime);
                    final Dialog fbDialogue = new Dialog(Seller_Photos.this, android.R.style.Theme_Black_NoTitleBar);

                    fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
                    fbDialogue.setContentView(R.layout.image_fullscreen);

                    if (currentAnimator != null) {
                        currentAnimator.cancel();
                    }



                    final ImageView expandedImageView = (ImageView)fbDialogue.findViewById(R.id.expanded_image);
                    Glide.with(Seller_Photos.this).load(current).diskCacheStrategy(DiskCacheStrategy.DATA).into(expandedImageView);
                    fbDialogue.setCancelable(true);
                    fbDialogue.show();
                }
            });

        }
        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {

            ImageView expresimg, servicename;
            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                //expresimg = itemView.findViewById(R.id.expresimg);
                servicename = (ImageView)itemView.findViewById(R.id.imageView);


            }
        }

    }
}
