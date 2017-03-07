package com.xmnjm.dao;

import com.google.common.base.Strings;
import com.xmnjm.dbcommon.JPAAccess;
import com.xmnjm.dbcommon.Query;
import com.xmnjm.dbcommon.QueryBuilder;
import com.xmnjm.model.TmpProducts;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.List;

/**
 * @author mandy.huang
 */
@Component
public class TmpProductsDao {
    @Inject
    JPAAccess jpaAccess;

    @Transactional
    public void save(TmpProducts tmpProducts) {
        tmpProducts.setStatus(1);
        jpaAccess.save(tmpProducts);
    }

    @Transactional
    public void update(TmpProducts tmpProducts) {
        jpaAccess.update(tmpProducts);
    }

    public TmpProducts findById(Long id) {
        return jpaAccess.findOne(Query.create("from TmpProducts where status=1 and id=:id").param("id", id));
    }

    public List<TmpProducts> list(TmpProducts tmpProducts, int offset, int fetchSize, String orderField, Boolean isDesc) {
        String vOrderField = Strings.isNullOrEmpty(orderField) ? "id" : orderField;
        Boolean vIsDesc = null == isDesc ? Boolean.FALSE : isDesc;
        QueryBuilder queryBuilder = QueryBuilder.query("from TmpProducts").skipEmptyFields()
            .append("status", tmpProducts.getStatus())
            .append("productTypeName", tmpProducts.getProductTypeName())
            .append("productSize", tmpProducts.getProductSize())
            .append("productColour", tmpProducts.getProductColour())
            .append("userId", tmpProducts.getUserId())
            .append("companyId", tmpProducts.getCompanyId())
            .orderBy(vOrderField, vIsDesc);
        if (StringUtils.hasText(tmpProducts.getAsin())) {
            queryBuilder.append("asin", '%' + tmpProducts.getAsin() + '%', "like");
        }
        if (StringUtils.hasText(tmpProducts.getBrand())) {
            queryBuilder.append("brand", '%' + tmpProducts.getBrand() + '%', "like");
        }
        return jpaAccess.find(queryBuilder.build().from(offset).fetch(fetchSize));
    }

    public int count(TmpProducts tmpProducts) {
        QueryBuilder queryBuilder = QueryBuilder.query("select count(*) from TmpProducts").skipEmptyFields()
            .append("status", tmpProducts.getStatus())
            .append("productTypeName", tmpProducts.getProductTypeName())
            .append("productSize", tmpProducts.getProductSize())
            .append("productColour", tmpProducts.getProductColour())
            .append("userId", tmpProducts.getUserId())
            .append("companyId", tmpProducts.getCompanyId());
        if (StringUtils.hasText(tmpProducts.getAsin())) {
            queryBuilder.append("asin", '%' + tmpProducts.getAsin() + '%', "like");
        }
        if (StringUtils.hasText(tmpProducts.getBrand())) {
            queryBuilder.append("brand", '%' + tmpProducts.getBrand() + '%', "like");
        }
        return Integer.parseInt(jpaAccess.find(queryBuilder.build()).get(0).toString());
    }
}