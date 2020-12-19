package com.example.snaptask.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.example.snaptask.adapters.GoalsExpandableListAdapter;
import com.example.snaptask.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Goals extends AppCompatActivity {

    ExpandableListView expandableListView;
    GoalsExpandableListAdapter expandableListAdapter;

    List<String> goals;
    HashMap<String, List<String>> steps;
    HashMap<String, String> status;

    private FirebaseDatabase firebasedatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private DrawerLayout drawer;

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
        setContentView(R.layout.activity_goals);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.goals);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        expandableListView = (ExpandableListView) findViewById(R.id.goals_list);


        fillData();

        expandableListAdapter = new GoalsExpandableListAdapter(this, goals, steps, status);
        expandableListView.setAdapter(expandableListAdapter);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.notes:
                        startActivity(new Intent(getApplicationContext(), Notes.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.tasks:
                        startActivity(new Intent(getApplicationContext(), Tasks.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.goals:
                        return true;
                    case R.id.statistics:
                        startActivity(new Intent(getApplicationContext(), Statistics.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.add:
                        startActivity(new Intent(getApplicationContext(), Add.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
    }

    public void fillData()
    {
        goals = new ArrayList<>();
        steps = new HashMap<>();
        status = new HashMap<>();

        String uId;
        firebasedatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = firebaseUser.getUid();
        databaseReference = firebasedatabase.getReference("users").child(uId).child("goals");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(goals.size() > 0) goals.clear();

                for(DataSnapshot goalSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot stepSnapshot = goalSnapshot.child("steps");
                    DataSnapshot statusSnapshot = goalSnapshot.child("status");
                    String goalName = goalSnapshot.getKey();
                    Log.e("TAG", "Goals: " + goalName);
                    goals.add(goalName);
                    List<String> childSteps = new ArrayList<String>();
                    String mStatus = null;

                    for (DataSnapshot mStepSnapshot : stepSnapshot.getChildren()) {
                        String childNames = mStepSnapshot.getValue(String.class);
                        childSteps.add(childNames);
                        Log.e("TAG", "Steps: " + childSteps);

                        mStatus =  statusSnapshot.getValue(String.class);
                        Log.e("TAG", "Status: " + mStatus);

                    }
                    steps.put(goalName, childSteps);
                    status.put(goalName, mStatus);

                }
                expandableListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();

            }
        });

    }
}
