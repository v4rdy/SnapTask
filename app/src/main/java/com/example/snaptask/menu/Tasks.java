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
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.snaptask.MainActivity;
import com.example.snaptask.adapters.TasksExpandableListAdapter;
import com.example.snaptask.MainCalendar;
import com.example.snaptask.R;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Tasks extends AppCompatActivity implements MainCalendar.OnDateSelected{

    ExpandableListView expandableListView;
    TasksExpandableListAdapter expandableListAdapter;

    List<String> tasks;
    HashMap<String, List<String>> subtasks;
    HashMap<String, String> priority;
    HashMap<String, String> status;

    private FirebaseDatabase firebasedatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private Button showCalendarBtn;
    private TextView noTasks;
    public String day;
    private DrawerLayout drawer;
    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(tasks.size() > 0) tasks.clear();

            noTasks.setVisibility(View.GONE);
            for(DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                DataSnapshot subtaskSnapshot = taskSnapshot.child("subtasks");
                DataSnapshot prioritySnapshot = taskSnapshot.child("priority");
                DataSnapshot statusSnapshot = taskSnapshot.child("status");
                String taskName = taskSnapshot.getKey();
                System.out.println(taskName);
                tasks.add(taskName);
                List<String> childSubtasks = new ArrayList<String>();
                String mPriority = null;
                String mStatus = null;

                mStatus =  statusSnapshot.getValue(String.class);
                Log.e("TAG", "Status: " + mStatus);

                mPriority = prioritySnapshot.getValue(String.class);
                Log.e("TAG", "Priority: " + mPriority);

                for (DataSnapshot mSubtaskSnapshot : subtaskSnapshot.getChildren()) {
                    String childNames = mSubtaskSnapshot.getValue(String.class);
                    childSubtasks.add(childNames);
                    Log.e("TAG", "Subtasks: " + childSubtasks);

                }
                subtasks.put(taskName, childSubtasks);
                status.put(taskName, mStatus);
                priority.put(taskName, mPriority);

            }
            if (tasks.size()==0) noTasks.setVisibility(View.VISIBLE);
            expandableListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            throw databaseError.toException();

        }
    };

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        showCalendarBtn = findViewById(R.id.show_calendar_btn);
        final NavigationView navigationView = findViewById(R.id.left_nav_view);
        navigationView.bringToFront();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.tasks);
        expandableListView = (ExpandableListView) findViewById(R.id.tasks_list);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final TextView label = (TextView) findViewById(R.id.label);
        noTasks = (TextView) findViewById(R.id.no_tasks);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setCurrentDate();
        fillData();
        expandableListAdapter = new TasksExpandableListAdapter(this, tasks, subtasks, priority, status, day);
        expandableListView.setAdapter(expandableListAdapter);

        showCalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainCalendar mainCalendar = new MainCalendar();
                mainCalendar.show(getSupportFragmentManager(), "mainCalendar");

           }
       }
        );

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.e("TAG", "Item Selected");
                switch (item.getItemId())
                {
                    case R.id.logout_btn:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(Tasks.this, MainActivity.class);
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
                        showCalendarBtn.setVisibility(View.GONE);
                        expandableListView.setVisibility(View.GONE);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.about_btn:
                        Fragment fragment1 = new AboutFragment();
                        FrameLayout fg1 = findViewById(R.id.fragment_container2);
                        fg1.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, fragment1).commit();
                        label.setText(getString(R.string.about));
                        showCalendarBtn.setVisibility(View.GONE);
                        expandableListView.setVisibility(View.GONE);
                        drawer.closeDrawer(GravityCompat.START);

                        break;
                }
                return true;
            }
        });

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.notes:
                        databaseReference.removeEventListener(listener);
                        startActivity(new Intent(getApplicationContext(), Notes.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.tasks:
                        FrameLayout fg = findViewById(R.id.fragment_container2);
                        fg.setVisibility(View.GONE);
                        label.setText(getString(R.string.title_tasks));
                        showCalendarBtn.setVisibility(View.VISIBLE);
                        expandableListView.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.goals:
                        databaseReference.removeEventListener(listener);
                        startActivity(new Intent(getApplicationContext(), Goals.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.statistics:
                        databaseReference.removeEventListener(listener);
                        startActivity(new Intent(getApplicationContext(), Statistics.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.add:
                        databaseReference.removeEventListener(listener);
                        startActivity(new Intent(getApplicationContext(), Add.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

    }

    @Override
    public void sendDate(String date) {
        showCalendarBtn.setText(date);
        day = date;
        fillData();
        expandableListAdapter = new TasksExpandableListAdapter(this, tasks, subtasks, priority, status, day);
        expandableListView.setAdapter(expandableListAdapter);

    }

    public void setCurrentDate(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        day = df.format(currentTime);
        System.out.println(day);
    }

    public void fillData()
    {
        tasks = new ArrayList<>();
        subtasks = new HashMap<>();
        priority = new HashMap<>();
        status = new HashMap<>();

        String uId;
        firebasedatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = firebaseUser.getUid();
        databaseReference = firebasedatabase.getReference("users").child(uId).child("tasks").child(day);

        Query query = databaseReference.orderByChild("priority");
        query.addValueEventListener(listener);

    }



}
