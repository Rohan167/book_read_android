package com.example.book_read_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    EditText username_signup;
    EditText email_signup;
    EditText password_signup;
    Button sign_up;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();

        username_signup = findViewById(R.id.username_signup);
        email_signup = findViewById(R.id.email_signup);
        password_signup = findViewById(R.id.password_signup);
        sign_up = findViewById(R.id.register_button);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = username_signup.getText().toString().trim();
                String email = email_signup.getText().toString().trim();
                String password = password_signup.getText().toString().trim();

                signupUser(username , email , password);

            }
        });
    }

    public void signupUser(String username , String email , String password)
    {
        firebaseAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    startActivity(new Intent(SignUp.this , HomePage.class));
                    finish();
                }
                else
                    {
                        Toast.makeText(SignUp.this, "This is embarassing", Toast.LENGTH_LONG).show();
                        Log.d("Login error ", String.valueOf(task.getResult()));
                }



            }
        });

    }
}
