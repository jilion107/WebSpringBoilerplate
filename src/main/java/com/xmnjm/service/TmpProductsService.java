package com.xmnjm.service;


import com.xmnjm.dao.TmpProductsDao;
import com.xmnjm.model.TmpProducts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;


/**
 * @author mandy.huang
 */
@Service
public class TmpProductsService {
    @Inject
    TmpProductsDao tmpProductsDao;

    @Transactional
    public void save(TmpProducts tmpProducts) {
        tmpProducts.setCreateTime(new Date());
        tmpProducts.setUpdateTime(new Date());
        tmpProducts.setStatus(1);
        tmpProductsDao.save(tmpProducts);
    }

    public List<TmpProducts> list(TmpProducts tmpProducts, int offset, int fetchSize, String orderField) {
        return tmpProductsDao.list(tmpProducts, offset, fetchSize, orderField, true);
    }

    public int count(TmpProducts tmpProducts) {
        return tmpProductsDao.count(tmpProducts);
    }

    @Transactional
    public void delete(Long id) {
        TmpProducts tmpProducts = findById(id);
        tmpProducts.setStatus(0);
        update(tmpProducts);
    }

    public TmpProducts findById(Long id) {
        return tmpProductsDao.findById(id);
    }

    @Transactional
    public void update(TmpProducts tmpProducts) {
        tmpProducts.setUpdateTime(new Date());
        tmpProductsDao.update(tmpProducts);
    }

    @Transactional
    public void delete(TmpProducts tmpProducts) {
        tmpProducts.setStatus(0);
        update(tmpProducts);
    }
}