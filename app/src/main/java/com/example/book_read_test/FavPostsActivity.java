package com.example.book_read_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.book_read_test.models.Posts;
import com.example.book_read_test.utils.CollectionNames;
import com.example.book_read_test.utils.GlobalData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavPostsActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    HashMap<String, Posts> allPosts;
    GlobalData globalData;

    Toolbar toolbar;
    TextView favPostDefaultTV;
    SwipeRefreshLayout favPostsSwipeRefresh;
    RecyclerView favPostsRecyclerView;

    PostAdapter postAdapter;

    List<Posts> userFavPostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_posts);

        firestore = FirebaseFirestore.getInstance();
        globalData = (GlobalData) getApplicationContext();
        allPosts = globalData.getAllPosts();
        userFavPostList = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Favorites");

        favPostDefaultTV = findViewById(R.id.favPostDefaultTV);
        favPostsSwipeRefresh = findViewById(R.id.favPostsSwipeRefresh);
        favPostsRecyclerView = findViewById(R.id.favPostsRecyclerView);
        favPostsRecyclerView.setHasFixedSize(true);
        favPostsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (String id : allPosts.keySet()) {
            if (globalData.getLoggedInUserData().getFavPosts().contains(id)) {
                userFavPostList.add(allPosts.get(id));
            }
        }

        if (allPosts.size() > 0) {
            postAdapter = new PostAdapter(FavPostsActivity.this, userFavPostList);
            favPostsRecyclerView.setAdapter(postAdapter);

            favPostsSwipeRefresh.setRefreshing(false);
            favPostsRecyclerView.setVisibility(View.VISIBLE);
            favPostDefaultTV.setVisibility(View.GONE);
        }
        else {
            favPostsSwipeRefresh.setRefreshing(false);
            favPostDefaultTV.setVisibility(View.VISIBLE);
            favPostsRecyclerView.setVisibility(View.GONE);
        }

    }
}
