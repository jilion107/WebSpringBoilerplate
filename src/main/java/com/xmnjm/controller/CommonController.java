package com.xmnjm.controller;

import com.xmnjm.bean.RoleEnum;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mandy.huang
 */
@CrossOrigin(origins = "http://localhost:8082")
@RestController
public class CommonController {
    @RequestMapping(value = "/api/role-enum", method = RequestMethod.GET)
    @ResponseBody
    public void getRoleEnum() {
        Map<String, String> roleMap = new HashMap<>();
        for (RoleEnum roleEnum : EnumSet.allOf(RoleEnum.class)) {
            roleMap.put(roleEnum.name(), roleEnum.getValue());
        }
    }
}
