package com.example.book_read_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.book_read_test.models.Comments;
import com.example.book_read_test.models.Posts;
import com.example.book_read_test.models.Users;
import com.example.book_read_test.utils.CollectionNames;
import com.example.book_read_test.utils.GlobalData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    GlobalData globalData;
    HashMap<String, Comments> allComments;
    HashMap<String, Posts> allPosts;
    List<Comments> commentsList;
    CommentAdapter commentAdapter;
    WriteBatch batch;

    Toolbar toolbar;
    TextView cmnt_book_title, cmnt_book_description, cmntDefaultTV;
    RecyclerView commentRecyclerView;
    ImageView cmnt_book_image;
    EditText commentET;
    Button submitCommentBtn;
    ProgressBar cmntProgressBar;

    String SELECTED_POST_ID;
    Users loggedInUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        firestore = FirebaseFirestore.getInstance();
        batch = firestore.batch();
        globalData = (GlobalData) getApplicationContext();
        allComments = globalData.getUserAllComments();
        allPosts = globalData.getAllPosts();
        loggedInUser = globalData.getLoggedInUserData();
        commentsList = new ArrayList<>();

        SELECTED_POST_ID = getIntent().getStringExtra(Posts.DOC_ID);

        toolbar = findViewById(R.id.toolbar);
        commentET = findViewById(R.id.commentET);
        cmntProgressBar = findViewById(R.id.cmntProgressBar);
        cmntDefaultTV = findViewById(R.id.cmntDefaultTV);
        submitCommentBtn = findViewById(R.id.submitCommentBtn);
        commentRecyclerView = findViewById(R.id.commentRecyclerView);
        cmnt_book_description = findViewById(R.id.cmnt_book_description);
        cmnt_book_title = findViewById(R.id.cmnt_book_title);
        cmnt_book_image = findViewById(R.id.cmnt_book_image);

        toolbar.setTitle("Comments");
        commentRecyclerView.setHasFixedSize(true);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cmnt_book_title.setText(allPosts.get(SELECTED_POST_ID).getBookName());
        cmnt_book_description.setText(allPosts.get(SELECTED_POST_ID).getBookDescription());

        if (allPosts.get(SELECTED_POST_ID).getPostImage() != null) {
            Picasso.get().load(allPosts.get(SELECTED_POST_ID).getPostImage()).into(cmnt_book_image);
        }

        for (String id : allPosts.get(SELECTED_POST_ID).getPostComments()) {
            commentsList.add(allComments.get(id));
        }

        commentAdapter = new CommentAdapter(CommentActivity.this, commentsList);
        commentRecyclerView.setAdapter(commentAdapter);

        if (commentsList.size() > 0) {
            cmntProgressBar.setVisibility(View.GONE);
            commentRecyclerView.setVisibility(View.VISIBLE);
        }
        else {
            cmntProgressBar.setVisibility(View.GONE);
            cmntDefaultTV.setVisibility(View.VISIBLE);
        }


        submitCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Comments cmnt = new Comments();
                cmnt.setComment(commentET.getText().toString());
                cmnt.setUserAvatar(loggedInUser.getUser_image());
                cmnt.setUserId(loggedInUser._getUserId());
                cmnt.setUsername(loggedInUser.getUsername());
                cmnt.setPostId(SELECTED_POST_ID);

                firestore.collection(CollectionNames.COMMENTS).add(cmnt)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    final String newCmntId = task.getResult().getId();

                                    firestore.collection(CollectionNames.POSTS).document(SELECTED_POST_ID)
                                            .update(Posts.POST_COMMENTS, FieldValue.arrayUnion(task.getResult().getId()))
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    cmnt._setCommentId(newCmntId);
                                                    commentsList.add(0, cmnt);
                                                    allComments.put(newCmntId, cmnt);
                                                    commentAdapter = new CommentAdapter(CommentActivity.this, commentsList);
                                                    commentRecyclerView.setAdapter(commentAdapter);
                                                    cmntProgressBar.setVisibility(View.GONE);
                                                    commentRecyclerView.setVisibility(View.VISIBLE);
                                                }
                                            });
                                }
                            }
                        });
            }
        });
    }
}
