package com.example.book_read_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.book_read_test.models.Posts;
import com.example.book_read_test.utils.CollectionNames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

public class UpdatePost extends AppCompatActivity {

    EditText bookNameUpdate , bookDescUpdate;
    Button updatePostBtn;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    CollectionNames collectionNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);

        final String book_id= getIntent().getStringExtra("bookid");

        bookDescUpdate = findViewById(R.id.bookDescUpdate);
        bookNameUpdate = findViewById(R.id.bookTitleUpdate);
        updatePostBtn = findViewById(R.id.updatePostBtn);

        collectionNames  = new CollectionNames();

        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        updatePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String book_new_name = bookNameUpdate.getText().toString().trim();
                String book_new_desc = bookDescUpdate.getText().toString().trim();
                updatePost(book_id , book_new_name , book_new_desc);
            }
        });



    }

    private void updatePost(String book_id , String book_new_name , String book_new_desc) {

        WriteBatch batch = firestore.batch();

        DocumentReference docRef = firestore.collection(collectionNames.getPostCollection()).document(book_id);

        batch.update(docRef, "booktitle", book_new_name);
        batch.update(docRef, "bookdesc", book_new_desc);

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(UpdatePost.this , HomePage.class));
            }
        });

    }
}
