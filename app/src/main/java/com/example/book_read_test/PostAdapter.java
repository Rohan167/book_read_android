package com.example.book_read_test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_read_test.models.Posts;

import java.util.Stack;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    HomePage homepageActivity;
    Stack<Posts> postsStack;

    public PostAdapter(HomePage homepage, Stack<Posts> posts) {
        this.homepageActivity = homepage;
        this.postsStack = posts;
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(homepageActivity).inflate(R.layout.list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bookTitle.setText(postsStack.get(position).getBookName());
        holder.bookDescription.setText(postsStack.get(position).getBookDescription());
    }

    @Override
    public int getItemCount() {
        return postsStack.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitle, bookDescription;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.book_title);
            bookDescription = itemView.findViewById(R.id.book_description);
        }
    }
}
