package com.xmnjm.dao;

import com.google.common.base.Strings;
import com.xmnjm.dbcommon.JPAAccess;
import com.xmnjm.dbcommon.Query;
import com.xmnjm.dbcommon.QueryBuilder;
import com.xmnjm.model.FormalProducts;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.List;

/**
 * @author mandy.huang
 */
@Component
public class FormalProductsDao {
    @Inject
    JPAAccess jpaAccess;

    @Transactional
    public void save(FormalProducts formalProducts) {
        formalProducts.setStatus(1);
        jpaAccess.save(formalProducts);
    }

    @Transactional
    public void update(FormalProducts formalProducts) {
        jpaAccess.update(formalProducts);
    }

    public FormalProducts findById(Long id) {
        return jpaAccess.findOne(Query.create("from FormalProducts where status=1 and id=:id").param("id", id));
    }

    public FormalProducts findByAsin(String asin) {
        return jpaAccess.findOne(Query.create("from FormalProducts where status=1 and asin=:asin").param("asin", asin));
    }

    public List<FormalProducts> list(FormalProducts formalProducts, int offset, int fetchSize, String orderField, Boolean isDesc) {
        String vOrderField = Strings.isNullOrEmpty(orderField) ? "id" : orderField;
        Boolean vIsDesc = null == isDesc ? Boolean.FALSE : isDesc;
        QueryBuilder queryBuilder = QueryBuilder.query("from FormalProducts").skipEmptyFields()
            .append("status", formalProducts.getStatus())
            .append("productTypeId", formalProducts.getProductTypeId())
            .append("productSize", formalProducts.getProductSize())
            .append("productColour", formalProducts.getProductColour())
            .append("userId", formalProducts.getUserId())
            .append("companyId", formalProducts.getCompanyId())
            .orderBy(vOrderField, vIsDesc);
        if (StringUtils.hasText(formalProducts.getAsin())) {
            queryBuilder.append("asin", '%' + formalProducts.getAsin() + '%', "like");
        }
        if (StringUtils.hasText(formalProducts.getBrand())) {
            queryBuilder.append("brand", '%' + formalProducts.getBrand() + '%', "like");
        }
        return jpaAccess.find(queryBuilder.build().from(offset).fetch(fetchSize));
    }

    public int count(FormalProducts formalProducts) {
        QueryBuilder queryBuilder = QueryBuilder.query("select count(*) from FormalProducts").skipEmptyFields()
            .append("status", formalProducts.getStatus())
            .append("productTypeId", formalProducts.getProductTypeId())
            .append("productSize", formalProducts.getProductSize())
            .append("productColour", formalProducts.getProductColour())
            .append("userId", formalProducts.getUserId())
            .append("companyId", formalProducts.getCompanyId());
        if (StringUtils.hasText(formalProducts.getAsin())) {
            queryBuilder.append("asin", '%' + formalProducts.getAsin() + '%', "like");
        }
        if (StringUtils.hasText(formalProducts.getBrand())) {
            queryBuilder.append("brand", '%' + formalProducts.getBrand() + '%', "like");
        }
        return Integer.parseInt(jpaAccess.find(queryBuilder.build()).get(0).toString());
    }
}