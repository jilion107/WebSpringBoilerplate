package com.xmnjm.service;

import com.xmnjm.dao.ProductSizeDao;
import com.xmnjm.model.ProductSize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;


/**
 * @author mandy.huang
 */
@Service
public class ProductSizeService {
    @Inject
    ProductSizeDao productSizeDao;

    @Transactional
    public void save(ProductSize productSize) {
        productSize.setCreateTime(new Date());
        productSize.setUpdateTime(new Date());
        productSize.setStatus(1);
        productSizeDao.save(productSize);
    }

    public List<ProductSize> list(ProductSize productSize, int offset, int fetchSize, String orderField) {
        return productSizeDao.list(productSize, offset, fetchSize, orderField, true);
    }

    public int count(ProductSize productSize) {
        return productSizeDao.count(productSize);
    }

    @Transactional
    public void delete(Long id) {
        ProductSize productSize = findById(id);
        productSize.setStatus(0);
        update(productSize);
    }

    public ProductSize findById(Long id) {
        return productSizeDao.findById(id);
    }

    @Transactional
    public void update(ProductSize productSize) {
        productSize.setUpdateTime(new Date());
        productSizeDao.update(productSize);
    }

    @Transactional
    public void delete(ProductSize productSize) {
        productSize.setStatus(0);
        update(productSize);
    }
}