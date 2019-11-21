package com.example.book_read_test.models;

import java.util.HashMap;
import java.util.Map;

public class Users {
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String USER_IMAGE = "user_image";

    private String username;
    private String email;
    private String password;
    private String user_image;

    public Users()
    {

    }


    public Users(String username , String email , String password, String userimg)
    {
        this.username = username;
        this.email =email;
        this.password = password;
        this.user_image = userimg;
    }

    //GETTER METHODS

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    //SETTER METHODS

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setEmail(String email) {this.email=email;}

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
}
