package com.Samagan.user;

import java.util.UUID;

public class UserService {

    private UserDao userDao;

    public UserService(UserDao userDao){
        this.userDao = userDao;
    }

    public User[] getAllUser(){
        return userDao.getUsers();
    }

    public User getUserById(UUID id){
        if (id == null ){
            throw new IllegalAccessError("User ID can not be null ");
        }
        return userDao.getUserById(id);
    }

    public boolean registerUser(String name){
        return userDao.addUser(name);
    }

}
