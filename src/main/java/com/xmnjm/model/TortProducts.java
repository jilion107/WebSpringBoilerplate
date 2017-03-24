package com.xmnjm.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author mandy.huang
 *         侵权库
 */
@Entity
public class TortProducts {
    @Id
    @GeneratedValue
    private Long id;

    private String asin;

    private Integer userId;

    private Integer companyId;

    //品牌
    private String brand;

    //产品名称
    private String productName;

    //产品分类
    private String productTypeName;

    //产品尺寸
    private String productSize;

    //产品颜色
    private String productColour;

    //评论数
    private Integer commentNumber;

    //备注
    private String comment;

    //产品地址
    private String productUrl;

    //产品缩略图
    private String productThumbnail;

    //高清图地址
    private String productImageUrl;

    //出单标签
    private Integer scenarioWhat;

    //价格
    private Double price;

    private String buyBoxPrice;

    private Double offer;

    //国家
    private String country;

    //产品短描述
    private String shortDescription;

    //产品长描述
    private String longDescription;

    private String dataFirstAvailable;

    private String bestSellersRank;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    @Transient
    private Boolean checked = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductColour() {
        return productColour;
    }

    public void setProductColour(String productColour) {
        this.productColour = productColour;
    }

    public Integer getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(Integer commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductThumbnail() {
        return productThumbnail;
    }

    public void setProductThumbnail(String productThumbnail) {
        this.productThumbnail = productThumbnail;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public Integer getScenarioWhat() {
        return scenarioWhat;
    }

    public void setScenarioWhat(Integer scenarioWhat) {
        this.scenarioWhat = scenarioWhat;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBuyBoxPrice() {
        return buyBoxPrice;
    }

    public void setBuyBoxPrice(String buyBoxPrice) {
        this.buyBoxPrice = buyBoxPrice;
    }

    public Double getOffer() {
        return offer;
    }

    public void setOffer(Double offer) {
        this.offer = offer;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getDataFirstAvailable() {
        return dataFirstAvailable;
    }

    public void setDataFirstAvailable(String dataFirstAvailable) {
        this.dataFirstAvailable = dataFirstAvailable;
    }

    public String getBestSellersRank() {
        return bestSellersRank;
    }

    public void setBestSellersRank(String bestSellersRank) {
        this.bestSellersRank = bestSellersRank;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}