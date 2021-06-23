package com.example.fdas.Model;

public class LoginModel {

UserModel user;
String error;
String message;

    public LoginModel(UserModel user, String error, String message) {
        this.user = user;
        this.error = error;
        this.message = message;
    }


    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
