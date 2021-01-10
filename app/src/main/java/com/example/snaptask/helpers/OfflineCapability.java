package com.example.snaptask.helpers;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;

import com.example.snaptask.MainActivity;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class OfflineCapability extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
