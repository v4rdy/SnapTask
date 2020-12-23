package com.example.snaptask.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.snaptask.MainActivity;
import com.example.snaptask.R;
import com.example.snaptask.addFragments.AddGoalsFragment;
import com.example.snaptask.addFragments.AddNotesFragment;
import com.example.snaptask.addFragments.AddTasksFragment;
import com.example.snaptask.navigationViewFragments.AboutFragment;
import com.example.snaptask.navigationViewFragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
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
    private double completedTasksValue = 0;
    private double uncompletedTasksValue = 0;
    private int completedGoalsValue = 0;
    private TextView completedTasksText, uncompletedTasksText, activityPercent, completedGoalsText;
    private RadioButton weekBtn, monthBtn, yearBtn;

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

        final NavigationView navigationView = findViewById(R.id.left_nav_view);
        navigationView.bringToFront();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.statistics);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        completedTasksText = findViewById(R.id.comp_tasks_value);
        uncompletedTasksText = findViewById(R.id.uncomp_tasks_value);
        completedGoalsText = findViewById(R.id.completed_goals);
        activityPercent = findViewById(R.id.activity_percent);
        final TextView label = (TextView) findViewById(R.id.label);
        RadioGroup rgPeriod = findViewById(R.id.rg_period);
        weekBtn = findViewById(R.id.week_btn);
        monthBtn = findViewById(R.id.month_btn);
        yearBtn = findViewById(R.id.year_btn);
        final LinearLayout linlay = (LinearLayout) findViewById(R.id.linlay);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        completedTasks(7);
        uncompletedTasks(7);
        completedGoals();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.e("TAG", "Item Selected");
                switch (item.getItemId())
                {
                    case R.id.logout_btn:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(Statistics.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.profile_btn:
                        System.out.println("profile");
                        Fragment fragment = new ProfileFragment();
                        FrameLayout fg = findViewById(R.id.fragment_container2);
                        fg.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, fragment).commit();
                        label.setText(getString(R.string.profile));
                        linlay.setVisibility(View.GONE);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.about_btn:
                        Fragment fragment1 = new AboutFragment();
                        FrameLayout fg1 = findViewById(R.id.fragment_container2);
                        fg1.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, fragment1).commit();
                        label.setText(getString(R.string.about));
                        linlay.setVisibility(View.GONE);
                        drawer.closeDrawer(GravityCompat.START);

                        break;
                }
                return true;
            }
        });

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
                        FrameLayout fg = findViewById(R.id.fragment_container2);
                        fg.setVisibility(View.GONE);
                        label.setText(getString(R.string.title_statistic));
                        linlay.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.add:
                        startActivity(new Intent(getApplicationContext(), Add.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });


        rgPeriod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(weekBtn.isChecked()) {
                    weekBtn.setTextColor(getResources().getColor(R.color.white));
                    monthBtn.setTextColor(getResources().getColor(R.color.blue));
                    yearBtn.setTextColor(getResources().getColor(R.color.blue));

                    weekBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            completedTasks(7);
                            uncompletedTasks(7);
                            completedGoals();
                        }
                    });
                }
                if(monthBtn.isChecked()) {
                    weekBtn.setTextColor(getResources().getColor(R.color.blue));
                    monthBtn.setTextColor(getResources().getColor(R.color.white));
                    yearBtn.setTextColor(getResources().getColor(R.color.blue));

                    monthBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            completedTasks(31);
                            uncompletedTasks(31);
                            completedGoals();
                        }
                    });
                }
                if(yearBtn.isChecked()) {
                    weekBtn.setTextColor(getResources().getColor(R.color.blue));
                    monthBtn.setTextColor(getResources().getColor(R.color.blue));
                    yearBtn.setTextColor(getResources().getColor(R.color.white));

                    yearBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            completedTasks(365);
                            uncompletedTasks(365);
                            completedGoals();
                        }
                    });
                }



            }
        });
    }

    private void completedTasks(final int period) {
        completedTasksValue = 0;
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String date = df.format(currentTime);
        for(int i =0; i<period; i++) {
            Log.d("TAG", "For  "+i);
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("tasks").child(date);
            Query query = databaseReference.orderByChild("status").equalTo("true");
            getData(query, new MyCallback() {
                @Override
                public void onCallback(int count) {
                    completedTasksValue += count;
                    String comp;

                    if(completedTasksValue == 0){
                        comp = String.format("%d",(int)Math.round(completedTasksValue));
                    }
                    else{
                        comp = String.format("%.0f",completedTasksValue);
                    }
                    completedTasksText.setText(comp);
                    Log.d("TAG", "Callback");
                }
            });
            date = minusDay(date);
        }

        }

    private void uncompletedTasks(final int period) {
        uncompletedTasksValue = 0;
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String date = df.format(currentTime);
        for(int i =0; i<period; i++) {
            Log.d("TAG", "For  "+i);
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("tasks").child(date);
            Query query = databaseReference.orderByChild("status").equalTo("false");
            getData(query, new MyCallback() {
                @Override
                public void onCallback(int count) {
                    uncompletedTasksValue += count;
                    String uncomp;
                    if(uncompletedTasksValue == 0){
                        uncomp = String.format("%d",(int)Math.round(uncompletedTasksValue));
                    }
                    else{
                        uncomp = String.format("%.0f",uncompletedTasksValue);
                    }
                    uncompletedTasksText.setText(uncomp);
                    Log.d("TAG", "Callback");

                    double activity = 0;
                    double sum = completedTasksValue+uncompletedTasksValue;
                    activity = (completedTasksValue/(sum))*100;
                    Log.e("TAG", "Comp - " +completedTasksValue+"   Uncomp - "+uncompletedTasksValue);
                    Log.e("TAG", "Activity - " + activity);
                    String result = String.format("%.0f",activity);
                    activityPercent.setText(result+"%");
                }
            });
            date = minusDay(date);
        }

    }

    private void completedGoals() {
        completedGoalsValue = 0;
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("goals");
            Query query = databaseReference.orderByChild("status").equalTo("true");
            getData(query, new MyCallback() {
                @Override
                public void onCallback(int count) {
                    completedGoalsValue += count;
                    completedGoalsText.setText(Integer.toString(completedGoalsValue));
                    Log.d("TAG", "Callback");
                }
            });

        }

    private void getData(Query query, final MyCallback myCallback) {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("INFO", "Completed Tasks:  " + String.valueOf(dataSnapshot.getChildrenCount()));
                String count = String.valueOf(dataSnapshot.getChildrenCount());
                int newCount = Integer.parseInt(count);
                myCallback.onCallback(newCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(listener);

    }



    private String minusDay(String date) {
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
        c.add(Calendar.DATE, -1);
        date1 = c.getTime();

        newDate = df.format(date1);
        Log.e("TAG", "New Date - " + newDate);
        return newDate;
    }

    private void setActivity(int done, int undone){
        float activity =0;
        activity = (done/(done+undone))*100;
        Log.e("TAG", "Activity - " + activity);
        activityPercent.setText(Float.toString(activity)+"%");
    }



    public interface MyCallback {
        void onCallback(int count);
    }
}



