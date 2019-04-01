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
import Classess.MediaModel;
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

  //      Signup signup = new Signup();

        FirebaseDatabase database = FirebaseDatabase.getInstance();



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
        DatabaseReference myRef = database.getReference("SubCategory");
        SubCategoryModel category = new SubCategoryModel();

        category.setName("Mobiles");
        category.setImg("imgs");
        category.setDesc("Description");
        category.setStar("value");
        category.setData("null");
        category.setCategory("Shopping");
        myRef.push().setValue(category);

        category.setName("Electronics");
        category.setImg("imgs");
        category.setDesc("Description");
        category.setStar("value");
        category.setData("null");
        category.setCategory("Shopping");
        myRef.push().setValue(category);

        category.setName("Clothing");
        category.setImg("imgs");
        category.setDesc("Description");
        category.setStar("value");
        category.setData("null");
        category.setCategory("Shopping");
        myRef.push().setValue(category);

        category.setName("Home Appliances");
        category.setImg("imgs");
        category.setDesc("Description");
        category.setStar("value");
        category.setData("null");
        category.setCategory("Shopping");
        myRef.push().setValue(category);

        category.setName("Jewellery");
        category.setImg("imgs");
        category.setDesc("Description");
        category.setStar("value");
        category.setData("null");
        category.setCategory("Shopping");
        myRef.push().setValue(category);

        category.setName("Artificial Jewellery");
        category.setImg("imgs");
        category.setDesc("Description");
        category.setStar("value");
        category.setData("null");
        category.setCategory("Shopping");
        myRef.push().setValue(category);


        category.setName("Family");
        category.setImg("imgs");
        category.setDesc("Description");
        category.setStar("value");
        category.setData("null");
            category.setCategory("Food");
        myRef.push().setValue(category);

        category.setName("Vegetarian");
        category.setImg("imgs");
        category.setDesc("Description");
        category.setStar("value");
        category.setData("null");
        category.setCategory("Food");
        myRef.push().setValue(category);

        category.setName("Non-Vegetarian");
        category.setImg("imgs");
        category.setDesc("Description");
        category.setStar("value");
        category.setData("null");
        category.setCategory("Food");
        myRef.push().setValue(category);

        category.setName("Tiffins");
        category.setImg("imgs");
        category.setDesc("Description");
        category.setStar("value");
        category.setData("null");
        category.setCategory("Food");
        myRef.push().setValue(category);

        category.setName("Meals");
        category.setImg("imgs");
        category.setDesc("Description");
        category.setStar("value");
        category.setData("null");
        category.setCategory("Food");
        myRef.push().setValue(category);

        category.setName("Dhaba");
        category.setImg("imgs");
        category.setDesc("Description");
        category.setStar("value");
        category.setData("null");
        category.setCategory("Food");
        myRef.push().setValue(category);

        category.setName("Sweets");
        category.setImg("imgs");
        category.setDesc("Description");
        category.setStar("value");
        category.setData("null");
        category.setCategory("Food");
        myRef.push().setValue(category);





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
//        DatabaseReference myRef = database.getReference("Locations").child("Athvelly").child("Shoppings").child("Clothing").child("Android Hari").child("images").push();
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
//        DatabaseReference myRef = database.getReference("Locations").child("Athvelly").child("Shoppings").child("Clothing").child("Android Hari").child("video").push();
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

//        DatabaseReference myRef = database.getReference("Locations").child("Athvelly").child("Shoppings").child("Clothing").child("Android Hari").child("comment").push();
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
//        DatabaseReference myRef2 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Clothing").child("8125110150");;
//        DatabaseReference myRef3 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Clothing").child("8125110152");;
//        DatabaseReference myRef4 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Clothing").child("8125110151");;
//        DatabaseReference myRef5 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Clothing").child("8125110153");;
//        DatabaseReference myRef6 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Clothing").child("8125110154");;
//        DatabaseReference myRef7 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Clothing").child("8125110155");;
//        DatabaseReference myRef8 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Clothing").child("8125110156");;
//        DatabaseReference myRef9 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Clothing").child("8125110157");;
//        DatabaseReference myRef0 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Clothing").child("8125110158");;
//        DatabaseReference myRef11 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Clothing").child("8125110159");;
//        DatabaseReference myRef22 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Clothing").child("8125110160");;
//        DatabaseReference myRef33 = database.getReference("Locations").child("Athvelly").child("Shopping").child("Clothing").child("8125110161");;
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
