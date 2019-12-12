package com.example.book_read_test.utils;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.book_read_test.models.Comments;
import com.example.book_read_test.models.Posts;
import com.example.book_read_test.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class GlobalData extends Application {

    private HashMap<String, Posts> allPosts = new HashMap<>();
    private Users loggedInUserData = new Users();
    private HashMap<String, Comments> userAllComments = new HashMap<>();
    private HashMap<String, Users> allUsersData = new HashMap<>();


    public HashMap<String, Posts> getAllPosts() {
        return allPosts;
    }

    public void setAllPosts() {
        FirebaseFirestore.getInstance().collection(CollectionNames.POSTS).get()
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
                                posts.setPostComments((List<String>) doc.get(Posts.POST_COMMENTS));

                                Log.d("GLOBAL_DATA", doc.get(Posts.POST_COMMENTS).toString());

                                allPosts.put(doc.getId(), posts);
                            }
                        }
                    }
                });
    }

    public void addToAllPost(String id, Posts post) {
        this.allPosts.put(id, post);
    }

    public void removeFromAllPost(String id) {
        this.allPosts.remove(id);
    }

    public Users getLoggedInUserData() {
        return loggedInUserData;
    }

    public void setLoggedInUserData() {
        FirebaseFirestore.getInstance().collection(CollectionNames.USERS)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot doc = task.getResult();

                            loggedInUserData._setUserId(doc.getId());
                            loggedInUserData.setFavPosts((List<String>) doc.get(Users.FAV_POSTS));
                            loggedInUserData.setUsername(doc.getString(Users.USERNAME));
                            loggedInUserData.setUser_image(doc.getString(Users.USER_IMAGE));
                            loggedInUserData.setEmail(doc.getString(Users.EMAIL));
                        }
                    }
                });
    }

    public HashMap<String, Comments> getUserAllComments() {
        return userAllComments;
    }

    public void setUserAllComments() {
        FirebaseFirestore.getInstance().collection(CollectionNames.COMMENTS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Comments comment = new Comments();

                                comment._setCommentId(doc.getId());
                                comment.setComment(doc.getString(Comments.COMMENT));
                                comment.setUserId(doc.getString(Users.USER_ID));
                                comment._setUser_image(allUsersData.get(doc.getString(Users.USER_ID)).getUser_image());
                                comment.setPostId(doc.getString(Posts.DOC_ID));
                                comment._setUsername(allUsersData.get(doc.getString(Users.USER_ID)).getUsername());

                                userAllComments.put(doc.getId(), comment);
                            }

                            Log.d("GLOBAL_DATA", new Gson().toJson(userAllComments));
                        }
                    }
                });
    }

    public HashMap<String, Users> getAllUsersData() {
        return allUsersData;
    }

    public void setAllUsersData() {
        FirebaseFirestore.getInstance().collection(CollectionNames.USERS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult() != null && task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Users user = new Users();

                                user._setUserId(doc.getId());
                                user.setUser_image(doc.getString(Users.USER_IMAGE));
                                user.setUsername(doc.getString(Users.USERNAME));
                                user.setEmail(doc.getString(Users.EMAIL));
                                user.setFavPosts((List<String>)doc.get(Users.FAV_POSTS));

                                allUsersData.put(doc.getId(), user);
                            }
                        }
                    }
                });
    }
}
