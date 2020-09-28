package com.example.snaptask;

import android.app.Application;
import android.content.Intent;

import com.example.snaptask.fragments.AddTasksFragment;
import com.example.snaptask.menu.Add;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class Home extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){
            Intent i = new Intent(this, Add.class);

            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
        }
    }
}
