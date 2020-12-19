package com.example.snaptask.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.snaptask.helpers.Note;
import com.example.snaptask.R;
import com.example.snaptask.adapters.NotesRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Notes extends AppCompatActivity {


    private List<Note> noteList = new ArrayList<>();
    NotesRecyclerViewAdapter notesRecyclerViewAdapter;
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
        setContentView(R.layout.activity_notes);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rec_view);
        bottomNav.setSelectedItemId(R.id.notes);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        fillData();
        notesRecyclerViewAdapter = new NotesRecyclerViewAdapter(this, noteList);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(notesRecyclerViewAdapter);
        Log.e("TAG", "Recycler view adapter is started");
        registerForContextMenu(recyclerView);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.notes:
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
                        startActivity(new Intent(getApplicationContext(), Statistics.class));
                        overridePendingTransition(0, 0);
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


    public void fillData() {
        String uId;
        firebasedatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = firebaseUser.getUid();
        databaseReference = firebasedatabase.getReference("users").child(uId).child("notes");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(noteList.size() > 0) noteList.clear();
                for (DataSnapshot notesSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot textSnapshot = notesSnapshot.child("text");
                    DataSnapshot dateSnapshot = notesSnapshot.child("date");
                    String ntName = notesSnapshot.getKey();
                    Log.e("TAG", "Note name - " + ntName);

                    String ntText = textSnapshot.getValue(String.class);
                    Log.e("TAG", "Note text - " + ntText);

                    String ntDate = dateSnapshot.getValue(String.class);
                    Log.e("TAG", "Note date - " + ntDate);

                    Note note = new Note(ntName, ntText, ntDate);
                    noteList.add(note);
                }
                notesRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case 1:
                notesRecyclerViewAdapter.deleteItem(item.getGroupId());
                return true;

            default:
               return super.onContextItemSelected(item);
        }

        }
}
