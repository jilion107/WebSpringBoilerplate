package com.xmnjm.bean;

import java.util.Date;
import java.util.List;

/**
 * @author mandy.huang
 */
public class ProductRequest {
    private String brand;
    private String asin;
    private Long productTypeId;
    private List<String> productColours;
    private List<String> productSizes;
    private Date startCreateTime;
    private Date endCreateTime;
    private Integer scenarioWhat;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public List<String> getProductColours() {
        return productColours;
    }

    public void setProductColours(List<String> productColours) {
        this.productColours = productColours;
    }

    public List<String> getProductSizes() {
        return productSizes;
    }

    public void setProductSizes(List<String> productSizes) {
        this.productSizes = productSizes;
    }

    public Date getStartCreateTime() {
        return startCreateTime;
    }

    public void setStartCreateTime(Date startCreateTime) {
        this.startCreateTime = startCreateTime;
    }

    public Date getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(Date endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public Integer getScenarioWhat() {
        return scenarioWhat;
    }

    public void setScenarioWhat(Integer scenarioWhat) {
        this.scenarioWhat = scenarioWhat;
    }
}
