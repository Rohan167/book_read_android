package com.example.book_read_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class PostView extends AppCompatActivity {

    TextView id_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        Toast.makeText(PostView.this , getIntent().getStringExtra("Id"), Toast.LENGTH_LONG).show();

        String val = getIntent().getStringExtra("Id");

        id_view =findViewById(R.id.id_view);
        id_view.setText(val);

    }
}
