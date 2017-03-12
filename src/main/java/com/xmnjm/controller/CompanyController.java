package com.xmnjm.controller;

import com.xmnjm.model.Company;
import com.xmnjm.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jilion.chen on 3/9/2017.
 */
@CrossOrigin(origins = "http://localhost:8082")
@RestController
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @RequestMapping("/api/companies")
    public @ResponseBody List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @RequestMapping("/api/companies/{id}")
    public @ResponseBody Company updateCompany(@RequestBody Company company, @PathVariable String id){
        return companyService.updateCompany(company);
    }

    @RequestMapping("/api/company")
    public @ResponseBody Company addCompany(@RequestBody Company company) {
        Company newCompany = new Company(company.getCompanyName());
        return companyService.addCompany(newCompany);
    }
}
