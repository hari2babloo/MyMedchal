package com.androidhari.mymedchal.SellerStuff;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.androidhari.mymedchal.Details;
import com.androidhari.mymedchal.Main2Activity;
import com.androidhari.mymedchal.R;
import com.androidhari.mymedchal.SupportFiles.GPSTracker;
import com.androidhari.mymedchal.SupportFiles.LocationTrack;
import com.androidhari.mymedchal.SupportFiles.ScalingUtilities;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Range;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Classess.DetailsModel;
import Classess.Photosmodel;
import Classess.QAmodels;
import Classess.RequestsModel;
import Classess.TinyDB;

public class Seller_Details extends AppCompatActivity {



     ImageView imageView;
    TextView addimage;
    private static int RESULT_LOAD_IMAGE = 1;
    TextView Title;
    AwesomeValidation mAwesomeValidation;
    EditText propname,businesname,contact1,contact2,whatsappno,emailid,desc,services,cost,timingfro,timingto,workingdays,yearsince,address,landmark,lane,lat,lng,addlogo;
    LinearLayout Linfo,Laddr;
    String picturepath;
    ProgressDialog pd;

    Button submit,button;
    String imageuploaded;
    TinyDB tinyDB;
    Context mContext;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    double longitude;
    double latitude;
    Button getlocation;
    String firebasestring="00";
    GPSTracker gps;

    int PLACE_PICKER_REQUEST = 1;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller__details);
        mContext = this;

        pd = new ProgressDialog(this);
        mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        tinyDB = new TinyDB(this);

        button = (Button)findViewById(R.id.button);
        getlocation = (Button)findViewById(R.id.getlocation);

        Linfo = (LinearLayout)findViewById(R.id.info);
        Laddr = (LinearLayout)findViewById(R.id.adres);
        Laddr.setVisibility(View.VISIBLE);
        propname = (EditText)findViewById(R.id.propname);
        businesname = (EditText)findViewById(R.id.businesname);
        contact1 = (EditText)findViewById(R.id.contact1);
        contact2 = (EditText)findViewById(R.id.contact2);
        whatsappno = (EditText)findViewById(R.id.whatsapp);
        emailid = (EditText)findViewById(R.id.email);
        desc = (EditText)findViewById(R.id.desc);
        services = (EditText)findViewById(R.id.services);
        cost = (EditText)findViewById(R.id.cost);
        timingfro = (EditText)findViewById(R.id.timinfrom);
        timingto = (EditText)findViewById(R.id.timingto);
        workingdays = (EditText)findViewById(R.id.workingdays);
        yearsince = (EditText)findViewById(R.id.since);
        address = (EditText)findViewById(R.id.address);
        landmark = (EditText)findViewById(R.id.landmark);
        lane = (EditText)findViewById(R.id.lane);
        lat = (EditText)findViewById(R.id.lat);
        lng = (EditText)findViewById(R.id.lon);
        submit = (Button) findViewById(R.id.submit);
        getdata();


        getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Seller_Details.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                } else {


                    //Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
                    gps = new GPSTracker(mContext, Seller_Details.this);

                    // Check if GPS enabled
                    if (gps.canGetLocation()) {

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                        lat.setText(String.valueOf( latitude));
                        lng.setText(String.valueOf(longitude));

                        // \n is for new line
                        //   Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        // Can't get location.
                        // GPS or network is not enabled.
                        // Ask user to enable GPS/network in settings.
                        gps.showSettingsAlert();
                    }
                }
            }
        });

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//                Toast.makeText(mContext, String.valueOf(latitude) + String.valueOf(longitude), Toast.LENGTH_SHORT).show();
//            }
//        });



        timingfro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();


                int hour = mcurrentTime.get(Calendar.AM_PM);
                int minute = mcurrentTime.get(Calendar.MINUTE);


                final TimePickerDialog mTimePicker;


                mTimePicker = new TimePickerDialog(Seller_Details.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String am_pm = "";

                        Calendar datetime = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        datetime.set(Calendar.MINUTE, selectedMinute);

                        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                            am_pm = "AM";
                        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                            am_pm = "PM";

                        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";

                        timingfro.setText( strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm );
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Timing From");

                mTimePicker.show();
            }
        });
        timingto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.AM_PM);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Seller_Details.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String am_pm = "";

                        Calendar datetime = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        datetime.set(Calendar.MINUTE, selectedMinute);

                        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                            am_pm = "AM";
                        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                            am_pm = "PM";

                        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";

                        timingto.setText( strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm );
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Timing To");
                mTimePicker.show();

            }
        });

