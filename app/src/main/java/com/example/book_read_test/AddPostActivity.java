package com.example.book_read_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.book_read_test.utils.CollectionNames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    CollectionNames collNames;

    EditText bookTitleEditText, bookDescEditText;
    Button bookPostSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        firestore = FirebaseFirestore.getInstance();
        collNames = new CollectionNames();

        bookTitleEditText = findViewById(R.id.bookTitleEditText);
        bookDescEditText = findViewById(R.id.bookDescEditText);
        bookPostSubmitBtn = findViewById(R.id.postBookSubmitBtn);


        bookPostSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String booktitle = bookTitleEditText.getText().toString().trim();
                String bookdesc = bookDescEditText.getText().toString().trim();

                Map<String, Object> posts = new HashMap<>();
                posts.put("booktitle", booktitle);
                posts.put("bookdesc", bookdesc);

                firestore.collection(collNames.getPostCollection()).add(posts)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(AddPostActivity.this, HomePage.class));
                                }
                            }
                        });
            }
        });
    }
}
