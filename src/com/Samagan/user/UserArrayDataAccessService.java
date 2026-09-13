package com.Samagan.user;

import java.util.UUID;

public class UserArrayDataAccessService implements UserDao {
    private static final User[] USERS = new User[]{
            new User(UUID.fromString("8c363e95-71cb-4678-8314-7221051522f1"), "James"),
            new User(UUID.fromString("9a7c36b8-2c67-4632-8414-7221051522f2"), "Sophia")
    };

    @Override
    public User[] getUsers() {
        return USERS;
    }

    @Override
    public User findUserById(UUID userId) {
        for (User user : USERS) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
}