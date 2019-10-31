package com.example.book_read_test.models;

import android.content.Intent;

import com.example.book_read_test.PostView;
import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Posts {

    private String bookTitle;
    private String bookDescription;
    private String docId;

    public Posts() {}

    public Posts(String bookname, String bookdesc ,String docId) {
        this.bookTitle = bookname;
        this.bookDescription = bookdesc;
        this.docId = docId;
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

    public String getDocId() {
        return docId;
    }

    @Exclude
    public Map<String , Object> toMap()
    {
        HashMap<String , Object> result = new HashMap<>();

        result.put("bookdesc" , bookDescription);
        result.put("booktitle", bookTitle);

        return result;
    }
}
