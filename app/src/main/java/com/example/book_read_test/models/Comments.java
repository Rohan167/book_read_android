package com.example.book_read_test.models;

public class Comments {

    public static final String COMMENT_ID = "commentId";
    public static final String COMMENT = "comment";

    private String commentId;
    private String comment;
    private String userId;
    private String postId;
    private String username;
    private String userAvatar;

    public String _getCommentId() {
        return commentId;
    }

    public void _setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
