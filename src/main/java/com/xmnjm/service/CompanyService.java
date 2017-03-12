package com.xmnjm.service;

import com.xmnjm.model.Company;
import com.xmnjm.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jilion on 2017/3/4.
 */
@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public Company getCompany(Long companyId) {
        Company company = companyRepository.findOne(companyId);
        return company;
    }

    public List<Company> getAllCompanies() {
        Iterable<Company> companies = companyRepository.findAll();
        List<Company> companyList = new ArrayList<>();
        companies.forEach(company -> {
            companyList.add(company);
        });
        return companyList;
    }

    public Company updateCompany(Company company){
        return company = companyRepository.save(company);
    }

    public Company addCompany(Company company) {
        return company = companyRepository.save(company);
    }
}
