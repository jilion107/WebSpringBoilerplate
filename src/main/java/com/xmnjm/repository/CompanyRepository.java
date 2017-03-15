package com.xmnjm.repository;

import com.xmnjm.model.Company;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Jilion on 2017/3/5.
 */
public interface CompanyRepository extends CrudRepository<Company, Integer> {

    Company findByCompanyName(String companyName);
}
