package com.alexshabanov.springrestapi.service;

import com.alexshabanov.springrestapi.domain.User;

import java.util.List;

/**
 * Defines operations with user.
 */
public interface UserService {

    int register(String name);

    User findById(int id);

    List<User> findUsers();
}
