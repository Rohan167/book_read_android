package com.example.book_read_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.book_read_test.models.Posts;
import com.example.book_read_test.utils.CollectionNames;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    CollectionNames collNames;

    static final int PICK_IMAGE_REQUEST = 1;
    Uri imgUri;

    EditText bookTitleEditText, bookDescEditText;
    Button bookPostSubmitBtn, selectImageViewBtn;
    ImageView bookImageView;
    ProgressBar bookSubmitProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getInstance().getReference("bookUploads");
        collNames = new CollectionNames();

        bookTitleEditText = findViewById(R.id.bookTitleEditText);
        bookDescEditText = findViewById(R.id.bookDescEditText);
        bookPostSubmitBtn = findViewById(R.id.postBookSubmitBtn);
        bookImageView = findViewById(R.id.bookImageView);
        selectImageViewBtn = findViewById( R.id.selectBookImageBtn);
        bookSubmitProgressBar = findViewById(R.id.bookSubmitProgressBar);

        bookPostSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String booktitle = bookTitleEditText.getText().toString().trim();
                String bookdesc = bookDescEditText.getText().toString().trim();

                bookPostSubmitBtn.setVisibility(View.GONE);
                bookSubmitProgressBar.setVisibility(View.VISIBLE);

                storeBookImage(booktitle, bookdesc, FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
        });

        selectImageViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null) {
            imgUri = data.getData();

            bookImageView.setVisibility(View.VISIBLE);

            Picasso.get().load(imgUri).into(bookImageView);
        }
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


    public void storeBookImage(String booktitle, String bookdesc, final String userid) {
        final Posts post = new Posts();
        post.setBookName(booktitle);
        post.setBookDescription(bookdesc);
        post.setUserId(userid);

        if (imgUri != null) {
            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imgUri));

            fileRef.putFile(imgUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) throw task.getException();
                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();

                                post.setPostImage(downloadUri.toString());

                                insertPostToDatabase(post);
                            }
                        }
                    });
        }
        else {
            insertPostToDatabase(post);
        }
    }


    public void insertPostToDatabase(Posts post) {
        firestore.collection(collNames.getPostCollection())
                .add(post)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        bookPostSubmitBtn.setVisibility(View.VISIBLE);
                        bookSubmitProgressBar.setVisibility(View.GONE);
                        finish();
                        startActivity(new Intent(AddPostActivity.this, HomePage.class));
                    }
                });
    }
}
