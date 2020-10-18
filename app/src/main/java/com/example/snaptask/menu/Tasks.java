package com.example.snaptask.menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.snaptask.CustomExpandableListAdapter;
import com.example.snaptask.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class Tasks extends AppCompatActivity {

    ExpandableListView expandableListView;
    CustomExpandableListAdapter expandableListAdapter;

    List<String> tasks;
    HashMap<String, List<String>> subtasks;

    private FirebaseDatabase firebasedatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.tasks);
        expandableListView = (ExpandableListView) findViewById(R.id.tasks_list);

        fillData();
        System.out.println(tasks);
        System.out.println(subtasks);

        expandableListAdapter = new CustomExpandableListAdapter(this, tasks, subtasks);
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
                        return true;
                    case R.id.goals:
                        startActivity(new Intent(getApplicationContext(), Goals.class));
                        overridePendingTransition(0,0);
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
        tasks = new ArrayList<>();
        subtasks = new HashMap<>();
        firebasedatabase = FirebaseDatabase.getInstance();
        databaseReference = firebasedatabase.getReference("Tasks");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(tasks.size() > 0) tasks.clear();
                ArrayList<String> childSubtasks;
                for(DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot subtaskSnapshot = taskSnapshot.child("Subtasks");
                    String taskName = taskSnapshot.getKey();
                    System.out.println(taskName);
                    tasks.add(taskName);
                    childSubtasks = new ArrayList<String>();
                    for (DataSnapshot subtasksSnapshot : subtaskSnapshot.getChildren()) {

                        String childNames = subtasksSnapshot.getValue(String.class);
                        System.out.println(childNames);
                        childSubtasks.add(childNames);
                        Log.e("TAG", "Subtasks :" + childSubtasks);
                    }
                    subtasks.put(taskName, childSubtasks);
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
