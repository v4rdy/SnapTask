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
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.snaptask.MainActivity;
import com.example.snaptask.adapters.GoalsExpandableListAdapter;
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
    private TextView noGoals;
    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if (goals.size() > 0) goals.clear();
            noGoals.setVisibility(View.GONE);

            for (DataSnapshot goalSnapshot : dataSnapshot.getChildren()) {
                DataSnapshot stepSnapshot = goalSnapshot.child("steps");
                DataSnapshot statusSnapshot = goalSnapshot.child("status");
                DataSnapshot deleteStatusSnapshot = goalSnapshot.child("delete_status");
                String goalName = goalSnapshot.getKey();
                Log.e("TAG", "Goals: " + goalName);

                List<String> childSteps = new ArrayList<String>();
                String mStatus = null;
                String mDeleteStatus = null;

                mStatus = statusSnapshot.getValue(String.class);
                Log.e("TAG", "Status: " + mStatus);

                mDeleteStatus = deleteStatusSnapshot.getValue(String.class);
                Log.e("TAG", "Delete Status: " + mDeleteStatus);

                for (DataSnapshot mStepSnapshot : stepSnapshot.getChildren()) {
                    String childNames = mStepSnapshot.getValue(String.class);
                    childSteps.add(childNames);
                    Log.e("TAG", "Steps: " + childSteps);
                }

                if (mDeleteStatus.equals("false")) {
                    goals.add(goalName);
                    steps.put(goalName, childSteps);
                    status.put(goalName, mStatus);
                }

            }
            if(goals.size()==0) noGoals.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.activity_goals);

        final NavigationView navigationView = findViewById(R.id.left_nav_view);
        navigationView.bringToFront();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.goals);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        final TextView label = (TextView) findViewById(R.id.label);
        noGoals = (TextView) findViewById(R.id.no_goals);
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.e("TAG", "Item Selected");
                switch (item.getItemId())
                {
                    case R.id.logout_btn:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        Intent intent = new Intent(Goals.this, MainActivity.class);
                        startActivity(intent);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        break;
                    case R.id.profile_btn:
                        System.out.println("profile");
                        Fragment fragment = new ProfileFragment();
                        FrameLayout fg = findViewById(R.id.fragment_container2);
                        fg.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, fragment).commit();
                        label.setText(getString(R.string.profile));
                        expandableListView.setVisibility(View.GONE);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.about_btn:
                        Fragment fragment1 = new AboutFragment();
                        FrameLayout fg1 = findViewById(R.id.fragment_container2);
                        fg1.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, fragment1).commit();
                        label.setText(getString(R.string.about));
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
                        finish();
                        startActivity(new Intent(getApplicationContext(), Notes.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.tasks:
                        databaseReference.removeEventListener(listener);
                        finish();
                        startActivity(new Intent(getApplicationContext(), Tasks.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.goals:
                        FrameLayout fg = findViewById(R.id.fragment_container2);
                        fg.setVisibility(View.GONE);
                        label.setText(getString(R.string.title_goals));
                        expandableListView.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.statistics:
                        databaseReference.removeEventListener(listener);
                        finish();
                        startActivity(new Intent(getApplicationContext(), Statistics.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.add:
                        databaseReference.removeEventListener(listener);
                        finish();
                        startActivity(new Intent(getApplicationContext(), Add.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
    }

    public void fillData() {
        goals = new ArrayList<>();
        steps = new HashMap<>();
        status = new HashMap<>();

        String uId;
        firebasedatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = firebaseUser.getUid();
        databaseReference = firebasedatabase.getReference("users").child(uId).child("goals");


        databaseReference.addValueEventListener(listener);

    }
}
