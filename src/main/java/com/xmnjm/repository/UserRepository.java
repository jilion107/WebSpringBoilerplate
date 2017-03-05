package com.xmnjm.repository;

import com.xmnjm.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Jilion on 2017/3/4.
 */
public interface UserRepository extends CrudRepository<User,Long> {

    public User findByUserNameAndPassword(String username, String password);
}
