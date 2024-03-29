package com.example.book_read_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book_read_test.models.Posts;
import com.example.book_read_test.models.Users;
import com.example.book_read_test.utils.CollectionNames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostView extends AppCompatActivity {

    TextView id_view , bookname_view , bookdesc_view;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    CollectionNames collectionNames;

    Button del_btn , updt_btn;
    String book_name, book_desc , book_id, post_img;
    ImageView book_image;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        book_name = getIntent().getStringExtra("name");
        book_desc = getIntent().getStringExtra("desc");
        book_id = getIntent().getStringExtra("Id");
        post_img = getIntent().getStringExtra("book_image");
        uid = getIntent().getStringExtra("postUserId");
        book_image = findViewById(R.id.book_image_view);



        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        collectionNames = new CollectionNames();
        id_view = findViewById(R.id.id_view);
        bookname_view = findViewById(R.id.bookname_view);
        bookdesc_view = findViewById(R.id.bookdesc_view);
        del_btn = findViewById(R.id.del_btn);
        updt_btn = findViewById(R.id.updt_btn);

        id_view.setText(book_id);
        bookname_view.setText(book_name);
        bookdesc_view.setText(book_desc);

        if (post_img != null) {
            Picasso.get().load(post_img).into(book_image);
        }

        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData(book_id);
            }
        });

        final Posts posts = new Posts();

        String logged_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        Log.d("POST_VIEW", "post uid : " + uid + " loggedin : " + logged_id);

        if (!logged_id.equals(uid))
        {
            Log.d("POST_VEIW", updt_btn.isEnabled() + " post UID: " + uid);
            updt_btn.setVisibility(View.GONE);
            del_btn.setVisibility(View.GONE);
        }


        updt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String val = id_view.getText().toString();
                startActivity(new Intent(PostView.this , UpdatePost.class).putExtra("bookid", val));
            }
        });





    }

//    public void getImage() {
//        firestore.collection(CollectionNames.POSTS).document(book_id)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        Posts post = task.getResult().toObject(Posts.class);
//
//
//                        if (post.getPostImage() != null)
//                            Picasso.get().load(post.getPostImage()).into(book_image);
//
//                    }
//                });
//    }


    public void deleteData(String id) {
//        Posts posts = new Posts();
        Log.d("POST_VIEW", "deleted id : " + id);
        firestore.collection(CollectionNames.POSTS).document(id).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PostView.this, "Deleted DONE", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(PostView.this, HomePage.class));
                        } else {
                            Log.d("POST_VIEW", task.getException().getMessage());
                        }
                    }
                });
    }



//    public void getDocIdDel(String value) {
//        final String[] itemID = {null};
//
//
//        firestore.collection("posts").whereEqualTo("bookName", value).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot doc : task.getResult()) {
//                        itemID[0] = doc.getId();
//                    }
//                    deleteData(itemID[0]);
//                }
//            }
//        });
//
//    }

}
