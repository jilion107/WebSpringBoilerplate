package com.xmnjm.service;


import com.xmnjm.bean.ProductRequest;
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
    @Inject
    RoleService roleService;

    @Transactional
    public void save(TmpProducts tmpProducts) {
        tmpProducts.setCreateTime(new Date());
        tmpProducts.setUpdateTime(new Date());
        tmpProducts.setStatus(1);
        tmpProductsDao.save(tmpProducts);
    }

    public List<TmpProducts> list(TmpProducts tmpProducts, int offset, int fetchSize, String orderField, Date startCreateTime, Date endCreateTime) {
        return tmpProductsDao.list(tmpProducts, offset, fetchSize, orderField, true, startCreateTime, endCreateTime);
    }

    public Long count(TmpProducts tmpProducts, Date startCreateTime, Date endCreateTime) {
        return tmpProductsDao.count(tmpProducts, startCreateTime, endCreateTime);
    }

    @Transactional
    public void delete(Long id) {
        tmpProductsDao.deleteById(id);
    }

    public TmpProducts findById(Long id) {
        return tmpProductsDao.findById(id);
    }

    public TmpProducts findByAsin(String asin) {
        return tmpProductsDao.findByAsin(asin);
    }

    @Transactional
    public void delete(TmpProducts tmpProducts) {
        tmpProductsDao.delete(tmpProducts);
    }

    @Transactional
    public void update(TmpProducts tmpProducts) {
        tmpProducts.setUpdateTime(new Date());
        tmpProductsDao.update(tmpProducts);
    }

    public List<TmpProducts> list(ProductRequest productRequest, int offset, int fetchSize) {
        roleService.updateProductRequestByRole(productRequest);
        return tmpProductsDao.list(productRequest, offset, fetchSize);
    }

    public Long count(ProductRequest productRequest) {
        roleService.updateProductRequestByRole(productRequest);
        return tmpProductsDao.count(productRequest);
    }
}