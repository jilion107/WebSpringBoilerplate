package com.xmnjm.controller;

import com.xmnjm.dao.UserDao;
import com.xmnjm.model.Company;
import com.xmnjm.model.User;
import com.xmnjm.service.CompanyService;
import com.xmnjm.service.UserService;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    @Autowired
    private UserDao userDao;
    @Autowired
    private CompanyService companyService;

    @RequestMapping(value = "/api/users/", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/api/users/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateUser(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        String loginName = user.getLoginName();
        try {
            User newUser =  userService.getUser(user.getId());
            Company company = companyService.getCompany(newUser.getCompanyId());
            Date newUserCreatedTime = newUser.getCreateTime();
            Integer newUserStatue = newUser.getStatus();
            BeanUtils.copyProperties(user, newUser);
            newUser.setCompanyId(company.getId());
            newUser.setUpdateTime(new Date());
            newUser.setPassword(user.getPhone());
            newUser.setCreateTime(newUserCreatedTime);
            newUser.setStatus(newUserStatue);
            newUser.setUpdateTime(new Date());
            result.put("result", "success");
            result.put("user", userService.updateUser(newUser));
            return result;
        } catch (Exception ex) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.POST)
    @ResponseBody
    public Object register(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
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
        } catch (Exception ex) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
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
        try {
            userService.deleteUser(id);
            result.put("result", "success");
            return result;
        } catch (Exception ex) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
    }

    @RequestMapping(value= "/api/users", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(value = "userName", required = false) String userName, @RequestParam(value = "phone", required = false) String phone) {
        Map<String, Object> result = new HashMap<>();
        List<User> users = null;
        try {
            if(userName != "" && userName != null) {
                users = userDao.list("userName",userName);
            } else if(phone != "" && phone != null) {
                users = userDao.list("phone",phone);
            } else {
                users = userService.getAllUsers();
            }
            result.put("result", "success");
            result.put("users", users);
            return result;
        } catch (Exception e) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
    }
}
