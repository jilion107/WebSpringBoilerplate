package com.xmnjm.service;

import com.xmnjm.model.User;
import com.xmnjm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jilion on 2017/3/4.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUser(Long userId) {
        User user = userRepository.findOne(userId);
        return user;
    }

    public List<User> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        List<User> userList = new ArrayList<>();
        users.forEach(user -> {
            userList.add(user);
        });
        return userList;
    }

    public User updateUser(User user) {
        return user = userRepository.save(user);
    }

    public User findByUserNameAndPassword(String username, String password) {
        User user = userRepository.findByUserNameAndPassword(username, password);
        return user;
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
