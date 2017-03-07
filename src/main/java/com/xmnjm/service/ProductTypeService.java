package com.xmnjm.service;


import com.xmnjm.dao.ProductTypeDao;
import com.xmnjm.model.ProductType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;


/**
 * @author mandy.huang
 */
@Service
public class ProductTypeService {
    @Inject
    ProductTypeDao productTypeDao;

    @Transactional
    public void save(ProductType productType) {
        productType.setCreateTime(new Date());
        productType.setUpdateTime(new Date());
        productType.setStatus(1);
        productTypeDao.save(productType);
    }

    public List<ProductType> list(ProductType productType, int offset, int fetchSize, String orderField) {
        return productTypeDao.list(productType, offset, fetchSize, orderField, true);
    }

    public int count(ProductType productType) {
        return productTypeDao.count(productType);
    }

    @Transactional
    public void delete(Long id) {
        ProductType productType = findById(id);
        productType.setStatus(0);
        update(productType);
    }

    public ProductType findById(Long id) {
        return productTypeDao.findById(id);
    }

    @Transactional
    public void update(ProductType productType) {
        productType.setUpdateTime(new Date());
        productTypeDao.update(productType);
    }

    @Transactional
    public void delete(ProductType productType) {
        productType.setStatus(0);
        update(productType);
    }
}