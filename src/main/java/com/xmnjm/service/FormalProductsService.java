package com.xmnjm.service;

import com.xmnjm.bean.ProductRequest;
import com.xmnjm.dao.FormalProductsDao;
import com.xmnjm.model.FormalProducts;
import com.xmnjm.model.ProductCategory;
import com.xmnjm.model.TmpProducts;
import org.springframework.beans.BeanUtils;
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
    @Inject
    TmpProductsService tmpProductsService;
    @Inject
    ProductCategoryService productCategoryService;
    @Inject
    RoleService roleService;

    public List<FormalProducts> list(FormalProducts formalProducts, int offset, int fetchSize, String orderField) {
        return formalProductsDao.list(formalProducts, offset, fetchSize, orderField, true);
    }

    public List<FormalProducts> list(ProductRequest productRequest, int offset, int fetchSize) {
        roleService.updateProductRequestByRole(productRequest);
        return formalProductsDao.list(productRequest, offset, fetchSize);
    }

    public Long count(ProductRequest productRequest) {
        roleService.updateProductRequestByRole(productRequest);
        return formalProductsDao.count(productRequest);
    }

    @Transactional
    public void delete(Long id) {
        FormalProducts formalProducts = findById(id);
        if (formalProducts != null)
            formalProductsDao.delete(formalProducts);
    }

    public FormalProducts findById(Long id) {
        return formalProductsDao.findById(id);
    }

    public FormalProducts findByAsin(String asin) {
        return formalProductsDao.findByAsin(asin);
    }

    @Transactional
    public void delete(FormalProducts formalProducts) {
        formalProducts.setStatus(0);
        update(formalProducts);
    }

    @Transactional
    public void update(FormalProducts formalProducts) {
        formalProducts.setUpdateTime(new Date());
        formalProductsDao.update(formalProducts);
    }

    @Transactional
    public void saveFromTmpProduct(Long productTypeId, Long tmpProductId) {
        TmpProducts tmpProducts = tmpProductsService.findById(tmpProductId);
        if (tmpProducts == null) return;
        FormalProducts formalProducts = new FormalProducts();
        BeanUtils.copyProperties(tmpProducts, formalProducts, "id");
        ProductCategory productCategory = productCategoryService.findById(productTypeId);
        if (productCategory != null) {
            formalProducts.setProductTypeId(productTypeId);
            formalProducts.setProductTypeName(productCategory.getCategoryName());
        }
        this.save(formalProducts);
        tmpProductsService.delete(tmpProductId);
    }

    @Transactional
    public void save(FormalProducts formalProducts) {
        formalProducts.setCreateTime(new Date());
        formalProducts.setUpdateTime(new Date());
        formalProducts.setStatus(1);
        formalProductsDao.save(formalProducts);
    }

    public List<FormalProducts> export(List<Long> ids, Date endUpdateTime, int fetch) {
        return formalProductsDao.export(ids, endUpdateTime, fetch);
    }

}