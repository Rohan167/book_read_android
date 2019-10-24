package com.example.book_read_test.models;

public class Posts {

    private String bookTitle;
    private String bookDescription;

    public Posts() {}

    public Posts(String bookname, String bookdesc) {
        this.bookTitle = bookname;
        this.bookDescription = bookdesc;
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
}
