package com.example.a73233.carefree.bean;

import org.litepal.crud.LitePalSupport;
public class Users_db extends LitePalSupport{
    private int id;
    private String userHeadIma;
    private String userName;
    private String userWords;

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
