package com.example.clddv13.LogIn;

import com.google.gson.annotations.SerializedName;

public class loggingIn {

    String username;

    String password;

    public String getUserId() {
        return username;
    }

    public void setUserId(String userId) {
        this.username = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public loggingIn(String userId, String password) {
        this.username = userId;
        this.password = password;
    }
}
