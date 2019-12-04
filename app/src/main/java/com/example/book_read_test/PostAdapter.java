package com.example.book_read_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_read_test.models.Posts;
import com.example.book_read_test.models.Users;
import com.example.book_read_test.utils.CollectionNames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Stack;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    FirebaseFirestore firestore;
    WriteBatch batch;
    Context context;
    List<Posts> postsList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener)
    {
        mListener = listener;
    }

    public PostAdapter(Context c, List<Posts> posts) {
        this.context = c;
        this.postsList = posts;
        this.firestore = FirebaseFirestore.getInstance();
        this.batch = firestore.batch();
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_layout, parent, false);
        PostViewHolder evh = new PostViewHolder(v , mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, int position) {
        final String THIS_POST_ID = postsList.get(position).getDocId();
        final String LOGGED_IN_UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DocumentReference userDocRef = firestore.collection(CollectionNames.USERS).document(LOGGED_IN_UID);
        final DocumentReference postDocRef = firestore.collection(CollectionNames.POSTS).document(THIS_POST_ID);

        holder.bookTitle.setText(postsList.get(position).getBookName());
        holder.bookDescription.setText(postsList.get(position).getBookDescription());
        holder.like_text_view.setText(Integer.toString(postsList.get(position).getPostLikes().size()));

        if (postsList.get(position).getPostLikes().contains(LOGGED_IN_UID)) {
            holder.like_btn.setVisibility(View.GONE);
            holder.unlike_btn.setVisibility(View.VISIBLE);
        }

        if (postsList.get(position).getPostImage() != null) {
            holder.book_image.setVisibility(View.VISIBLE);
            Picasso.get().load(postsList.get(position).getPostImage()).into(holder.book_image);
        }


        holder.like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                batch.update(userDocRef, Users.FAV_POSTS, FieldValue.arrayUnion(THIS_POST_ID));
                batch.update(postDocRef, Posts.POST_LIKES, FieldValue.arrayUnion(LOGGED_IN_UID));

                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            holder.like_btn.setVisibility(View.GONE);
                            holder.unlike_btn.setVisibility(View.VISIBLE);
                            int postLikeCount = Integer.parseInt(holder.like_text_view.getText().toString());
                            holder.like_text_view.setText(Integer.toString(postLikeCount + 1));
                        }
                    }
                });
            }
        });

        holder.unlike_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                batch.update(userDocRef, Users.FAV_POSTS, FieldValue.arrayRemove(THIS_POST_ID));
                batch.update(postDocRef, Posts.POST_LIKES, FieldValue.arrayRemove(LOGGED_IN_UID));

                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            holder.like_btn.setVisibility(View.VISIBLE);
                            holder.unlike_btn.setVisibility(View.GONE);
                            int postLikeCount = Integer.parseInt(holder.like_text_view.getText().toString());
                            holder.like_text_view.setText(Integer.toString(postLikeCount - 1));
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitle, bookDescription, like_text_view, postCommentCountTV;
        ImageView like_btn, comment_btn, book_image, unlike_btn;

        public PostViewHolder(@NonNull View itemView , final OnItemClickListener listener) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.book_title);
            bookDescription = itemView.findViewById(R.id.book_description);
            like_btn = itemView.findViewById(R.id.like_btn);
            like_text_view = itemView.findViewById(R.id.like_text_view);
            comment_btn = itemView.findViewById(R.id.comment_btn);
            book_image = itemView.findViewById(R.id.book_image);
            unlike_btn = itemView.findViewById(R.id.unlike_btn);
            postCommentCountTV = itemView.findViewById(R.id.postCommentCountTV);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }

                }
            });
        }
    }
}
