package com.example.book_read_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book_read_test.models.Posts;
import com.example.book_read_test.models.Users;
import com.example.book_read_test.utils.CollectionNames;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ProfilePage extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;

    Button save_btn;
    EditText profile_name, profile_password , profile_email;
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
    ImageView user_img;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        save_btn = findViewById(R.id.save_profile);
        profile_name = findViewById(R.id.profile_name);
        profile_password = findViewById(R.id.profile_password);
        profile_email = findViewById(R.id.email_nav_text);
        user_text = findViewById(R.id.user_text);
        profileImgChngBtn = findViewById(R.id.profileImgChngBtn);
        user_img = findViewById(R.id.user_img);

        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("userAvatars");
        collectionNames = new CollectionNames();

        getUserData();


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Users user = new Users();

                String profile_nm = profile_name.getText().toString().trim();
                String profile_pass = profile_password.getText().toString().trim();
                String profile_mail = profile_email.getText().toString().trim();

                if (profile_mail.isEmpty() && profile_nm.isEmpty() && profile_pass.isEmpty() && imgUri == null) {
                    Toast.makeText(ProfilePage.this , "ENter any one Cred" , Toast.LENGTH_LONG).show();
                    return;
                }

                user.setUsername(profile_nm);
                user.setPassword(profile_pass);
                user.setEmail(profile_mail);

                storeProfileImageToFirestore(user);
            }
        });


        profileImgChngBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
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

                        if (user.getUser_image() != null) {
                            Picasso.get().load(user.getUser_image()).into(user_img);
                        }

                    }
                });

    }


    public String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null) {

            imgUri = data.getData();

            Picasso.get().load(imgUri).into(user_img);
        }
    }

    public void storeProfileImageToFirestore(final Users user) {
        final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imgUri));

        fileRef.putFile(imgUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) throw task.getException();
                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downloadedUri = task.getResult();

                user.setUser_image(downloadedUri.toString());

                firestore.collection(collectionNames.getUserCollection()).document(firebaseUser.getUid())
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                                startActivity(new Intent(ProfilePage.this , HomePage.class));
                            }
                        });
            }
        });
    }
}
