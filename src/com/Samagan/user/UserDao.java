package com.Samagan.user;

import java.util.UUID;

public class UserDao {
    private static int CAPACITY = 1000;
    private static final User[] USERS = new User[CAPACITY];
    private static int userCount = 0;

    static{
        USERS[userCount++] = new User(UUID.fromString("801e0a29-b695-467f-be99-4d6cb60e32b8"), "Alice");
        USERS[userCount++] = new User(UUID.fromString("229f3154-1fa1-4993-85ec-9e235e236ce4"), "Bob");
        USERS[userCount++] = new User(UUID.fromString("38d2a67e-bb36-47b2-a42e-13cb09f9024c"), "Charlie");
    }
    public User[] getUsers(){
        return USERS;
    }

    public boolean  addUser(String name){
        if (userCount >= CAPACITY){
            CAPACITY *= 2;
        }
        USERS[userCount++] = new User(UUID.randomUUID(), name);
        return true;
    }

    public static User getUserById(UUID id) {
        for(int i=0; i<userCount; i++){
            if(USERS != null && USERS[i].getId().equals(id)){
                return USERS[i];
            }
        }
        return null;
    }
}
