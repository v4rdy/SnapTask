package com.example.snaptask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.snaptask.menu.Add;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText email, password;
    private Button sing_in, to_reg;
    FirebaseAuth auth;
    FirebaseDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password = findViewById(R.id.editPassword);
        email = findViewById(R.id.editEmail);
        sing_in = findViewById(R.id.signin_btn);
        to_reg = findViewById(R.id.to_reg_btn);


        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        addListenerOnButton();
    }

    public void addListenerOnButton (){
        password = findViewById(R.id.editPassword);
        email = findViewById(R.id.editEmail);
        sing_in = findViewById(R.id.signin_btn);
        to_reg = findViewById(R.id.to_reg_btn);
        to_reg.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, Registration.class);
                        startActivity(intent);
                    }
                }
        );

        sing_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_log = email.getText().toString();
                String pass_log = password.getText().toString();

                 if(email_log.isEmpty()){
                    email.setError("Please enter Email");
                    email.requestFocus();
                }
                else if(pass_log.isEmpty() || password.getText().toString().length() < 6){
                    password.setError("Password must have more than 6 symbols");
                    password.requestFocus();
                }
                else
                    auth.signInWithEmailAndPassword(email_log, pass_log).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Sign in was successful", Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(MainActivity.this, Add.class);
                                startActivity(intent2);
                                }
                            else
                                Toast.makeText(getApplicationContext(), "Sign in was unsuccessful", Toast.LENGTH_SHORT).show();


                        }
                    });
            }
        });


    }

}
