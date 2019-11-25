package com.example.book_read_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book_read_test.models.Posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class PostView extends AppCompatActivity {

    TextView id_view , bookname_view , bookdesc_view;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;

    Button del_btn , updt_btn;
    String book_name, book_desc , book_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        book_name = getIntent().getStringExtra("name");
        book_desc = getIntent().getStringExtra("desc");
        book_id = getIntent().getStringExtra("Id");


        firestore = FirebaseFirestore.getInstance();
        id_view = findViewById(R.id.id_view);
        bookname_view = findViewById(R.id.bookname_view);
        bookdesc_view = findViewById(R.id.bookdesc_view);
        del_btn = findViewById(R.id.del_btn);
        updt_btn = findViewById(R.id.updt_btn);

        id_view.setText(book_id);
        bookname_view.setText(book_name);
        bookdesc_view.setText(book_desc);

        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = bookname_view.getText().toString();

                getDocIdDel(value);
            }
        });


        updt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String val = id_view.getText().toString();
                startActivity(new Intent(PostView.this , UpdatePost.class).putExtra("bookid", val));
            }
        });



    }


    public void deleteData(String id) {

        firestore.collection("posts").document(id).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PostView.this, "Deleted DONE", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(PostView.this, HomePage.class));
                    }
                });
    }



    public void getDocIdDel(String value) {
        final String[] itemID = {null};


        firestore.collection("posts").whereEqualTo("booktitle", value).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        itemID[0] = doc.getId();
                    }
                    deleteData(itemID[0]);
                }
            }
        });

    }

}
