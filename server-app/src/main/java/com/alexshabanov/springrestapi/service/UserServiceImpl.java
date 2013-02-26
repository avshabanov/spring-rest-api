package com.alexshabanov.springrestapi.service;

import com.alexshabanov.springrestapi.domain.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sample server-side implementation of the {@link UserService}
 *
 * @author Alexander Shabanov
 */
@Service
public final class UserServiceImpl implements UserService {
    private final Map<Integer, User> userMap = new ConcurrentHashMap<Integer, User>();
    private final AtomicInteger idCounter = new AtomicInteger();

    @Override
    public int register(String name) {
        final int id = idCounter.incrementAndGet();
        userMap.put(id, User.as(id, name));
        return id;
    }

    @Override
    public User findById(int id) {
        return userMap.get(id);
    }

    @Override
    public List<User> findAll() {
        return Collections.unmodifiableList(new ArrayList<User>(userMap.values()));
    }
}
