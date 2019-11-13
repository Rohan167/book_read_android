package com.example.book_read_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book_read_test.models.Posts;
import com.example.book_read_test.models.Users;
import com.example.book_read_test.utils.CollectionNames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ProfilePage extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;

    Button save_btn;
    EditText profile_name, profile_password;
    TextView user_text;
    FirebaseAuth firebaseAuth;
    CollectionNames collectionNames;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Task<Uri> imgUploadTask;
    ImageButton profileImgChngBtn;
    String username, uid;
    Uri imgUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        save_btn = findViewById(R.id.save_profile);
        profile_name = findViewById(R.id.profile_name);
        profile_password = findViewById(R.id.profile_password);
        user_text = findViewById(R.id.user_text);
        profileImgChngBtn = findViewById(R.id.profileImgChngBtn);

        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        collectionNames = new CollectionNames();

        getUserData();

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfilePage.this , HomePage.class));
            }
        });


    }

    public void getUserData()
    {
        firestore.collection(collectionNames.getUserCollection()).document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Users user = task.getResult().toObject(Users.class);

                        user_text.setText(user.getUsername());

                    }
                });

    }
}
