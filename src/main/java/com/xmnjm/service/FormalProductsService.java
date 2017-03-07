package com.xmnjm.service;

import com.xmnjm.dao.FormalProductsDao;
import com.xmnjm.model.FormalProducts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;


/**
 * @author mandy.huang
 */
@Service
public class FormalProductsService {
    @Inject
    FormalProductsDao formalProductsDao;

    @Transactional
    public void save(FormalProducts formalProducts) {
        formalProducts.setCreateTime(new Date());
        formalProducts.setUpdateTime(new Date());
        formalProducts.setStatus(1);
        formalProductsDao.save(formalProducts);
    }

    public List<FormalProducts> list(FormalProducts formalProducts, int offset, int fetchSize, String orderField) {
        return formalProductsDao.list(formalProducts, offset, fetchSize, orderField, true);
    }

    public int count(FormalProducts formalProducts) {
        return formalProductsDao.count(formalProducts);
    }

    @Transactional
    public void delete(Long id) {
        FormalProducts formalProducts = findById(id);
        formalProducts.setStatus(0);
        update(formalProducts);
    }

    public FormalProducts findById(Long id) {
        return formalProductsDao.findById(id);
    }

    @Transactional
    public void update(FormalProducts formalProducts) {
        formalProducts.setUpdateTime(new Date());
        formalProductsDao.update(formalProducts);
    }

    @Transactional
    public void delete(FormalProducts formalProducts) {
        formalProducts.setStatus(0);
        update(formalProducts);
    }
}