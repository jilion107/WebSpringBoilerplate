package com.xmnjm.dao;

import com.google.common.base.Strings;
import com.xmnjm.dbcommon.JPAAccess;
import com.xmnjm.dbcommon.Query;
import com.xmnjm.dbcommon.QueryBuilder;
import com.xmnjm.model.ProductColour;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * @author mandy.huang
 */
@Component
public class ProductColourDao {
    @Inject
    JPAAccess jpaAccess;

    @Transactional
    public void save(ProductColour productColour) {
        productColour.setStatus(1);
        jpaAccess.save(productColour);
    }

    @Transactional
    public void update(ProductColour productColour) {
        jpaAccess.update(productColour);
    }

    public ProductColour findById(Long id) {
        return jpaAccess.findOne(Query.create("from ProductColour where status=1 and id=:id").param("id", id));
    }

    public List<ProductColour> findByColourName(String colourName) {
        return jpaAccess.find(Query.create("from ProductColour where status=1 and colourName=:colourName").param("colourName", colourName));
    }

    public List<ProductColour> list(ProductColour productColour, int offset, int fetchSize, String orderField, Boolean isDesc) {
        String vOrderField = Strings.isNullOrEmpty(orderField) ? "id" : orderField;
        Boolean vIsDesc = null == isDesc ? Boolean.FALSE : isDesc;
        QueryBuilder queryBuilder = QueryBuilder.query("from ProductColour").skipEmptyFields()
            .append("status", productColour.getStatus())
            .orderBy(vOrderField, vIsDesc);

        return jpaAccess.find(queryBuilder.build().from(offset).fetch(fetchSize));
    }

    public int count(ProductColour productColour) {
        QueryBuilder queryBuilder = QueryBuilder.query("select count(*) from ProductColour").skipEmptyFields()
            .append("status", productColour.getStatus());
        return Integer.parseInt(jpaAccess.find(queryBuilder.build()).get(0).toString());
    }

    public void delete(ProductColour productColour) {
        jpaAccess.delete(productColour);
    }
}