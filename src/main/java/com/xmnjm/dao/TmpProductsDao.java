package com.xmnjm.dao;

import com.google.common.base.Strings;
import com.xmnjm.bean.ProductRequest;
import com.xmnjm.dbcommon.JPAAccess;
import com.xmnjm.dbcommon.Query;
import com.xmnjm.dbcommon.QueryBuilder;
import com.xmnjm.model.TmpProducts;
import com.xmnjm.service.GenDataService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * @author mandy.huang
 */
@Component
public class TmpProductsDao {
    @Inject
    JPAAccess jpaAccess;
    @Inject
    GenDataService genDataService;

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

    public void deleteById(Long id) {
        TmpProducts tmpProducts = jpaAccess.findOne(Query.create("from TmpProducts where status=1 and id=:id").param("id", id));
        if (tmpProducts != null) jpaAccess.delete(tmpProducts);
    }

    public void delete(TmpProducts tmpProducts) {
        if (tmpProducts != null) jpaAccess.delete(tmpProducts);
    }

    public TmpProducts findByAsin(String asin) {
        return jpaAccess.findOne(Query.create("from TmpProducts where status=1 and asin=:asin").param("asin", asin));
    }

    public List<TmpProducts> list(TmpProducts tmpProducts, int offset, int fetchSize, String orderField, Boolean isDesc, Date startCreateTime, Date endCreateTime) {
        String vOrderField = Strings.isNullOrEmpty(orderField) ? "id" : orderField;
        Boolean vIsDesc = null == isDesc ? Boolean.FALSE : isDesc;
        QueryBuilder queryBuilder = QueryBuilder.query("from TmpProducts").skipEmptyFields()
            .append("status", tmpProducts.getStatus())
            .append("productTypeName", tmpProducts.getProductTypeName())
            .append("productSize", tmpProducts.getProductSize())
            .append("productColour", tmpProducts.getProductColour())
            .append("userId", tmpProducts.getUserId())
            .append("companyId", tmpProducts.getCompanyId())
            .append("createTime", startCreateTime, "<=")
            .append("createTime", endCreateTime, ">=")
            .orderBy(vOrderField, vIsDesc);
        if (StringUtils.hasText(tmpProducts.getAsin())) {
            queryBuilder.append("asin", '%' + tmpProducts.getAsin() + '%', "like");
        }
        if (StringUtils.hasText(tmpProducts.getBrand())) {
            queryBuilder.append("brand", '%' + tmpProducts.getBrand() + '%', "like");
        }
        return jpaAccess.find(queryBuilder.build().from(offset).fetch(fetchSize));
    }

    public Long count(TmpProducts tmpProducts, Date startCreateTime, Date endCreateTime) {
        QueryBuilder queryBuilder = QueryBuilder.query("select count(*) from TmpProducts").skipEmptyFields()
            .append("status", tmpProducts.getStatus())
            .append("productTypeName", tmpProducts.getProductTypeName())
            .append("productSize", tmpProducts.getProductSize())
            .append("productColour", tmpProducts.getProductColour())
            .append("userId", tmpProducts.getUserId())
            .append("createTime", startCreateTime, "<=")
            .append("createTime", endCreateTime, ">=")
            .append("companyId", tmpProducts.getCompanyId());
        if (StringUtils.hasText(tmpProducts.getAsin())) {
            queryBuilder.append("asin", '%' + tmpProducts.getAsin() + '%', "like");
        }
        if (StringUtils.hasText(tmpProducts.getBrand())) {
            queryBuilder.append("brand", '%' + tmpProducts.getBrand() + '%', "like");
        }
        return Long.parseLong(jpaAccess.find(queryBuilder.build()).get(0).toString());
    }

    public List<TmpProducts> list(ProductRequest productRequest, int offset, int fetchSize) {
        StringBuilder builder = new StringBuilder();
        builder.append("from TmpProducts where 1=1 ");
        Query query = genDataService.genQuery(productRequest, builder);
        query.from(offset).fetch(fetchSize);
        return jpaAccess.find(query);
    }


    public Long count(ProductRequest productRequest) {
        StringBuilder builder = new StringBuilder();
        builder.append("select count(*) from TmpProducts where 1=1 ");
        Query query = genDataService.genQuery(productRequest, builder);
        return Long.parseLong(jpaAccess.find(query).get(0).toString());
    }
}