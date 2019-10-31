package com.example.book_read_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.book_read_test.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class ProfilePage extends AppCompatActivity {

    Button save_btn;
    EditText profile_name , profile_password;
    TextView user_text;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        save_btn = findViewById(R.id.save_profile);
        profile_name = findViewById(R.id.profile_name);
        profile_password = findViewById(R.id.profile_password);
        user_text = findViewById(R.id.user_text);

        Users user = new Users();

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ProfilePage.this, HomePage.class));
            }
        });

    }
}
