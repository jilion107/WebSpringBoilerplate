package com.xmnjm.service;

import com.xmnjm.dao.ProductFilterDao;
import com.xmnjm.model.ProductFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;


/**
 * @author mandy.huang
 */
@Service
public class ProductFilterService {
    @Inject
    ProductFilterDao productFilterDao;

    @Transactional
    public void save(ProductFilter productFilter) {
        productFilter.setCreateTime(new Date());
        productFilter.setUpdateTime(new Date());
        productFilter.setStatus(1);
        productFilterDao.save(productFilter);
    }

    public List<ProductFilter> list(ProductFilter productFilter, int offset, int fetchSize, String orderField) {
        return productFilterDao.list(productFilter, offset, fetchSize, orderField, true);
    }

    public int count(ProductFilter productFilter) {
        return productFilterDao.count(productFilter);
    }

    @Transactional
    public void delete(Long id) {
        ProductFilter productFilter = findById(id);
        productFilterDao.delete(productFilter);
    }

    public ProductFilter findById(Long id) {
        return productFilterDao.findById(id);
    }

    @Transactional
    public void update(ProductFilter productFilter) {
        productFilter.setUpdateTime(new Date());
        productFilterDao.update(productFilter);
    }

    public List<ProductFilter> find(Long productTypeId, Long productColourId, Long productSizeId) {
        return productFilterDao.find(productTypeId, productColourId, productSizeId);
    }

    public List<ProductFilter> find(String productTypeName, String productColourName, String productSizeName) {
        return productFilterDao.find(productTypeName, productColourName, productSizeName);
    }

    public List<ProductFilter> findByProductCategoryId(Long productCategoryId) {
        return productFilterDao.findByProductCategoryId(productCategoryId);
    }

    public List<ProductFilter> findByProductColourId(Long productColourId) {
        return productFilterDao.findByProductColourId(productColourId);
    }

    public List<ProductFilter> findByProductSizeId(Long productSizeId) {
        return productFilterDao.findByProductSizeId(productSizeId);
    }

    @Transactional
    public void delete(ProductFilter productFilter) {
        productFilter.setStatus(0);
        update(productFilter);
    }
}