package com.androidhari.mymedchal;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by b on 28/3/19.
 */

public class MyMedchal extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
