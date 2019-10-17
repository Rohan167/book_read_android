package com.example.book_read_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class LoginPage extends AppCompatActivity {
    Button sign_up;
    EditText username_login;
    EditText password_login;
    Button login_btn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            startActivity(new Intent(LoginPage.this , HomePage.class));
            finish();
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        sign_up = findViewById(R.id.signup_btn);
        username_login = findViewById(R.id.username_login);
        password_login = findViewById(R.id.password_login);
        login_btn = findViewById(R.id.login_btn);
        firebaseAuth = FirebaseAuth.getInstance();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = username_login.getText().toString().trim();
                String password = password_login.getText().toString().trim();

                Log.d("", email);
                Log.d("", password);


                if (email.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(LoginPage.this , "Please enter the Credentials", Toast.LENGTH_LONG).show();
                }
                else
                {
                    loginUser(email , password);
                }



            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_up_btn = new Intent(LoginPage.this , SignUp.class);
                startActivity(sign_up_btn);

            }
        });

    }

    public void loginUser(String email , String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    startActivity(new Intent(LoginPage.this , HomePage.class));
                    finish();
                }
                else
                {
                    Toast.makeText(LoginPage.this, "This is embarassing", Toast.LENGTH_LONG).show();
                    Log.d("Login error ", String.valueOf(task.getResult()));
                }

            }
        });
    }
}
