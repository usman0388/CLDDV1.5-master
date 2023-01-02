package com.example.clddv13.SigningIn;

public class SignUp {
    String username;
    String number;
    String password;
    String confirm;

    public SignUp(String username, String number, String password, String confirm) {
        this.username = username;
        this.number = number;
        this.password = password;
        this.confirm = confirm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}
