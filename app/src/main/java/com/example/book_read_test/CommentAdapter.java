package com.example.book_read_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_read_test.models.Comments;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    
    CommentActivity context;
    List<Comments> commentsList;
    
    public CommentAdapter(CommentActivity ca, List<Comments> commentsList) {
        this.context = ca;
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(this.context).inflate(R.layout.comment_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.commentTV.setText(commentsList.get(position).getComment());
        holder.cmntUsername.setText(commentsList.get(position).getUsername());

        if (commentsList.get(position).getUserAvatar() != null) {
            Picasso.get().load(commentsList.get(position).getUserAvatar()).into(holder.cmntUserAvatarIV);
        }

        if (commentsList.get(position).getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.cmntMoreBtn.setVisibility(View.VISIBLE);

            PopupMenu popup = new PopupMenu(context, holder.cmntMoreBtn);
            popup.inflate(R.menu.edit_delete_menu);

//            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem menuItem) {
//                    switch (menuItem.getItemId()) {
//                        case R.id.edit_post: {
//                            break;
//                        }
//                        case R.id.delete_post: {
//                            break;
//                        }
//                    }
//
//                    return false;
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView cmntUserAvatarIV;
        TextView cmntUsername, commentTV;
        ImageButton cmntMoreBtn;

        public CommentViewHolder(@NonNull View iv) {
            super(iv);

            cmntUserAvatarIV = iv.findViewById(R.id.cmntUserAvatarIV);
            cmntUsername = iv.findViewById(R.id.cmntUsername);
            commentTV = iv.findViewById(R.id.commentTV);
            cmntMoreBtn = iv.findViewById(R.id.cmntMoreBtn);
        }
    }
}
