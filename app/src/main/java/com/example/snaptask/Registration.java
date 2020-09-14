package com.example.snaptask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Registration extends AppCompatActivity {

    private EditText name, email, password, conf_password;
    private Button sing_up, to_login;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation);
        name = findViewById(R.id.editName);
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        conf_password = findViewById(R.id.editConfimPassword);
        sing_up = findViewById(R.id.signup_btn);
        to_login = findViewById(R.id.to_login_btn);

        addListenerOnButton();
        //registration();

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
    }

    public void addListenerOnButton (){
        to_login = findViewById(R.id.to_login_btn);
        to_login.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }
        );
        sing_up = findViewById(R.id.signup_btn);
        User user = new User();
        sing_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_reg = name.getText().toString();
                String email_reg = email.getText().toString();
                String pass = password.getText().toString();
                String conf_pass = conf_password.getText().toString();

                if(name_reg.isEmpty()){
                    name.setError("Please enter Name");
                    name.requestFocus();
                }
                else if(email_reg.isEmpty()){
                    email.setError("Please enter Email");
                    email.requestFocus();
                }
                else if(pass.isEmpty() || password.getText().toString().length() < 6){
                    password.setError("Password must have more than 6 symbols");
                    password.requestFocus();
                }
                else if(conf_pass.isEmpty()){
                    conf_password.setError("Passwords do not match");
                    conf_password.requestFocus();
                }
                else
                    auth.createUserWithEmailAndPassword(email_reg, pass).
                            addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Sign up successful", Toast.LENGTH_SHORT);
                                        toast.show();
                                        Intent intent2 = new Intent("com.example.snaptask.menu.Add");
                                        startActivity(intent2);

                                    }
                                }
                            });
            }
        });
    }




}
