package com.xmnjm.service;


import com.xmnjm.bean.ProductRequest;
import com.xmnjm.dao.TortProductsDao;
import com.xmnjm.model.TortProducts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;


/**
 * @author mandy.huang
 */
@Service
public class TortProductsService {
    @Inject
    TortProductsDao tortProductsDao;

    @Transactional
    public void save(TortProducts tortProducts) {
        tortProducts.setCreateTime(new Date());
        tortProducts.setUpdateTime(new Date());
        tortProducts.setStatus(1);
        tortProductsDao.save(tortProducts);
    }

    public List<TortProducts> list(TortProducts tortProducts, int offset, int fetchSize, String orderField) {
        return tortProductsDao.list(tortProducts, offset, fetchSize, orderField, true);
    }

    public int count(TortProducts tortProducts) {
        return tortProductsDao.count(tortProducts);
    }

    @Transactional
    public void delete(Long id) {
        TortProducts tortProducts = findById(id);
        tortProducts.setStatus(0);
        update(tortProducts);
    }

    public TortProducts findById(Long id) {
        return tortProductsDao.findById(id);
    }

    @Transactional
    public void update(TortProducts tortProducts) {
        tortProducts.setUpdateTime(new Date());
        tortProductsDao.update(tortProducts);
    }

    public TortProducts findByAsin(String asin) {
        return tortProductsDao.findByAsin(asin);
    }

    @Transactional
    public void delete(TortProducts tortProducts) {
        tortProducts.setStatus(0);
        update(tortProducts);
    }

    public List<TortProducts> list(ProductRequest productRequest, int offset, int fetchSize) {
        return tortProductsDao.list(productRequest, offset, fetchSize);
    }

    public Long count(ProductRequest productRequest) {
        return tortProductsDao.count(productRequest);
    }
}