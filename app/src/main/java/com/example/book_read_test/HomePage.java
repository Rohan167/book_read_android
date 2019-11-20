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

import java.util.Stack;

public class HomePage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener{
//    Button logout_btn;
    TextView welcome_text , like_count_view;
    private DrawerLayout drawer;
    ImageView image_nav_view;
    ImageButton like_btn , comment_btn;
    TextView user_nav_text;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    CollectionNames collectionNames;
    Stack<Posts> postsStack;
    Stack usersStack;
    PostAdapter postAdapter;
    RecyclerView homePostRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        collectionNames = new CollectionNames();
        postsStack = new Stack<>();
        homePostRecyclerView = findViewById(R.id.homePostRecyclerView);
        homePostRecyclerView.setHasFixedSize(true);
        homePostRecyclerView.setLayoutManager(new LinearLayoutManager(this));



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
                finish();
                startActivity(new Intent(HomePage.this , ProfilePage.class));
                break;
            }
            case R.id.nav_add:
            {
                finish();
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
        firestore.collection(collectionNames.getPostCollection()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Posts posts = new Posts(
                                    doc.getString("booktitle"),
                                    doc.getString("bookdesc"),
                                        doc.getId()
                                );

                                postsStack.add(posts);
                            }

                            postAdapter = new PostAdapter(HomePage.this, postsStack);
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

                                    String val = postsStack.get(position).getDocId();
                                    String book_name = postsStack.get(position).getBookName();
                                    String book_desc = postsStack.get(position).getBookDescription();
                                    Posts get_pos = postsStack.get(position);
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
        firestore.collection(collectionNames.getUserCollection()).document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Users user = task.getResult().toObject(Users.class);

                        user_nav_text.setText(user.getUsername());

                    }
                });
    }

}
