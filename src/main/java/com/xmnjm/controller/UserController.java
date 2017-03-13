package com.xmnjm.controller;

import com.xmnjm.bean.LoginRequest;
import com.xmnjm.bean.UserResponse;
import com.xmnjm.model.User;
import com.xmnjm.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jilion.chen on 3/10/2017.
 */
@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    @ResponseBody
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    @ResponseBody
    public User register(@RequestBody User user) {
        return userService.save(user);
    }

    @RequestMapping(value = "/api/users/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(@Valid @RequestBody LoginRequest loginRequest) {
        Map<String, Object> result = new HashMap<>();
        if (!StringUtils.hasText(loginRequest.getLoginName()) || !StringUtils.hasText(loginRequest.getPassword())) {
            result.put("result", "fail");
            result.put("message", "登录名和密码不能为空");
            return result;
        }

        User user = userService.findByUserNameAndPassword(loginRequest.getLoginName(), loginRequest.getPassword());
        if (user == null) {
            result.put("result", "fail");
            result.put("message", "手机号或密码错误！");
            return result;
        }
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        if (user.getCompany() != null) userResponse.setCompany(user.getCompany());

        result.put("result", "success");
        result.put("user", userResponse);
        return result;
    }

}
