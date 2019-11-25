package com.example.book_read_test.models;


import java.util.List;

public class Posts {

    public static final String BOOK_TITLE = "bookName";
    public static final String BOOK_DESC = "bookDescription";
    public static final String POST_IMAGE = "postImage";
    public static final String DOC_ID = "docId";
    public static final String POST_LIKES = "postLikes";

    private String bookTitle;
    private String bookDescription;
    private String docId;
    private String postImage;
    private String userId;
    private List<String> postLikes;

    public Posts() {}

    public Posts(String bookname, String bookdesc ,String docId, String postimg) {
        this.bookTitle = bookname;
        this.bookDescription = bookdesc;
        this.docId = docId;
        this.postImage = postimg;
    }


    public String getBookName() {
        return bookTitle;
    }

    public void setBookName(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

//
//    @Exclude
//    public Map<String , Object> toMap()
//    {
//        HashMap<String , Object> result = new HashMap<>();
//
//        result.put("bookdesc" , bookDescription);
//        result.put("booktitle", bookTitle);
//
//        return result;
//    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(List<String> postLikes) {
        this.postLikes = postLikes;
    }
}
