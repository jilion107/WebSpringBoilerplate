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
        productFilter.setStatus(0);
        update(productFilter);
    }

    public ProductFilter findById(Long id) {
        return productFilterDao.findById(id);
    }

    @Transactional
    public void update(ProductFilter productFilter) {
        productFilter.setUpdateTime(new Date());
        productFilterDao.update(productFilter);
    }

    @Transactional
    public void delete(ProductFilter productFilter) {
        productFilter.setStatus(0);
        update(productFilter);
    }
}