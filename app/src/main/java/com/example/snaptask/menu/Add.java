package com.example.snaptask.menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.snaptask.MainActivity;
import com.example.snaptask.navigationViewFragments.AboutFragment;
import com.example.snaptask.navigationViewFragments.ProfileFragment;
import com.example.snaptask.R;
import com.example.snaptask.addFragments.AddGoalsFragment;
import com.example.snaptask.addFragments.AddNotesFragment;
import com.example.snaptask.addFragments.AddTasksFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


public class Add extends AppCompatActivity {

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
        setContentView(R.layout.activity_add);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.add);
        NavigationView navigationView = findViewById(R.id.left_nav_view);
        navigationView.bringToFront();
        final RadioButton notes_button = findViewById(R.id.notes_btn);
        final RadioButton tasks_button = findViewById(R.id.tasks_btn);
        final RadioButton goals_button = findViewById(R.id.goals_btn);
        RadioGroup rgCategory = findViewById(R.id.rg_cat);
        RadioButton rb = findViewById(R.id.choose_day_btn);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        String date = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //The key argument here must match that used in the other activity
            date = extras.getString("DATE_KEY");
        }

        AddTasksFragment frag = new AddTasksFragment();

        Bundle bundle = new Bundle();
        bundle.putString("DATE_KEY", date);
        frag.setArguments(bundle);

        Fragment fragment1 = new AddTasksFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment1).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.e("TAG", "Item Selected");
                switch (item.getItemId())
                {
                    case R.id.logout_btn:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(Add.this, MainActivity.class);
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
                        break;
                    case R.id.about_btn:
                        Fragment fragment1 = new AboutFragment();
                        FrameLayout fg1 = findViewById(R.id.fragment_container2);
                        fg1.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, fragment1).commit();
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
                        startActivity(new Intent(getApplicationContext(), Notes.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.add:
                        return true;
                    case R.id.tasks:
                        startActivity(new Intent(getApplicationContext(), Tasks.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.goals:
                        startActivity(new Intent(getApplicationContext(), Goals.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.statistics:
                        startActivity(new Intent(getApplicationContext(), Statistics.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        rgCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Fragment fragment = new AddTasksFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                if(tasks_button.isChecked()) {
                    tasks_button.setTextColor(getResources().getColor(R.color.white));
                    notes_button.setTextColor(getResources().getColor(R.color.blue));
                    goals_button.setTextColor(getResources().getColor(R.color.blue));

                    tasks_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            System.out.println(user.getUid());
                            Fragment fragment = new AddTasksFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                        }
                    });
                }
                if(notes_button.isChecked()) {
                    tasks_button.setTextColor(getResources().getColor(R.color.blue));
                    notes_button.setTextColor(getResources().getColor(R.color.white));
                    goals_button.setTextColor(getResources().getColor(R.color.blue));

                    notes_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Fragment fragment = new AddNotesFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                        }
                    });
                }
                if(goals_button.isChecked()) {
                    tasks_button.setTextColor(getResources().getColor(R.color.blue));
                    notes_button.setTextColor(getResources().getColor(R.color.blue));
                    goals_button.setTextColor(getResources().getColor(R.color.white));

                    goals_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Fragment fragment = new AddGoalsFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                        }
                    });
                }



            }
        });

    }

}
