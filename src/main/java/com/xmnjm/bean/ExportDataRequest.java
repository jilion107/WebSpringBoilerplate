package com.xmnjm.bean;

import java.util.List;

/**
 * @author mandy.huang
 */
public class ExportDataRequest {
    //汇出条数
    private Integer total;
    //最小库存
    private int minQuantity;
    //最大库存
    private int maxQuantity;
    //不可重复汇出天数
    private int beforeDays;
    //价格
    private Double prices;
    //发货天数
    private int deliveryDays;
    private List<Long> productIds;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public int getBeforeDays() {
        return beforeDays;
    }

    public void setBeforeDays(int beforeDays) {
        this.beforeDays = beforeDays;
    }

    public Double getPrices() {
        return prices;
    }

    public void setPrices(Double prices) {
        this.prices = prices;
    }

    public int getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(int deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
}
