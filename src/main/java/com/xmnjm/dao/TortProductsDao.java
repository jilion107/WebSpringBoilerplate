package com.xmnjm.dao;

import com.google.common.base.Strings;
import com.xmnjm.bean.ProductRequest;
import com.xmnjm.dbcommon.JPAAccess;
import com.xmnjm.dbcommon.Query;
import com.xmnjm.dbcommon.QueryBuilder;
import com.xmnjm.model.TortProducts;
import com.xmnjm.service.GenDataService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.List;

/**
 * @author mandy.huang
 */
@Component
public class TortProductsDao {
    @Inject
    JPAAccess jpaAccess;
    @Inject
    GenDataService genDataService;

    @Transactional
    public void save(TortProducts tortProducts) {
        tortProducts.setStatus(1);
        jpaAccess.save(tortProducts);
    }

    @Transactional
    public void update(TortProducts tortProducts) {
        jpaAccess.update(tortProducts);
    }

    public TortProducts findById(Long id) {
        return jpaAccess.findOne(Query.create("from TortProducts where status=1 and id=:id").param("id", id));
    }

    public TortProducts findByAsin(String asin) {
        return jpaAccess.findOne(Query.create("from TortProducts where status=1 and asin=:asin").param("asin", asin));
    }

    public List<TortProducts> list(TortProducts tortProducts, int offset, int fetchSize, String orderField, Boolean isDesc) {
        String vOrderField = Strings.isNullOrEmpty(orderField) ? "id" : orderField;
        Boolean vIsDesc = null == isDesc ? Boolean.FALSE : isDesc;
        QueryBuilder queryBuilder = QueryBuilder.query("from TortProducts").skipEmptyFields()
            .append("status", tortProducts.getStatus())
            .append("productTypeName", tortProducts.getProductTypeName())
            .append("productSize", tortProducts.getProductSize())
            .append("productColour", tortProducts.getProductColour())
            .append("userId", tortProducts.getUserId())
            .append("companyId", tortProducts.getCompanyId())
            .orderBy(vOrderField, vIsDesc);
        if (StringUtils.hasText(tortProducts.getAsin())) {
            queryBuilder.append("asin", '%' + tortProducts.getAsin() + '%', "like");
        }
        if (StringUtils.hasText(tortProducts.getBrand())) {
            queryBuilder.append("brand", '%' + tortProducts.getBrand() + '%', "like");
        }

        return jpaAccess.find(queryBuilder.build().from(offset).fetch(fetchSize));
    }

    public int count(TortProducts tortProducts) {
        QueryBuilder queryBuilder = QueryBuilder.query("select count(*) from TortProducts").skipEmptyFields()
            .append("status", tortProducts.getStatus())
            .append("productTypeName", tortProducts.getProductTypeName())
            .append("productSize", tortProducts.getProductSize())
            .append("productColour", tortProducts.getProductColour())
            .append("userId", tortProducts.getUserId())
            .append("companyId", tortProducts.getCompanyId());
        if (StringUtils.hasText(tortProducts.getAsin())) {
            queryBuilder.append("asin", '%' + tortProducts.getAsin() + '%', "like");
        }
        if (StringUtils.hasText(tortProducts.getBrand())) {
            queryBuilder.append("brand", '%' + tortProducts.getBrand() + '%', "like");
        }
        return Integer.parseInt(jpaAccess.find(queryBuilder.build()).get(0).toString());
    }


    public List<TortProducts> list(ProductRequest productRequest, int offset, int fetchSize) {
        StringBuilder builder = new StringBuilder();
        builder.append("from TortProducts where 1=1 ");
        Query query = genDataService.genQuery(productRequest, builder);
        query.from(offset).fetch(fetchSize);
        return jpaAccess.find(query);
    }


    public Long count(ProductRequest productRequest) {
        StringBuilder builder = new StringBuilder();
        builder.append("select count(*) from TortProducts where 1=1 ");
        Query query = genDataService.genQuery(productRequest, builder);
        return Long.parseLong(jpaAccess.find(query).get(0).toString());
    }
}