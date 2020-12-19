package com.example.snaptask;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snaptask.menu.Notes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditNote extends AppCompatActivity {

    private Button goBack, applyChanges, deleteNote;
    private EditText noteTitle, noteText;
    private String Title, Text, Date;

    private FirebaseDatabase firebasedatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        goBack = (Button) findViewById(R.id.go_back);
        applyChanges = (Button) findViewById(R.id.apply_changes);
        deleteNote = (Button) findViewById(R.id.delete_note);
        noteTitle = (EditText) findViewById(R.id.note_title);
        noteText = (EditText) findViewById(R.id.note_text);

        firebasedatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = firebasedatabase.getReference("users").child(firebaseUser.getUid()).child("notes");

        Intent intent = getIntent();
        Title = intent.getExtras().getString("Title");
        Text = intent.getExtras().getString("Text");
        Date = intent.getExtras().getString("Date");

        noteTitle.setText(Title);
        noteText.setText(Text);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateUpdate();
            }
        });

        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteDialog();
            }
        });

    }

    private void dateUpdate(){
        String newTitle, newText, day;
        newTitle = noteTitle.getText().toString();
        newText = noteText.getText().toString();

        java.util.Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        day = df.format(currentTime);


        if(Title.equals(newTitle) && Text.equals(newText)){
            finish();
        }
        else{

            databaseReference.child(Title).removeValue();
            databaseReference.child(newTitle).child("text").setValue(newText);
            databaseReference.child(newTitle).child("date").setValue(day);
            finish();
        }
    }

    private void onDeleteDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Do you want to delete this note?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        databaseReference.child(Title).removeValue();
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}



