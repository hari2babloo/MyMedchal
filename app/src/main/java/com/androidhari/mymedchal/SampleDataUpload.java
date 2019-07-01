package com.androidhari.mymedchal;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


import Classess.CategoryModel;
import Classess.CommentsModel;
import Classess.DetailsModel;
import Classess.LocationsModel;
import Classess.MediaModel;
import Classess.Photosmodel;
import Classess.QAmodels;
import Classess.Reviewmodels;
import Classess.SubCategoryModel;

public class SampleDataUpload extends AppCompatActivity {

    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_data_upload);
        mDatabase = FirebaseDatabase.getInstance().getReference();
//getvalue();
postvalue();




    }

    private void postvalue() {


        //Add Locations


        FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//
//
//        //uploadPhotos
//        DatabaseReference myRef = database.getReference("locations");
//        LocationsModel category = new LocationsModel();
//        category.setUrl("https://www.business.qld.gov.au/__data/assets/image/0023/215573/go-local-grow-local.png");
//        category.setName("Athvelly");
//
//        myRef.push().setValue(category);
//
//        category.setUrl("https://www.business.qld.gov.au/__data/assets/image/0023/215573/go-local-grow-local.png");
//        category.setName("Medchal");
//
//        myRef.push().setValue(category);
//        category.setUrl("https://www.business.qld.gov.au/__data/assets/image/0023/215573/go-local-grow-local.png");
//        category.setName("Kandlakoya");
//
//        myRef.push().setValue(category);
//        category.setUrl("https://www.business.qld.gov.au/__data/assets/image/0023/215573/go-local-grow-local.png");
//        category.setName("Gundlapochampally");
//
//        myRef.push().setValue(category);category.setUrl("https://www.business.qld.gov.au/__data/assets/image/0023/215573/go-local-grow-local.png");
//        category.setName("Gowdavelli");
//
//        myRef.push().setValue(category);
//        category.setUrl("https://www.business.qld.gov.au/__data/assets/image/0023/215573/go-local-grow-local.png");
//        category.setName("Railapur");
//
//        myRef.push().setValue(category);
//        category.setUrl("https://www.business.qld.gov.au/__data/assets/image/0023/215573/go-local-grow-local.png");
//        category.setName("Pudur");
//
//        myRef.push().setValue(category);




  //      Signup signup = new Signup();

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//
//
//        //uploadPhotos
//        DatabaseReference myRef = database.getReference("photos").child("-LbOhR4pW4FoDiSbQU5k");
//        Photosmodel category = new Photosmodel();
//        category.setImgurl("https://www.business.qld.gov.au/__data/assets/image/0023/215573/go-local-grow-local.png");
//        category.setTimestanp(45534542);
//        category.setUserid("fdsfsdf");
//        category.setUsername("fsdfsdfsdfdsf");
//        category.setProfilepic("dfsdfsfsdf");
//        category.setNameofbusiness("GA Mobiles");
//        category.setLocation("fdsfsdf");
//        category.setKey("hjhb");
//        category.setDescription("New photo");
//        myRef.push().setValue(category);


        //Q&A

//        DatabaseReference myRef = database.getReference("Q&A");
//        QAmodels category = new QAmodels();
//        category.setAns("4adasfafa");
//        category.setTimestanp(45534542);
//        category.setUserid("fdsfsdf");
//        category.setUsername("fsdfsdfsdfdsf");
//        category.setProfilepic("dfsdfsfsdf");
//        category.setNameofbusiness("GA Mobiles");
//        category.setLocation("fdsfsdf");
//        category.setKey("hjhb");
//        category.setQuestion("how are you");
//        myRef.push().setValue(category);




        //ReviewModels
//
//        DatabaseReference myRef = database.getReference("reviews");
//
//
//        myRef.orderByChild("key").equalTo("-Lih14wdd8PDDSy26FDk").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
////
//
////                    String ss = postSnapshot.getKey().toString();
//                    String ss3 = postSnapshot.child("userid").getValue().toString();
//                    Log.e("Data",ss3);
//
//
//                    if (ss3.equalsIgnoreCase("0FKjAKMf5iZtAQsikcf2KLAMI1e2")){
//
//                        Reviewmodels category = new Reviewmodels();
//                        category.setStars("4");
//                        category.setTimestanp(45534542);
//                        category.setTitle("dfdsf");
//                        category.setDescription("fsdfsdfdsfsdfsdf");
//                        category.setUserid("fdsfsdf");
//                        category.setUsername("fsdfsdfsdfdsf");
//                        category.setProfilepic("dfsdfsfsdf");
//                        category.setNameofbusiness("GA Mobiles");
//                        category.setLocation("fdsfsdf");
//                        category.setKey("-LbOhR4pW4FoDiSbQU5k");
//
//                        myRef.push().setValue(category);
//
//                    }
//
//
//                    else {
//
//
//
//                    }
//  //                  Log.e("Data2",ss3);
//                    // TODO: handle the post
//                }
//
//
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



//  Category
//        DatabaseReference myRef = database.getReference("Category");
//        CategoryModel category = new CategoryModel();
//        category.setName("Shopping");
//        category.setImg("imgs");
//        category.setDesc("Description");
//        category.setStar("value");
//        category.setData("data");
//        myRef.push().setValue(category);



////        SubCategory List
//
//
//        DatabaseReference myRef = database.getReference("SubCategory");
//        SubCategoryModel category = new SubCategoryModel();
//
//        category.setName("Home Appliances");
//        category.setImg("imgs");
//        category.setDesc("Description");
//        category.setStar("value");
//        category.setData("null");
//        category.setCategory("Shopping");
//        myRef.push().setValue(category);


        //Details
//        DetailsModel detailsModel  = new DetailsModel();
//        DatabaseReference myRef = database.getReference().child("Dhaba");
//
//
//        detailsModel.setName("MM Dhaba");
//        detailsModel.setPropname("hari");
//        detailsModel.setContact("1321");
//        detailsModel.setContact2("9856");
//        detailsModel.setContact3("756");
//        detailsModel.setEmail("Hari@gmail.com");
//        detailsModel.setAwards("BEst if best");
//        detailsModel.setAddress("Raghavendra Nagar");
//        detailsModel.setLocation("Athvelly");
//        detailsModel.setDescription("Best Mobile in market");
//        detailsModel.setLandmark("Water Tank");
//        detailsModel.setLane("raghavendra nagar");
//        detailsModel.setLat("0.0000");
//        detailsModel.setLng("0.0000");
//        detailsModel.setWhatsapp("9666");
//        detailsModel.setTimingsfrom("10:00 am");
//        detailsModel.setTimingsto("6:00 pm");
//        detailsModel.setWebsite("www.google.com");
//        detailsModel.setWorkindays("Mon-Fri");
//        detailsModel.setImg("imglink");
//        detailsModel.setVideo("Videolink");
//        detailsModel.setRating("9");
//        detailsModel.setSubcategory("Dhaba");
//        detailsModel.setCost("$$");
//        detailsModel.setCategory("Food");
//        detailsModel.setSince("1999");
//        detailsModel.setBand("Exclusive");
//        myRef.push().setValue(detailsModel);





//       ListDetails
//
//        DatabaseReference myRef = database.getReference("Business").child("8125110147");
//
//
//        DetailsModel detailsModel   = new DetailsModel();
//        detailsModel.setName("AndroidHari");
//        detailsModel.setPropname("Hari  Krishna");
//        detailsModel.setContact("8125110147");
//        detailsModel.setContact2("8074219509");
//        detailsModel.setWebsite("Androidhari.com");
//        detailsModel.setWhatsapp("8125110147");
//        detailsModel.setEmail("hari2babloo@live.com");
//        detailsModel.setLocation("Medchal");
//        detailsModel.setAwards("Mostwanted");
//        detailsModel.setServices("Android apps");
//        detailsModel.setDescription("Android Developer awith good ecperience");
//        detailsModel.setTimingsfrom("10am-6pm");
//        detailsModel.setWorkindays("mon-saturday");
//        detailsModel.setImg("img");
//        detailsModel.setVideo("ytvideo");
//        detailsModel.setRating("Good");
//        detailsModel.setLikes("34");
//        detailsModel.setCategory("Hospitals");
//        detailsModel.setSubcategory("Dental");
//
//        myRef.setValue(detailsModel);


//    Images
//
//        DatabaseReference myRef = database.getReference("Locations").child("Athvelly").child("Shoppings").child("Home Appliances").child("Android Hari").child("images").push();
//
//        MediaModel  mediaModel = new MediaModel();
//
//        mediaModel.setName("img1");
//        mediaModel.setPreview("img2");
//        mediaModel.setUrl("url");
//
//        myRef.setValue(mediaModel);

//     Video

//
//        DatabaseReference myRef = database.getReference("Locations").child("Athvelly").child("Shoppings").child("Home Appliances").child("Android Hari").child("video").push();
//
//        MediaModel  mediaModel = new MediaModel();
//
//        mediaModel.setName("img1");
//        mediaModel.setPreview("img2");
//        mediaModel.setUrl("url");
//
//        myRef.setValue(mediaModel);


// Comments

        //    Images

//        DatabaseReference myRef = database.getReference("Locations").child("Athvelly").child("Shoppings").child("Home Appliances").child("Android Hari").child("comment").push();
//
//
//        CommentsModel commentsModel = new CommentsModel();
//
//        commentsModel.setHeading("Good");
//        commentsModel.setDesc("very good enfsdfsd fsdfsdfsd fsdfsd fsd fsdf sd");
//        commentsModel.setUid("Uid");
//        commentsModel.setUname("name");
//        commentsModel.setProfimgurl("url");
//        commentsModel.setRating("5");
//        commentsModel.setTimestamp("timeee");
//        myRef.setValue(commentsModel);





//
//myRef.child("82").child("images").setValue(detailsModel);
    //    myRef.setValue(subCategoryModel);
//        DatabaseReference myRef2 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Home Appliances").child("8125110150");;
//        DatabaseReference myRef3 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Home Appliances").child("8125110152");;
//        DatabaseReference myRef4 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Home Appliances").child("8125110151");;
//        DatabaseReference myRef5 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Home Appliances").child("8125110153");;
//        DatabaseReference myRef6 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Home Appliances").child("8125110154");;
//        DatabaseReference myRef7 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Home Appliances").child("8125110155");;
//        DatabaseReference myRef8 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Home Appliances").child("8125110156");;
//        DatabaseReference myRef9 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Home Appliances").child("8125110157");;
//        DatabaseReference myRef0 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Home Appliances").child("8125110158");;
//        DatabaseReference myRef11 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Home Appliances").child("8125110159");;
//        DatabaseReference myRef22 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Home Appliances").child("8125110160");;
//        DatabaseReference myRef33 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Home Appliances").child("8125110161");;
//
////        myRef.setValue(signup);
//
//        myRef2.setValue(signup);
//        myRef3.setValue(signup);
//        myRef4.setValue(signup);
//        myRef5.setValue(signup);
//        myRef6.setValue(signup);
//        myRef7.setValue(signup);
//        myRef8.setValue(signup);
//        myRef9.setValue(signup);
//        myRef0.setValue(signup);
//        myRef11.setValue(signup);
//        myRef22.setValue(signup);
//        myRef33.setValue(signup);












//        myRef


  //      myRef.setValue(signup);

//        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
////
//        DatabaseReference myRef = database2.getReference("Athvelly").child("Hospitals");
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//
//
//                    String ss = postSnapshot.getKey().toString();
////                    String ss3 = postSnapshot.child(ss).getValue().toString();
//                    Log.e("Data",ss);
//  //                  Log.e("Data2",ss3);
//                    // TODO: handle the post
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void getvalue() {

        DatabaseReference myRef = mDatabase.child("locations").child("Athvelly").child("Hospitals");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dataSnapshot.getKey();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {


                    String ss = postSnapshot.getKey();
                    String ss3 = postSnapshot.child("Children").getKey();
                    Log.e("Data",ss);
                    Log.e("Data2",ss3);
                    // TODO: handle the post
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
