package com.xmnjm.service;


import com.xmnjm.dao.ProductColourDao;
import com.xmnjm.model.ProductColour;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;


/**
 * @author mandy.huang
 */
@Service
public class ProductColourService {
    @Inject
    ProductColourDao productColourDao;

    @Transactional
    public void save(ProductColour productColour) {
        productColour.setCreateTime(new Date());
        productColour.setUpdateTime(new Date());
        productColour.setStatus(1);
        productColourDao.save(productColour);
    }

    public List<ProductColour> list(ProductColour productColour, int offset, int fetchSize, String orderField) {
        return productColourDao.list(productColour, offset, fetchSize, orderField, true);
    }

    public int count(ProductColour productColour) {
        return productColourDao.count(productColour);
    }

    @Transactional
    public void delete(Long id) {
        ProductColour productColour = findById(id);
        productColourDao.delete(productColour);
    }

    public ProductColour findById(Long id) {
        return productColourDao.findById(id);
    }

    @Transactional
    public void update(ProductColour productColour) {
        productColour.setUpdateTime(new Date());
        productColourDao.update(productColour);
    }

    public List<ProductColour> findByColourName(String colourName) {
        return productColourDao.findByColourName(colourName);
    }

    @Transactional
    public void delete(ProductColour productColour) {
        productColour.setStatus(0);
        update(productColour);
    }
}