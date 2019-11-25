package com.example.book_read_test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_read_test.models.Posts;
import com.squareup.picasso.Picasso;

import java.util.Stack;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    HomePage homepageActivity;
    Stack<Posts> postsStack;

    private OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener)
    {
        mListener = listener;
    }

    public PostAdapter(HomePage homepage, Stack<Posts> posts) {
        this.homepageActivity = homepage;
        this.postsStack = posts;
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(homepageActivity).inflate(R.layout.list_layout, parent, false);
        PostViewHolder evh = new PostViewHolder(v , mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bookTitle.setText(postsStack.get(position).getBookName());
        holder.bookDescription.setText(postsStack.get(position).getBookDescription());

        if (postsStack.get(position).getPostImage() != null) {
            holder.book_image.setVisibility(View.VISIBLE);
            Picasso.get().load(postsStack.get(position).getPostImage()).into(holder.book_image);
        }


        holder.like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return postsStack.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitle, bookDescription;
        ImageButton like_btn, comment_btn;
        ImageView book_image;

        public PostViewHolder(@NonNull View itemView , final OnItemClickListener listener) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.book_title);
            bookDescription = itemView.findViewById(R.id.book_description);
            like_btn = itemView.findViewById(R.id.like_btn);
            comment_btn = itemView.findViewById(R.id.comment_btn);
            book_image = itemView.findViewById(R.id.book_image);

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
