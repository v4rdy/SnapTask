package com.example.snaptask.addFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.snaptask.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddNotesFragment extends Fragment {

    EditText noteTitle, noteText;
    Button applyBtn;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_notes_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noteTitle = (EditText) view.findViewById(R.id.note_title);
        noteText = (EditText) view.findViewById(R.id.note_text);
        applyBtn = (Button) view.findViewById(R.id.apply_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeNote();
            }
        });
    }

    public void writeNote(){
        String Title, Text, day;
        Title = noteTitle.getText().toString();
        Text = noteText.getText().toString();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        day = df.format(currentTime);

        if(Title.isEmpty())  Toast.makeText(getActivity(), "Please write a Note name!", Toast.LENGTH_SHORT).show();
        else if(Text.isEmpty())  Toast.makeText(getActivity(), "Please write a note text!", Toast.LENGTH_SHORT).show();
        else{
            database.child("users").child(firebaseUser.getUid()).child("notes").child(Title).child("text").setValue(Text);
            database.child("users").child(firebaseUser.getUid()).child("notes").child(Title).child("date").setValue(day);
        }
    }

}
