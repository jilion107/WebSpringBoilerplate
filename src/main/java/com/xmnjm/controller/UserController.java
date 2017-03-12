package com.xmnjm.controller;

import com.xmnjm.model.User;
import com.xmnjm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jilion.chen on 3/10/2017.
 */
@CrossOrigin(origins = "http://localhost:8082")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/api/users")
    public @ResponseBody List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @RequestMapping("/api/users/{id}")
    public @ResponseBody User updateUser(@RequestBody User user, @PathVariable String id) {
        return userService.updateUser(user);
    }

}
