package com.xmnjm.dao;

import com.google.common.base.Strings;
import com.xmnjm.bean.ProductRequest;
import com.xmnjm.dbcommon.JPAAccess;
import com.xmnjm.dbcommon.Query;
import com.xmnjm.dbcommon.QueryBuilder;
import com.xmnjm.model.FormalProducts;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author mandy.huang
 */
@Component
public class FormalProductsDao {
    @Inject
    JPAAccess jpaAccess;
    @Inject
    JdbcTemplate jdbcTemplate;

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

    public List<FormalProducts> list(ProductRequest productRequest, int offset, int fetchSize) {
        StringBuilder builder = new StringBuilder();
        builder.append("from FormalProducts where 1=1 ");
        Query query = this.genQuery(productRequest, builder);
        query.from(offset).fetch(fetchSize);
        return jpaAccess.find(query);
    }

    public Query genQuery(ProductRequest productRequest, StringBuilder builder) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.hasText(productRequest.getBrand())) {
            builder.append("and brand=:brand ");
            params.put("brand", productRequest.getBrand());
        }
        if (StringUtils.hasText(productRequest.getAsin())) {
            builder.append("and asin=:asin ");
            params.put("asin", productRequest.getAsin());
        }
        if (productRequest.getProductTypeId() != null && productRequest.getProductTypeId() > 0) {
            builder.append("and productTypeId=:productTypeId ");
            params.put("productTypeId", productRequest.getProductTypeId());
        }
        if (productRequest.getScenarioWhat() != null) {
            builder.append("and scenarioWhat=:scenarioWhat ");
            params.put("scenarioWhat", productRequest.getScenarioWhat());
        }
        if (productRequest.getStartCreateTime() != null) {
            builder.append("and createTime<=:startCreateTime ");
            params.put("startCreateTime", productRequest.getStartCreateTime());
        }
        if (productRequest.getEndCreateTime() != null) {
            builder.append("and createTime>=:endCreateTime ");
            params.put("endCreateTime", productRequest.getEndCreateTime());
        }
        if (!CollectionUtils.isEmpty(productRequest.getProductSizes())) {
            builder.append("and (");
            for (int i = 0; i < productRequest.getProductSizes().size(); i++) {
                builder.append("productSize=:productSize").append(i);
                if (i < productRequest.getProductSizes().size() - 1) builder.append(" or ");
                params.put("productSize" + i, productRequest.getProductSizes().get(i));
            }
            builder.append(") ");
        }
        if (!CollectionUtils.isEmpty(productRequest.getProductColours())) {
            builder.append("and (");
            for (int i = 0; i < productRequest.getProductColours().size(); i++) {
                builder.append("productColour=:productColour").append(i);
                if (i < productRequest.getProductColours().size() - 1) builder.append(" or ");
                params.put("productColour" + i, productRequest.getProductColours().get(i));
            }
            builder.append(") ");
        }
        Query query = Query.create(builder.toString());
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            query.param(key, params.get(key));
        }
        return query;
    }

    public Long count(ProductRequest productRequest) {
        StringBuilder builder = new StringBuilder();
        builder.append("select count(*) from FormalProducts where 1=1 ");
        Query query = this.genQuery(productRequest, builder);
        return Long.parseLong(jpaAccess.find(query).get(0).toString());
    }

    public Long count(FormalProducts formalProducts) {
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
        return Long.parseLong(jpaAccess.find(queryBuilder.build()).get(0).toString());
    }
}