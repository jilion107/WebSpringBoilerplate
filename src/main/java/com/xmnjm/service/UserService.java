package com.xmnjm.service;

import com.xmnjm.model.User;
import com.xmnjm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jilion on 2017/3/4.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUser(Integer userId) {
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

    public User findByLoginNameAndPassword(String loginName, String password) {
        User user = userRepository.findByLoginNameAndPassword(loginName, password);
        return user;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findByCompanyId(Integer companyId) {
        return userRepository.findByCompanyId(companyId);
    }

    public List<User> findByLoginName(String userName) {
        return userRepository.findByLoginName(userName);
    }

    private User findById(Integer id){
        return userRepository.findById(id);
    }

    public void deleteUser(Integer userId) {
        userRepository.delete(userId);
    }
}
