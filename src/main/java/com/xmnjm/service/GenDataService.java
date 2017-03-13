package com.xmnjm.service;

import com.xmnjm.bean.ProductRequest;
import com.xmnjm.dbcommon.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author mandy.huang
 */
@Repository
public class GenDataService {


    public Query genQuery(ProductRequest productRequest, StringBuilder builder) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.hasText(productRequest.getBrand())) {
            builder.append("and brand like :brand ");
            params.put("brand", '%' + productRequest.getBrand() + '%');
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

}
