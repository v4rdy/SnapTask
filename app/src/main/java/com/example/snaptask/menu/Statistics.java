package com.example.snaptask.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.snaptask.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Statistics extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private DrawerLayout drawer;
    private List<Integer> completedTasksValue;

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.statistics);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        completedTasks(8);
        additionElementsOfArray();



        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.notes:
                        startActivity(new Intent(getApplicationContext(), Notes.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.tasks:
                        startActivity(new Intent(getApplicationContext(), Tasks.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.goals:
                        startActivity(new Intent(getApplicationContext(), Goals.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.statistics:
                        return true;
                    case R.id.add:
                        startActivity(new Intent(getApplicationContext(), Add.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });
    }

    private void completedTasks(int period) {
        completedTasksValue = new ArrayList<>();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String date = df.format(currentTime);
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("tasks").child(date);
            Query query = databaseReference.orderByChild("status").startAt(minusDay(date,period)).endAt(date);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("INFO", "Completed Tasks:  " + String.valueOf(dataSnapshot.getChildrenCount()));
                String count = String.valueOf(dataSnapshot.getChildrenCount());
                Log.e("TAG", "count - " + count);
                int newCount = Integer.parseInt(count);
                Log.e("TAG", "newCount - " + newCount);
                completedTasksValue.add(newCount);
                Log.e("TAG", "Values - " + completedTasksValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
            query.addListenerForSingleValueEvent(listener);

        }


    private void getData(DatabaseReference ref) {

    }

    private String minusDay(String date, int period) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date1 = new Date();

        try {
            date1 = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String newDate;
        Calendar c = Calendar.getInstance();
        assert date1 != null;
        c.setTime(date1);
        c.add(Calendar.DATE, -period+1);
        date1 = c.getTime();

        newDate = df.format(date1);
        Log.e("TAG", "New Date - " + newDate);
        return newDate;
    }

    private int additionElementsOfArray() {
        int result = 0;
        for(int i = 0; i<completedTasksValue.size(); i++){
            result+= completedTasksValue.get(i);
        }
        Log.e("TAG", "Result - " + result);
        return result;
    }
}
