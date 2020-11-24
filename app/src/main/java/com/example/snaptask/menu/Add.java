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
import android.widget.RadioGroup;

import com.example.snaptask.MainActivity;
import com.example.snaptask.R;
import com.example.snaptask.fragments.AddGoalsFragment;
import com.example.snaptask.fragments.AddNotesFragment;
import com.example.snaptask.fragments.AddTasksFragment;
import com.example.snaptask.menu.Goals;
import com.example.snaptask.menu.Notes;
import com.example.snaptask.menu.Statistics;
import com.example.snaptask.menu.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        final RadioButton notes_button = findViewById(R.id.notes_btn);
        final RadioButton tasks_button = findViewById(R.id.tasks_btn);
        final RadioButton goals_button = findViewById(R.id.goals_btn);
        RadioGroup rgCategory = findViewById(R.id.rg_cat);
        Button logOut = findViewById(R.id.logout);
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

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Add.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


    }

}
