package com.example.fdas.Model;

import java.util.List;

public class GetAllUserResponse {

    List<UserModel> users;

    public GetAllUserResponse(List<UserModel> users) {
        this.users = users;
    }

    public List<UserModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }
}
