package com.example.snaptask.navigationViewFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snaptask.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;


public class ProfileFragment extends Fragment {
    private LinearLayout changePassLay;
    private Button changePassword, confirmChange;
    private TextView nameText, emailText;
    private EditText newPass, repNewPass;
    public String Email, Password;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference database;
    FirebaseDatabase firebasedatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changePassLay = view.findViewById(R.id.change_password_layout);
        changePassword = view.findViewById(R.id.change_password_btn);
        confirmChange = view.findViewById(R.id.confirm_change_btn);
        nameText = view.findViewById(R.id.name_text);
        emailText = view.findViewById(R.id.email_text);
        newPass = view.findViewById(R.id.new_password);
        repNewPass = view.findViewById(R.id.repeat_new_password);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        firebasedatabase = FirebaseDatabase.getInstance();

        fillProfile();

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword.setVisibility(View.GONE);
                changePassLay.setVisibility(View.VISIBLE);
            }
        });

        confirmChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String np = newPass.getText().toString();
                String rnp = repNewPass.getText().toString();
                if (np.isEmpty() || np.length() < 6) {
                    newPass.setError("Password must have more than 6 symbols");
                    newPass.requestFocus();
                } else if (rnp.isEmpty() || !np.equals(rnp)) {
                    newPass.setError("Passwords do not match");
                    newPass.requestFocus();
                } else {
                    AuthCredential credential = EmailAuthProvider.getCredential(Email, Password);
                                firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseUser.updatePassword(newPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Password was updated", Toast.LENGTH_SHORT).show();
                                        database = firebasedatabase.getReference("users").child(firebaseUser.getUid()).child("profile").child("password");
                                        database.setValue(newPass.getText().toString());
                                        newPass.setText(null);
                                        repNewPass.setText(null);
                                    } else {
                                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Log.d(TAG, "Error auth failed");
                        }
                    }

                });
            }
        }
    });
    }
        private void fillProfile () {
            database = firebasedatabase.getReference("users").child(firebaseUser.getUid()).child("profile");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    Log.e(TAG, "name - " + name);
                    Email = dataSnapshot.child("email").getValue(String.class);
                    Log.e(TAG, "name - " + Email);
                    Password = dataSnapshot.child("password").getValue(String.class);
                    nameText.setText(name);
                    emailText.setText(Email);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(TAG, "onCancelled", databaseError.toException());
                }
            });
        }
}