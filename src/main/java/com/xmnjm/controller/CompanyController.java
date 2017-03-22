package com.xmnjm.controller;

import com.google.gson.Gson;
import com.xmnjm.model.Company;
import com.xmnjm.model.User;
import com.xmnjm.service.CompanyService;
import com.xmnjm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jilion.chen on 3/9/2017.
 */
@CrossOrigin(origins = "http://localhost:8082")
@RestController
public class CompanyController {
    public static final Integer COMPANY_STATUS_ACTIVED = 1;
    public static final Integer COMPANY_STATUS_INACTIVED = 0;

    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/api/companies", method = RequestMethod.GET)
    public @ResponseBody List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @RequestMapping(value = "/api/companies/{id}", method = RequestMethod.PUT)
    public @ResponseBody Object updateCompany(@RequestBody Company company){
        Map<String, Object> result = new HashMap<>();
        try {
            String companyName = company.getCompanyName();
            Company existCompany = companyService.findCompanyByName(companyName);
            if(existCompany != null) {
                result.put("result", "fail");
                result.put("message", "公司名不能重复！");
            } else {
                Company com =  companyService.getCompany(company.getId());
                com.setCompanyName(companyName);
                com.setUpdateTime(new Date());
                result.put("result", "success");
                result.put("company", companyService.updateCompany(com));
            }
            return result;
        } catch (Exception ex) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
    }

    @RequestMapping(value = "/api/company", method = RequestMethod.POST)
    public @ResponseBody Object addCompany(@RequestBody Company company) {
        Map<String, Object> result = new HashMap<>();
        try {
            String companyName = company.getCompanyName();
            Company existCompany = companyService.findCompanyByName(companyName);
            if(existCompany != null) {
                result.put("result", "fail");
                result.put("message", "公司名不能重复！");
            } else {
                Company newCompany = new Company(company.getCompanyName());
                newCompany.setStatus(COMPANY_STATUS_ACTIVED);
                newCompany.setCreateTime(new Date());
                newCompany.setUpdateTime(new Date());
                result.put("result", "success");
                result.put("company", companyService.addCompany(newCompany));
            }
            return result;
        } catch (Exception ex) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
    }

    @RequestMapping(value="/api/companies/{id}", method = RequestMethod.DELETE)
    public @ResponseBody Object deleteCompany(@PathVariable Integer id){
        Map<String, Object> result = new HashMap<>();
        try {
            List<User> users = userService.findByCompanyId(id);
            if(users.isEmpty()) {
                companyService.deleteCompany(id);
                result.put("result", "success");
            }else {
                result.put("result", "fail");
                result.put("message", "请先删除或修改属于此公司的用户！");
            }
            return result;
        } catch (Exception ex) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
    }
}
