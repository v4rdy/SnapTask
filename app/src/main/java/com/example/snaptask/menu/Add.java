package com.example.snaptask.menu;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.snaptask.R;
import com.example.snaptask.fragments.AddGoalsFragment;
import com.example.snaptask.fragments.AddNotesFragment;
import com.example.snaptask.fragments.AddTasksFragment;
import com.example.snaptask.menu.Goals;
import com.example.snaptask.menu.Notes;
import com.example.snaptask.menu.Statistics;
import com.example.snaptask.menu.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class Add extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.add);
        Button notes_button = findViewById(R.id.notes_btn);
        Button tasks_button = findViewById(R.id.tasks_btn);
        Button goals_button = findViewById(R.id.goals_btn);
        RadioButton rb = findViewById(R.id.choose_day_btn);


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

        Fragment fragment = new AddTasksFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

        tasks_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddTasksFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });

        notes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddNotesFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });

        goals_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddGoalsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });
//


    }

}
