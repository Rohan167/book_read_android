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
        holder.id_view.setText(postsStack.get(position).getDocId());
    }

    @Override
    public int getItemCount() {
        return postsStack.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitle, bookDescription , id_view;

        public PostViewHolder(@NonNull View itemView , final OnItemClickListener listener) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.book_title);
            bookDescription = itemView.findViewById(R.id.book_description);
            id_view  = itemView.findViewById(R.id.id_view);

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
