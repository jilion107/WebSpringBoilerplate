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

    @RequestMapping(value = "/api/companies", method = RequestMethod.GET)
    public @ResponseBody List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @RequestMapping(value = "/api/companies/{id}", method = RequestMethod.PUT)
    public @ResponseBody Company updateCompany(@RequestBody Company company){
        return companyService.updateCompany(company);
    }

    @RequestMapping(value = "/api/company", method = RequestMethod.POST)
    public @ResponseBody Company addCompany(@RequestBody Company company) {
        Company newCompany = new Company(company.getCompanyName());
        return companyService.addCompany(newCompany);
    }

    @RequestMapping(value="/api/companies/{id}", method = RequestMethod.DELETE)
    public @ResponseBody void deleteCompany(@PathVariable String id){
        companyService.deleteCompany(new Integer(id));
    }
}
