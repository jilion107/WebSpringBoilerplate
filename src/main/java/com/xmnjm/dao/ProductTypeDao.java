package com.xmnjm.dao;

import com.google.common.base.Strings;
import com.xmnjm.dbcommon.JPAAccess;
import com.xmnjm.dbcommon.Query;
import com.xmnjm.dbcommon.QueryBuilder;
import com.xmnjm.model.ProductType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * @author mandy.huang
 */
@Component
public class ProductTypeDao {
    @Inject
    JPAAccess jpaAccess;

    @Transactional
    public void save(ProductType productType) {
        productType.setStatus(1);
        jpaAccess.save(productType);
    }

    @Transactional
    public void update(ProductType productType) {
        jpaAccess.update(productType);
    }

    public ProductType findById(Long id) {
        return jpaAccess.findOne(Query.create("from ProductType where status=1 and id=:id").param("id", id));
    }

    public List<ProductType> list(ProductType productType, int offset, int fetchSize, String orderField, Boolean isDesc) {
        String vOrderField = Strings.isNullOrEmpty(orderField) ? "id" : orderField;
        Boolean vIsDesc = null == isDesc ? Boolean.FALSE : isDesc;
        QueryBuilder queryBuilder = QueryBuilder.query("from ProductType").skipEmptyFields()
            .append("status", productType.getStatus())
            .orderBy(vOrderField, vIsDesc);
        return jpaAccess.find(queryBuilder.build().from(offset).fetch(fetchSize));
    }

    public int count(ProductType productType) {
        QueryBuilder queryBuilder = QueryBuilder.query("select count(*) from ProductType").skipEmptyFields()
            .append("status", productType.getStatus());
        return Integer.parseInt(jpaAccess.find(queryBuilder.build()).get(0).toString());
    }
}