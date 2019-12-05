package com.example.book_read_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.book_read_test.models.Posts;
import com.example.book_read_test.models.Users;
import com.example.book_read_test.utils.CollectionNames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProfileActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    List<Posts> postsList;
    PostAdapter postAdapter;

    Toolbar toolbar;

    LinearLayout profileFavCountWrapperLL;
    ImageView profileUserAvatarImageView;
    ProgressBar profilePostsProgressBar;
    TextView profileUsernameTV, profileEmailTV, profilePostsPlaceholderTV, profileFavPostCountTV;
    RecyclerView profilePostsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firestore = FirebaseFirestore.getInstance();
        postsList = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");

        profileFavCountWrapperLL = findViewById(R.id.profileFavCountWrapperLL);
        profileFavPostCountTV = findViewById(R.id.profileFavPostCountTV);
        profileUserAvatarImageView = findViewById(R.id.profileUserAvatarImageView);
        profilePostsProgressBar = findViewById(R.id.profilePostsProgressBar);
        profileUsernameTV = findViewById(R.id.profileUsernameTV);
        profilePostsPlaceholderTV = findViewById(R.id.profilePostsPlaceholderTV);
        profileEmailTV = findViewById(R.id.profileEmailTV);
        profilePostsRecyclerView = findViewById(R.id.profilePostsRecyclerView);
        profilePostsRecyclerView.setHasFixedSize(true);
        profilePostsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestore.collection(CollectionNames.USERS).document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Users user = task.getResult().toObject(Users.class);

                            Log.d("PROFILE", user.getEmail());

                            profileEmailTV.setText(user.getEmail());
                            profileUsernameTV.setText(user.getUsername());
                            profileFavPostCountTV.setText(Integer.toString(user.getFavPosts().size()));

                            if (user.getUser_image() != null) {
                                Picasso.get().load(user.getUser_image()).into(profileUserAvatarImageView);
                            }
                        }
                    }
                });

        firestore.collection(CollectionNames.POSTS).whereEqualTo(Users.USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    Posts posts = new Posts();
                                    posts.setBookName(doc.getString(Posts.BOOK_TITLE));
                                    posts.setBookDescription(doc.getString(Posts.BOOK_DESC));
                                    posts.setDocId(doc.getId());
                                    posts.setPostImage(doc.getString(Posts.POST_IMAGE));
                                    posts.setPostLikes((List<String>) doc.get(Posts.POST_LIKES));

                                    postsList.add(posts);
                                }
                                Log.d("PROFILE", postsList.toString());

                                if (postsList.size() > 0) {
                                    postAdapter = new PostAdapter(ProfileActivity.this, postsList);
                                    profilePostsRecyclerView.setAdapter(postAdapter);

                                    profilePostsProgressBar.setVisibility(View.GONE);
                                    profilePostsRecyclerView.setVisibility(View.VISIBLE);
                                }
                                else {
                                    profilePostsProgressBar.setVisibility(View.GONE);
                                    profilePostsPlaceholderTV.setVisibility(View.VISIBLE);
                                }
                            }
                    }
                });

        profileFavCountWrapperLL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, FavPostsActivity.class));
            }
        });
    }
}
