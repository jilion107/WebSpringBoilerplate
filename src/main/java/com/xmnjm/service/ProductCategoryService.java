package com.xmnjm.service;


import com.xmnjm.dao.ProductCategoryDao;
import com.xmnjm.model.ProductCategory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;


/**
 * @author mandy.huang
 */
@Service
public class ProductCategoryService {
    @Inject
    ProductCategoryDao productCategoryDao;

    @Transactional
    public void save(ProductCategory productCategory) {
        productCategory.setCreateTime(new Date());
        productCategory.setUpdateTime(new Date());
        productCategory.setStatus(1);
        productCategoryDao.save(productCategory);
    }

    public List<ProductCategory> list(ProductCategory productCategory, int offset, int fetchSize, String orderField) {
        return productCategoryDao.list(productCategory, offset, fetchSize, orderField, true);
    }

    public List<ProductCategory> findByCategoryName(String categoryName) {
        return productCategoryDao.findByCategoryName(categoryName);
    }

    public int count(ProductCategory productCategory) {
        return productCategoryDao.count(productCategory);
    }

    @Transactional
    public void delete(Long id) {
        ProductCategory productCategory = findById(id);
        productCategoryDao.delete(productCategory);
    }

    public ProductCategory findById(Long id) {
        return productCategoryDao.findById(id);
    }

    @Transactional
    public void update(ProductCategory productCategory) {
        productCategory.setUpdateTime(new Date());
        productCategoryDao.update(productCategory);
    }

    @Transactional
    public void delete(ProductCategory productCategory) {
        productCategory.setStatus(0);
        update(productCategory);
    }
}