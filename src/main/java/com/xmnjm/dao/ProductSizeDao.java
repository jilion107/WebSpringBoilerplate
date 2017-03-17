package com.xmnjm.dao;

import com.google.common.base.Strings;
import com.xmnjm.dbcommon.JPAAccess;
import com.xmnjm.dbcommon.Query;
import com.xmnjm.dbcommon.QueryBuilder;
import com.xmnjm.model.ProductSize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * @author mandy.huang
 */
@Component
public class ProductSizeDao {
    @Inject
    JPAAccess jpaAccess;

    @Transactional
    public void save(ProductSize productSize) {
        productSize.setStatus(1);
        jpaAccess.save(productSize);
    }

    @Transactional
    public void update(ProductSize productSize) {
        jpaAccess.update(productSize);
    }

    public ProductSize findById(Long id) {
        return jpaAccess.findOne(Query.create("from ProductSize where status=1 and id=:id").param("id", id));
    }

    public List<ProductSize> findBySizeName(String sizeName) {
        return jpaAccess.find(Query.create("from ProductSize where status=1 and sizeName=:sizeName").param("sizeName", sizeName));
    }

    public List<ProductSize> list(ProductSize productSize, int offset, int fetchSize, String orderField, Boolean isDesc) {
        String vOrderField = Strings.isNullOrEmpty(orderField) ? "id" : orderField;
        Boolean vIsDesc = null == isDesc ? Boolean.FALSE : isDesc;
        QueryBuilder queryBuilder = QueryBuilder.query("from ProductSize").skipEmptyFields()
            .append("status", productSize.getStatus())
            .orderBy(vOrderField, vIsDesc);
        return jpaAccess.find(queryBuilder.build().from(offset).fetch(fetchSize));
    }

    public int count(ProductSize productSize) {
        QueryBuilder queryBuilder = QueryBuilder.query("select count(*) from ProductSize").skipEmptyFields()
            .append("status", productSize.getStatus());
        return Integer.parseInt(jpaAccess.find(queryBuilder.build()).get(0).toString());
    }

    public void delete(ProductSize productSize) {
        jpaAccess.delete(productSize);
    }
}