package com.xmnjm.controller;

import com.xmnjm.bean.LoginRequest;
import com.xmnjm.bean.UserResponse;
import com.xmnjm.model.User;
import com.xmnjm.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jilion on 2017/3/4.
 */
@CrossOrigin(origins = "http://localhost:8082")
@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(@Valid @RequestBody LoginRequest loginRequest) {
        Map<String, Object> result = new HashMap<>();
        if (!StringUtils.hasText(loginRequest.getLoginName()) || !StringUtils.hasText(loginRequest.getPassword())) {
            result.put("result", "fail");
            result.put("message", "登录名和密码不能为空");
            return result;
        }

        User user = userService.findByLoginNameAndPassword(loginRequest.getLoginName(), loginRequest.getPassword());
        if (user == null) {
            result.put("result", "fail");
            result.put("message", "手机号或密码错误！");
            return result;
        }
        result.put("result", "success");
        result.put("user", user);
        return result;
    }
}
