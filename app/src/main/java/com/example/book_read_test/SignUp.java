package com.example.book_read_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book_read_test.models.Users;
import com.example.book_read_test.utils.CollectionNames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SignUp extends AppCompatActivity {
    EditText username_signup;
    EditText email_signup;
    EditText password_signup;
    Button sign_up;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    CollectionNames collectionNames;
    Users users;
    TextView usernameErrMsgTV;
    ImageView usernameValidImageView;

    List<String> allUsernames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        collectionNames = new CollectionNames();
        users = new Users();
        allUsernames = new ArrayList<>();

        username_signup = findViewById(R.id.username_signup);
        email_signup = findViewById(R.id.email_signup);
        password_signup = findViewById(R.id.password_signup);
        sign_up = findViewById(R.id.register_button);
        usernameErrMsgTV = findViewById(R.id.usernameErrMsgTV);
        usernameValidImageView = findViewById(R.id.usernameValidImageView);

        firestore.collection(CollectionNames.USERS).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult() != null && task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                allUsernames.add(doc.getString(Users.USERNAME));
                            }
                        }
                    }
                });

//        username_signup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (b) {
//                    //DoNothing
//                } else {
                    firestore.collection(CollectionNames.USERS).
                            whereEqualTo(users.USERNAME, username_signup.getText().toString().trim()).
                            get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            List<DocumentChange> docChanges = task.getResult().getDocumentChanges();

                            if (task.isSuccessful() && task.getResult() != null) {
                                Toast.makeText(SignUp.this, "username set", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SignUp.this, "username not set", Toast.LENGTH_LONG).show();
                            }

                        }
        });



        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = username_signup.getText().toString().trim();
                String email = email_signup.getText().toString().trim();
                String password = password_signup.getText().toString().trim();

                createNewUser(username , email , password);

            }
        });


        username_signup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() < 1) {
                    usernameValidImageView.setVisibility(View.GONE);
                }

                if (allUsernames.contains(editable.toString())) {
                    usernameErrMsgTV.setVisibility(View.VISIBLE);
                    usernameErrMsgTV.setText(editable.toString() + " already exists!");
                    usernameValidImageView.setVisibility(View.GONE);
                }
                else {
                    usernameErrMsgTV.setVisibility(View.GONE);
                    usernameErrMsgTV.setText("");
                    usernameValidImageView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void createNewUser(final String username, final String email, final String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("SIGNUP : ", "createUserWithEmailAndPassword SUCCESS USER_ID : " + task.getResult().getUser().getUid());


                            // setting the default value at user register/signup
                            Users users = new Users(
                                    username,
                                    email,
                                    password,
                                    null,
                                    new ArrayList<String>()
                            );

                            firestore.collection(CollectionNames.USERS)
                                    .document(task.getResult().getUser().getUid())
                                    .set(users)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            sign_up.setVisibility(View.VISIBLE);
                                            startActivity(new Intent(SignUp.this , HomePage.class));
                                            finish();
                                        }
                                    });




                        } else {
                            Log.d("SIGNUP: ", "ERROR in createUserWithEmailAndPassword", task.getException());
                        }

                    }

//    public void signupUser(String username , String email , String password)
//    {
//        firebaseAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                if (task.isSuccessful())
//                {
//                    startActivity(new Intent(SignUp.this , HomePage.class));
//                    finish();
//                }
//                else
//                    {
//                        Toast.makeText(SignUp.this, "This is embarassing", Toast.LENGTH_LONG).show();
//                        Log.d("Login error ", String.valueOf(task.getResult()));
//                }
//
//
//
//            }
//        });

                });
    }
}
