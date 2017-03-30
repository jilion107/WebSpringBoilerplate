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

    public List<ProductFilter> findByProductCategoryId(Long productCategoryId) {
        return jpaAccess.find(Query.create("from ProductFilter where status=1 and productCategoryId=:productCategoryId").param("productCategoryId", productCategoryId));
    }

    public List<ProductFilter> findByProductColourId(Long productColourId) {
        return jpaAccess.find(Query.create("from ProductFilter where status=1 and productColourId=:productColourId").param("productColourId", productColourId));
    }

    public List<ProductFilter> findByProductSizeId(Long productSizeId) {
        return jpaAccess.find(Query.create("from ProductFilter where status=1 and productSizeId=:productSizeId").param("productSizeId", productSizeId));
    }

    public List<ProductFilter> find(Long productCategoryId, Long productColourId, Long productSizeId) {
        return jpaAccess.find(Query.create("from ProductFilter where status=1 and productCategoryId=:productCategoryId and productColourId=:productColourId and productSizeId=:productSizeId").param("productCategoryId", productCategoryId).param("productColourId", productColourId).param("productSizeId", productSizeId));
    }

    public List<ProductFilter> find(String productCategoryName, String productColourName, String productSizeName) {
        return jpaAccess.find(Query.create("from ProductFilter where status=1 and (productCategoryName=:productCategoryName or productCategoryName is null) and (productColourName=:productColourName or productColourName is null) and (productSizeName=:productSizeName or productSizeName is null)")
            .param("productCategoryName", productCategoryName).param("productColourName", productColourName).param("productSizeName", productSizeName));
    }


    public List<ProductFilter> list(ProductFilter productFilter, int offset, int fetchSize, String orderField, Boolean isDesc) {
        String vOrderField = Strings.isNullOrEmpty(orderField) ? "id" : orderField;
        Boolean vIsDesc = null == isDesc ? Boolean.FALSE : isDesc;
        QueryBuilder queryBuilder = QueryBuilder.query("from ProductFilter").skipEmptyFields()
            .append("status", productFilter.getStatus())
            .append("productCategoryId", productFilter.getProductCategoryId())
            .append("productColourId", productFilter.getProductColourId())
            .append("productSizeId", productFilter.getProductSizeId())
            .orderBy(vOrderField, vIsDesc);
        return jpaAccess.find(queryBuilder.build().from(offset).fetch(fetchSize));
    }

    public int count(ProductFilter productFilter) {
        QueryBuilder queryBuilder = QueryBuilder.query("select count(*) from ProductFilter").skipEmptyFields()
            .append("status", productFilter.getStatus())
            .append("productCategoryId", productFilter.getProductCategoryId())
            .append("productColourId", productFilter.getProductColourId())
            .append("productSizeId", productFilter.getProductSizeId());
        return Integer.parseInt(jpaAccess.find(queryBuilder.build()).get(0).toString());
    }

    public void delete(ProductFilter productFilter) {
        jpaAccess.delete(productFilter);
    }

    public List<ProductFilter> list(String queryField, String queryStr) {
        QueryBuilder queryBuilder = QueryBuilder.query("from ProductFilter").skipEmptyFields()
                .append(queryField, queryStr);
        return jpaAccess.find(queryBuilder.build());
    }
}