//        addlogo.setFocusable(false);
//        addlogo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(
//                        Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                startActivityForResult(i, RESULT_LOAD_IMAGE);
//            }
//        });

        String regex = "[A-Za-z0-9\\s\\!\\\"\\#\\$\\%\\&\\'\\(\\)\\*\\+\\,\\-\\.\\/\\:\\;\\<\\>\\=\\?\\@\\[\\]\\{\\}\\\\\\\\\\^\\_\\`\\~]+$";
        //String regex = regex;
        mAwesomeValidation.addValidation(Seller_Details.this, R.id.propname, "[a-zA-Z\\s]+", R.string.errpropname);
        mAwesomeValidation.addValidation(Seller_Details.this, R.id.businesname, "[a-zA-Z\\s]+", R.string.errbname);
        mAwesomeValidation.addValidation(Seller_Details.this, R.id.contact1, Patterns.PHONE, R.string.errcontact);
        mAwesomeValidation.addValidation(Seller_Details.this, R.id.whatsapp, Patterns.PHONE, R.string.errwhatsap);
        mAwesomeValidation.addValidation(Seller_Details.this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.erremail);
        mAwesomeValidation.addValidation(Seller_Details.this, R.id.desc, regex, R.string.errdesc);
        mAwesomeValidation.addValidation(Seller_Details.this, R.id.services, regex, R.string.errservices);
        mAwesomeValidation.addValidation(Seller_Details.this, R.id.cost, regex, R.string.errcost);
        mAwesomeValidation.addValidation(Seller_Details.this, R.id.timinfrom, regex, R.string.errtmingfrom);
        mAwesomeValidation.addValidation(Seller_Details.this, R.id.timingto, regex, R.string.errtmingto);
        mAwesomeValidation.addValidation(Seller_Details.this, R.id.workingdays, regex, R.string.errworkingdays);
        mAwesomeValidation.addValidation(Seller_Details.this,R.id.address,regex,R.string.erraddr);
        mAwesomeValidation.addValidation(Seller_Details.this, R.id.landmark, regex, R.string.errlandmark);
        mAwesomeValidation.addValidation(Seller_Details.this, R.id.lane, regex, R.string.errlane);
        mAwesomeValidation.addValidation(Seller_Details.this, R.id.lat, regex, R.string.errlat);
        mAwesomeValidation.addValidation(Seller_Details.this, R.id.lon, regex, R.string.errlng);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAwesomeValidation.validate()){

                    submiphotoanddata();
                }
            }
        });
    }



    public void addlogo(View view){



        final Dialog dialog = new Dialog(Seller_Details.this,R.style.DialoTheme);

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




                    pd.setMessage("Submitting your Logo..");
                    pd.setCancelable(false);
                    pd.show();


                    StorageReference storage = FirebaseStorage.getInstance().getReference();
                    //   StorageReference storageRef = storage.getReference();

//        StorageReference mountainsRef = storageRef.child("mountains.jpg");

// Create a reference to 'images/mountains.jpg'
                    final StorageReference mountainImagesRef = storage.child("businesslogo/"+tinyDB.getString("key")+"/" +System.currentTimeMillis() +".jpg");

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
                                final DatabaseReference myRef = database.getReference("BusinessLists").child(tinyDB.getString("location")).child(tinyDB.getString("key"));
                                DetailsModel category = new DetailsModel();

                                category.setImg(imageuploaded);
                                myRef.child("img").setValue(imageuploaded);
                                pd.dismiss();

                                new AlertDialog.Builder(Seller_Details.this)
                                        .setTitle("Success!!!")
                                        .setMessage("We have uploaded your Logo")

                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton("FINISH", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {


                                                startActivity(new Intent(Seller_Details.this,Main2Activity.class));
                                                // Continue with delete operation'
                                                pd.dismiss();
                                            }
                                        })
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

    private void getdata() {
        pd.setMessage("Getting your Profile");
        pd.setCancelable(false);
        pd.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("BusinessLists").child(tinyDB.getString("location"));
        myRef.orderByKey().equalTo(tinyDB.getString("key")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("UI Bind", dataSnapshot.toString());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                   DetailsModel detailsModel = ds.getValue(DetailsModel.class);
                    detailsModel.setKey(ds.getKey());
                    propname.setText(detailsModel.propname);
                    businesname.setText(detailsModel.name);
                    contact1.setText(detailsModel.contact);
                    contact2.setText(detailsModel.contact2);
                    whatsappno.setText(detailsModel.whatsapp);
                    emailid.setText(detailsModel.email);
                    desc.setText(detailsModel.description);
                    services.setText(detailsModel.services);
                    cost.setText(detailsModel.cost);
                    timingfro.setText(detailsModel.timingsfrom);
                    timingto.setText(detailsModel.timingsto);
                    workingdays.setText(detailsModel.workindays);
                    address.setText(detailsModel.address);
                    landmark.setText(detailsModel.landmark);
                    lane.setText(detailsModel.lane);
                    lat.setText(detailsModel.lat);
                    lng.setText(detailsModel.lng);
                    Log.e("Name of Busines", detailsModel.getName());
                    pd.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Seller_Details.this, "No Data Found", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
        
    }

    private void submiphotoanddata() {
        pd.setMessage("Submitting your Details");
        pd.setCancelable(false);
        pd.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("BusinessLists").child(tinyDB.getString("location")).child(tinyDB.getString("key"));
        DetailsModel detailsModel = new DetailsModel();
        //
        detailsModel.setName(businesname.getText().toString());
        detailsModel.setPropname(propname.getText().toString());
        detailsModel.setContact(contact1.getText().toString());
        detailsModel.setContact2(contact2.getText().toString());
        //detailsModel.setContact3("756");
        detailsModel.setEmail(emailid.getText().toString());
        detailsModel.setAwards("No");
        detailsModel.setAddress(address.getText().toString());
        detailsModel.setLocation(tinyDB.getString("location"));
        detailsModel.setDescription(desc.getText().toString());
        detailsModel.setLandmark(landmark.getText().toString());
        detailsModel.setLane(lane.getText().toString());
        detailsModel.setLat(lat.getText().toString());
        detailsModel.setLng(lng.getText().toString());
        detailsModel.setWhatsapp(whatsappno.getText().toString());
        detailsModel.setServices(services.getText().toString());
        detailsModel.setDescription(desc.getText().toString());
        detailsModel.setTimingsfrom(timingfro.getText().toString());
        detailsModel.setTimingsto(timingto.getText().toString());
        detailsModel.setWebsite("na");
        detailsModel.setWorkindays(workingdays.getText().toString());
        detailsModel.setImg("imglink");
//        detailsModel.setVideo("Videolink");
        //      detailsModel.setRating("9");
        detailsModel.setSubcategory(tinyDB.getString("subcatkey"));
        detailsModel.setCost(cost.getText().toString());
        detailsModel.setCategory(tinyDB.getString("Category"));
        detailsModel.setSince(yearsince.getText().toString());
        detailsModel.setBand("na");

//        DatabaseReference sss = myRef.push();
//
//        String ss = sss.getKey();

//        tinyDB.putString("key",ss);
//        Log.e("sss",ss);




        myRef.setValue(detailsModel).addOnSuccessListener(new OnSuccessListener<Void>() {


            @Override
            public void onSuccess(Void aVoid) {

                //              String mGroupId = myRef.getKey();




//                Log.e("valuesuploades",mGroupId);


//
//                pd.setMessage("Updating Requests");
//                pd.setCancelable(false);
//                pd.show();
//
//                final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                final DatabaseReference myRef = database.getReference("requests").child(tinyDB.getString("location")).child(tinyDB.getString("requestkey"));
////        RequestsModel requestsModel = new RequestsModel();
////        requestsModel.setVisitdate(selecteddate);
//                myRef.child("status").setValue("FINISHED");
//                myRef.child("key").setValue(tinyDB.getString("key"));
//                myRef.child("statusmsg").setValue("Your Details are Live");
//                myRef.child("bname").setValue(businesname.getText().toString());
//                myRef.child("subcategory").setValue(tinyDB.getString("subcat"));
//                pd.dismiss();
//
//
//                pd.dismiss();
//
//
//
//                pd.setMessage("Creating Seller Profile");
//                pd.setCancelable(false);
//                pd.show();
//
//                final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
//                final DatabaseReference myRef2 = database2.getReference("ServiceProvider").child(tinyDB.getString("uid"));
//                RequestsModel requestsModel = new RequestsModel();
//                requestsModel.setKey(tinyDB.getString("key"));
//                requestsModel.setStatus("OPEN");
//                requestsModel.setLocation(tinyDB.getString("location"));
//                requestsModel.setTimestamp(System.currentTimeMillis());
//                requestsModel.setBname(businesname.getText().toString());
//                myRef.child("subcategory").setValue(tinyDB.getString("subcat"));
//                myRef2.push().setValue(requestsModel);
//
//                pd.dismiss();


                pd.dismiss();

                new AlertDialog.Builder(Seller_Details.this)
                        .setTitle("Success!!!")
                        .setMessage("We have uploaded your Given Information")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                startActivity(new Intent(Seller_Details.this, Main2Activity.class));
                                // Continue with delete operation'
                                pd.dismiss();
                            }
                        })
                        .show();
            }


        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("valuesuploades","Failed");
                        pd.dismiss();
                    }
                })
        ;




    }









    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the

                    // contacts-related task you need to do.

                    gps = new GPSTracker(mContext, Seller_Details.this);

                    // Check if GPS enabled
                    if (gps.canGetLocation()) {

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                        // \n is for new line
                        //                  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        // Can't get location.
                        // GPS or network is not enabled.
                        // Ask user to enable GPS/network in settings.
                        gps.showSettingsAlert();
                    }

                } else {


                    final Dialog openDialog = new Dialog(Seller_Details.this);
                    openDialog.setContentView(R.layout.alert);
                    openDialog.setTitle("Permission Request");
                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                    dialogTextContent.setText("MyMedchal needs your location for providing better pickup and delivery services.So,Please allow to access your location.");
                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                    dialogImage.setBackgroundResource(R.drawable.chalk);
                    dialogImage.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.chalk));
//              dialogImage.setBackground(this.getDrawable(ContextCompat.R.drawable.failure));
                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                    dialogCloseButton.setVisibility(View.GONE);
                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

                    dialogno.setText("OK");


                    dialogno.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDialog.dismiss();
                            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(Seller_Details.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                            } else {


                                //Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
                                gps = new GPSTracker(mContext, Seller_Details.this);

                                // Check if GPS enabled
                                if (gps.canGetLocation()) {

                                    latitude = gps.getLatitude();
                                    longitude = gps.getLongitude();

                                    // \n is for new line
//                                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                                } else {
                                    // Can't get location.
                                    // GPS or network is not enabled.
                                    // Ask user to enable GPS/network in settings.
                                    gps.showSettingsAlert();
                                }
                            }

                            //                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
                        }
                    });


                    openDialog.setCancelable(false);
                    openDialog.show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    //                   Toast.makeText(mContext, "You need to grant permission", Toast.LENGTH_SHORT).show();
                }

                return;
            }
        }
    }


}