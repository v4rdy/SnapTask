package com.example.snaptask.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.snaptask.MainActivity;
import com.example.snaptask.helpers.Note;
import com.example.snaptask.R;
import com.example.snaptask.adapters.NotesRecyclerViewAdapter;
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

        final NavigationView navigationView = findViewById(R.id.left_nav_view);
        navigationView.bringToFront();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rec_view);
        bottomNav.setSelectedItemId(R.id.notes);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final TextView label = (TextView) findViewById(R.id.label);
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.e("TAG", "Item Selected");
                switch (item.getItemId())
                {
                    case R.id.logout_btn:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(Notes.this, MainActivity.class);
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
                        recyclerView.setVisibility(View.GONE);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.about_btn:
                        Fragment fragment1 = new AboutFragment();
                        FrameLayout fg1 = findViewById(R.id.fragment_container2);
                        fg1.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, fragment1).commit();
                        label.setText(getString(R.string.about));
                        recyclerView.setVisibility(View.GONE);
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
                        FrameLayout fg = findViewById(R.id.fragment_container2);
                        label.setText(getString(R.string.title_notes));
                        recyclerView.setVisibility(View.VISIBLE);
                        fg.setVisibility(View.GONE);

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
