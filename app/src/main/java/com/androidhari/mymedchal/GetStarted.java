package com.androidhari.mymedchal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.FirebaseDatabase;

public class GetStarted extends AppCompatActivity {

   // Button getstarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_started);
     //    getstarted = (Button)findViewById(R.id.getstarted);
    }

    public void getstartedbtn(View view){
      //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        startActivity(new Intent(GetStarted.this,MainActivity.class));
       // Intent intent = new Intent()


    }
}
