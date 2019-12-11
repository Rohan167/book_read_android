package com.example.book_read_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.book_read_test.models.Posts;
import com.example.book_read_test.models.Users;
import com.example.book_read_test.utils.CollectionNames;
import com.example.book_read_test.utils.GlobalData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class HomePage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener{
    TextView like_count_view;
    private DrawerLayout drawer;
    ImageView image_nav_view;
    ImageButton like_btn , comment_btn;
    TextView user_nav_text;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    CollectionNames collectionNames;
    PostAdapter postAdapter;
    RecyclerView homePostRecyclerView;

    GlobalData globalData;

    @Override
    protected void onResume() {
        super.onResume();
        getAllPosts();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        globalData = (GlobalData) getApplicationContext();

        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        collectionNames = new CollectionNames();
        homePostRecyclerView = findViewById(R.id.homePostRecyclerView);
        homePostRecyclerView.setHasFixedSize(true);
        homePostRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        globalData.setAllPosts();
        globalData.setLoggedInUserData();
        globalData.setUserAllComments();

        Toolbar  toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView  = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        android.view.View navHeaderView = navigationView.getHeaderView(0);

        image_nav_view = navHeaderView.findViewById(R.id.image_nav_view);
        user_nav_text = navHeaderView.findViewById(R.id.user_nav_text);
        like_btn = navHeaderView.findViewById(R.id.like_btn);
        comment_btn = navHeaderView.findViewById(R.id.comment_btn);
        like_count_view = navHeaderView.findViewById(R.id.like_count_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer, toolbar , R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getAllPosts();
        getUserDets();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId())
        {
            case R.id.nav_logout:
            {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(HomePage.this , LoginPage.class));

                break;
            }
            case R.id.nav_profile:
            {
                startActivity(new Intent(HomePage.this , ProfilePage.class));
                break;
            }
            case R.id.nav_profile_activity: {
                startActivity(new Intent(HomePage.this, ProfileActivity.class));
                break;
            }
            case R.id.nav_add:
            {
                startActivity(new Intent(HomePage.this , AddPostActivity.class));
                break;
            }
        }

            drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }

    }


    public void getAllPosts() {
        final List<Posts> postsList = new ArrayList<>();

        firestore.collection(CollectionNames.POSTS).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Posts posts = new Posts();
                                posts.setBookName(doc.getString(Posts.BOOK_TITLE));
                                posts.setBookDescription(doc.getString(Posts.BOOK_DESC));
                                posts.setDocId(doc.getId());
                                posts.setPostImage(doc.getString(Posts.POST_IMAGE));
                                posts.setPostLikes((List<String>) doc.get(Posts.POST_LIKES));
                                posts.setPostComments((List<String>) doc.get(Posts.POST_COMMENTS));

                                postsList.add(posts);
                            }

                            postAdapter = new PostAdapter(HomePage.this, postsList);
                            homePostRecyclerView.setAdapter(postAdapter);

                            postAdapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
//                                        Stack <String> send_dat = new Stack<>();
//
//                                    for (QueryDocumentSnapshot val : task.getResult())
//                                    {
//                                        send_dat.add(val.getString("booktitle"));
//                                        send_dat.add(val.getString("bookdesc"));
//                                        send_dat.add(val.getId());
//
//                                    }

                                    String val = postsList.get(position).getDocId();
                                    String book_name = postsList.get(position).getBookName();
                                    String book_desc = postsList.get(position).getBookDescription();
                                    Posts get_pos = postsList.get(position);
                                    startActivity(new Intent(HomePage.this , PostView.class).putExtra("Id" , val).putExtra("name" , book_name).putExtra("desc",book_desc));

                                }
                            });

                        }
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("HOME DATA RETRIVE ", e.getMessage());
            }
        });
    }

    public void getUserDets()
    {
        firestore.collection(CollectionNames.USERS).document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult() != null && task.isSuccessful()) {
                            Users user = task.getResult().toObject(Users.class);

                            user_nav_text.setText(user.getUsername());

                            if (user.getUser_image() != null)
                                Picasso.get().load(user.getUser_image()).into(image_nav_view);
                        }

                    }
                });
    }

}
