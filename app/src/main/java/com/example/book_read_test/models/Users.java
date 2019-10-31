package com.example.book_read_test.models;

import java.util.HashMap;
import java.util.Map;

public class Users {
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    private String username;
    private String email;
    private String password;

    public Users()
    {

    }


    public Users(String username , String email , String password)
    {
        this.username = username;
        this.email =email;
        this.password = password;
    }

    public Map<String,Object> getUsersDets() {

        Map<String, Object> userData = new HashMap<>();
        userData.put(USERNAME, username);
        userData.put(EMAIL, email);
        userData.put(PASSWORD, password);
        return userData;
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

}
