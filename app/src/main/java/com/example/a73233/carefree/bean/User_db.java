package com.example.a73233.carefree.bean;

import org.litepal.crud.LitePalSupport;

public class User_db extends LitePalSupport {
    private String userHeadIma;
    private String userId;
    private String userName;
    private String userWords;
    private int id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserHeadIma() {
        return userHeadIma;
    }

    public void setUserHeadIma(String userHeadIma) {
        this.userHeadIma = userHeadIma;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserWords() {
        return userWords;
    }

    public void setUserWords(String userWords) {
        this.userWords = userWords;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
