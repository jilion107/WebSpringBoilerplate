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
    private String productTypeName;
    private List<String> productColours;
    private List<String> productSizes;
    private String startCreateTime;
    private String endCreateTime;
    private Integer scenarioWhat;
    private Date endUpdateTime;
    private Integer userId;
    private Integer companyId;

    public Date getEndUpdateTime() {
        return endUpdateTime;
    }

    public void setEndUpdateTime(Date endUpdateTime) {
        this.endUpdateTime = endUpdateTime;
    }

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

    public String getStartCreateTime() {
        return startCreateTime;
    }

    public void setStartCreateTime(String startCreateTime) {
        this.startCreateTime = startCreateTime;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public Integer getScenarioWhat() {
        return scenarioWhat;
    }

    public void setScenarioWhat(Integer scenarioWhat) {
        this.scenarioWhat = scenarioWhat;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
