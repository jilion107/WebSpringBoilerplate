package com.xmnjm.dao;

import com.google.common.base.Strings;
import com.xmnjm.dbcommon.JPAAccess;
import com.xmnjm.dbcommon.Query;
import com.xmnjm.dbcommon.QueryBuilder;
import com.xmnjm.model.ProductFilter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * @author mandy.huang
 */
@Component
public class ProductFilterDao {
    @Inject
    JPAAccess jpaAccess;

    @Transactional
    public void save(ProductFilter productFilter) {
        productFilter.setStatus(1);
        jpaAccess.save(productFilter);
    }

    @Transactional
    public void update(ProductFilter productFilter) {
        jpaAccess.update(productFilter);
    }

    public ProductFilter findById(Long id) {
        return jpaAccess.findOne(Query.create("from ProductFilter where status=1 and id=:id").param("id", id));
    }

    public List<ProductFilter> findByType(Long productTypeId) {
        return jpaAccess.find(Query.create("from ProductFilter where status=1 and productTypeId=:productTypeId").param("productTypeId", productTypeId));
    }

    public List<ProductFilter> find(Long productTypeId, Long productColourId, Long productSizeId) {
        return jpaAccess.find(Query.create("from ProductFilter where status=1 and productTypeId=:productTypeId and productColourId=:productColourId and productSizeId=:productSizeId").param("productTypeId", productTypeId).param("productColourId", productColourId).param("productSizeId", productSizeId));
    }

    public List<ProductFilter> find(String productTypeName, String productColourName, String productSizeName) {
        return jpaAccess.find(Query.create("from ProductFilter where status=1 and (productTypeName=:productTypeName or productTypeName is null) and (productColourName=:productColourName or productTypeName is null) and (productSizeName=:productSizeName or productSizeName is null)")
            .param("productTypeName", productTypeName).param("productColourName", productColourName).param("productSizeName", productSizeName));
    }


    public List<ProductFilter> list(ProductFilter productFilter, int offset, int fetchSize, String orderField, Boolean isDesc) {
        String vOrderField = Strings.isNullOrEmpty(orderField) ? "id" : orderField;
        Boolean vIsDesc = null == isDesc ? Boolean.FALSE : isDesc;
        QueryBuilder queryBuilder = QueryBuilder.query("from ProductFilter").skipEmptyFields()
            .append("status", productFilter.getStatus())
            .append("productTypeId", productFilter.getProductTypeId())
            .append("productColourId", productFilter.getProductColourId())
            .append("productSizeId", productFilter.getProductSizeId())
            .orderBy(vOrderField, vIsDesc);
        return jpaAccess.find(queryBuilder.build().from(offset).fetch(fetchSize));
    }

    public int count(ProductFilter productFilter) {
        QueryBuilder queryBuilder = QueryBuilder.query("select count(*) from ProductFilter").skipEmptyFields()
            .append("status", productFilter.getStatus())
            .append("productTypeId", productFilter.getProductTypeId())
            .append("productColourId", productFilter.getProductColourId())
            .append("productSizeId", productFilter.getProductSizeId());
        return Integer.parseInt(jpaAccess.find(queryBuilder.build()).get(0).toString());
    }
}