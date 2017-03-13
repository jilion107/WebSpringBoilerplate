package com.xmnjm.bean;

import java.util.List;

/**
 * @author mandy.huang
 */
public class ProductsIdRequest {
    private List<Long> productIds;

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
}
