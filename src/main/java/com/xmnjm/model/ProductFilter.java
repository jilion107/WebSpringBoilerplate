package com.xmnjm.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author mandy.huang
 *         过滤表
 */
@Entity
public class ProductFilter {
    @Id
    @GeneratedValue
    private Long id;

    //产品分类Id
    private Long productTypeId;

    //产品分类名称
    private String productTypeName;

    //产品颜色ID
    private Long productColourId;

    //产品颜色名称
    private String productColourName;

    //产品尺寸ID
    private Long productSizeId;

    //产品尺寸名称
    private String productSizeName;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public Long getProductColourId() {
        return productColourId;
    }

    public void setProductColourId(Long productColourId) {
        this.productColourId = productColourId;
    }

    public String getProductColourName() {
        return productColourName;
    }

    public void setProductColourName(String productColourName) {
        this.productColourName = productColourName;
    }

    public Long getProductSizeId() {
        return productSizeId;
    }

    public void setProductSizeId(Long productSizeId) {
        this.productSizeId = productSizeId;
    }

    public String getProductSizeName() {
        return productSizeName;
    }

    public void setProductSizeName(String productSizeName) {
        this.productSizeName = productSizeName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}