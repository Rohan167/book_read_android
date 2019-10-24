package com.example.book_read_test.models;

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
}
