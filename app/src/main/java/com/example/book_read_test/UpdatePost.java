package com.example.book_read_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.book_read_test.models.Posts;
import com.example.book_read_test.utils.CollectionNames;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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

        Posts p = new Posts(book_new_name , book_new_desc , book_id);
        Map<String, Object> posts = new HashMap<>();
        posts.put("booktitle", book_new_name);
        posts.put("bookdesc", book_new_desc);

        firestore.collection(collectionNames.getPostCollection()).document(p.getDocId())
                .set(posts)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                        startActivity(new Intent(UpdatePost.this , HomePage.class));
                    }
                });
    }
}
