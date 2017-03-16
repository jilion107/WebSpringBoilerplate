package com.xmnjm.repository;

import com.xmnjm.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Jilion on 2017/3/4.
 */
public interface UserRepository extends CrudRepository<User,Integer> {

    public User findByLoginNameAndPassword(String loginName, String password);

    public List<User> findByCompanyId(Integer companyId);

    public List<User> findByLoginName(String userName);

    public User findById(Integer id);
}
