package com.xmnjm.dao;

import com.google.common.base.Strings;
import com.xmnjm.dbcommon.JPAAccess;
import com.xmnjm.dbcommon.Query;
import com.xmnjm.dbcommon.QueryBuilder;
import com.xmnjm.model.ProductCategory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * @author mandy.huang
 */
@Component
public class ProductCategoryDao {
    @Inject
    JPAAccess jpaAccess;

    @Transactional
    public void save(ProductCategory productCategory) {
        productCategory.setStatus(1);
        jpaAccess.save(productCategory);
    }

    @Transactional
    public void update(ProductCategory productCategory) {
        jpaAccess.update(productCategory);
    }

    public ProductCategory findById(Long id) {
        return jpaAccess.findOne(Query.create("from ProductCategory where status=1 and id=:id").param("id", id));
    }

    public List<ProductCategory> findByCategoryName(String categoryName) {
        return jpaAccess.find(Query.create("from ProductCategory where status=1 and categoryName=:categoryName").param("categoryName", categoryName));
    }

    public List<ProductCategory> list(ProductCategory productCategory, int offset, int fetchSize, String orderField, Boolean isDesc) {
        String vOrderField = Strings.isNullOrEmpty(orderField) ? "id" : orderField;
        Boolean vIsDesc = null == isDesc ? Boolean.FALSE : isDesc;
        QueryBuilder queryBuilder = QueryBuilder.query("from ProductCategory").skipEmptyFields()
            .append("status", productCategory.getStatus())
            .orderBy(vOrderField, vIsDesc);
        return jpaAccess.find(queryBuilder.build().from(offset).fetch(fetchSize));
    }

    public int count(ProductCategory productCategory) {
        QueryBuilder queryBuilder = QueryBuilder.query("select count(*) from ProductCategory").skipEmptyFields()
            .append("status", productCategory.getStatus());
        return Integer.parseInt(jpaAccess.find(queryBuilder.build()).get(0).toString());
    }

    public void delete(ProductCategory productCategory) {
        jpaAccess.delete(productCategory);
    }
}