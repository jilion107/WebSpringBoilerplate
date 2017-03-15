package com.xmnjm.controller;

import com.google.gson.Gson;
import com.xmnjm.model.Company;
import com.xmnjm.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by jilion.chen on 3/9/2017.
 */
@CrossOrigin(origins = "http://localhost:8082")
@RestController
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @RequestMapping(value = "/api/companies", method = RequestMethod.GET)
    public @ResponseBody List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @RequestMapping(value = "/api/companies/{id}", method = RequestMethod.PUT)
    public @ResponseBody Company updateCompany(@RequestBody Company company){
        Company com =  companyService.getCompany(company.getId());
        com.setCompanyName(company.getCompanyName());
        return companyService.updateCompany(com);
    }

    @RequestMapping(value = "/api/company", method = RequestMethod.POST)
    public @ResponseBody Object addCompany(@RequestBody Company company) {
        String companyName = company.getCompanyName();
        Company existCompany = companyService.findCompanyByName(companyName);
        Map<String, Object> result = new HashMap<>();
        if(existCompany != null) {
            result.put("result", "fail");
            result.put("message", "公司名不能重复！");
            return result;
        } else {
            Company newCompany = new Company(company.getCompanyName());
            result.put("result", "success");
            result.put("company", companyService.addCompany(newCompany));
            return result;
        }
    }

    @RequestMapping(value="/api/companies/{id}", method = RequestMethod.DELETE)
    public @ResponseBody void deleteCompany(@PathVariable String id){
        companyService.deleteCompany(new Integer(id));
    }
}
