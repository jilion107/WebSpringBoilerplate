package com.xmnjm.controller;

import com.xmnjm.bean.LoginRequest;
import com.xmnjm.bean.UserResponse;
import com.xmnjm.model.User;
import com.xmnjm.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

/**
 * Created by jilion.chen on 3/10/2017.
 */
@CrossOrigin(origins = "http://localhost:8082")
@RestController
public class UserController {
    public static final Integer USER_STATUS_ACTIVED = 1;
    public static final Integer USER_STATUS_INACTIVED = 0;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/api/users/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateUser(@RequestBody User user) {
        String loginName = user.getLoginName();
        List<User> existUser = userService.findByLoginName(loginName);
        Map<String, Object> result = new HashMap<>();
        if(existUser.isEmpty()) {
            User newUser =  userService.getUser(user.getId());
            Date newUserCreatedTime = newUser.getCreateTime();
            Integer newUserStatue = newUser.getStatus();
            BeanUtils.copyProperties(user, newUser);
            newUser.setUpdateTime(new Date());
            newUser.setPassword(user.getPhone());
            newUser.setCreateTime(newUserCreatedTime);
            newUser.setStatus(newUserStatue);
            newUser.setUpdateTime(new Date());
            result.put("result", "success");
            result.put("user", userService.updateUser(newUser));
        } else {
            result.put("result", "fail");
            result.put("message", "登录名不能重复！");
        }
        return result;
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.POST)
    @ResponseBody
    public Object register(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        String loginName = user.getLoginName();
        List<User> existUser = userService.findByLoginName(loginName);
        if(existUser.isEmpty()) {
            user.setPassword(user.getPhone());
            user.setStatus(USER_STATUS_ACTIVED);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            result.put("result", "success");
            result.put("user", userService.save(user));
        } else {
            result.put("result", "fail");
            result.put("message", "登录名'" + loginName + "'已经存在！");
        }
        return result;
    }

    @RequestMapping(value = "/api/users/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserById(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        User user = userService.getUser(id);
        if(user == null) {
            result.put("result", "fail");
            result.put("message", "公司名不能重复！");
        } else {
            result.put("result", "success");
            result.put("user", user);
        }
        return result;
    }

    @RequestMapping(value="/api/users/{id}", method = RequestMethod.DELETE)
    public @ResponseBody Object deleteUser(@PathVariable Integer id){
        Map<String, Object> result = new HashMap<>();
        userService.deleteUser(id);
        result.put("result", "success");
        return result;
    }
}
