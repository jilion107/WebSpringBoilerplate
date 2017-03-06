package com.xmnjm.controller;

import com.xmnjm.model.User;
import com.xmnjm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Jilion on 2017/3/4.
 */
@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "http://localhost:8082")
    @RequestMapping("/api/login")
    public @ResponseBody User login(@RequestParam("username") String username, @RequestParam("password") String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return null;
        } else {
            User user = userService.findByUserNameAndPassword(username, password);
            if (user == null) {
                return null;
            } else {
                return user;
            }
        }
    }
}
