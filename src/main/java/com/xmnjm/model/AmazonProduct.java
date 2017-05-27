package com.xmnjm.model;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jilion.chen on 5/4/2017.
 */
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name="amazonProduct")
public class AmazonProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String asin;

    @Column(name = "parentasin")
    private String parentAsin;

    private String url;

    private String title;

    @Column(name = "sizeype")
    private String sizeType;

    private String color;

    @Column(name = "thumbnaiurl")
    private String thumbnailUrl;

    @Column(name = "imageurl")
    private String imageUrl;

    private String price;

    private Integer comments;

    @Column(name = "hasothersellers")
    private boolean hasOtherSellers;

    @Column(name = "othersellersamount")
    private Integer otherSellersAmount;

    @Column(name = "createtime")
    private Date createdTime;

    @Column(name = "fromapi")
    private boolean fromApi;

    @Column(name = "base64url")
    private String base64Url;

    @Column(name = "keyword")
    private String keyword;

    public AmazonProduct() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getParentAsin() {
        return parentAsin;
    }

    public void setParentAsin(String parentAsin) {
        this.parentAsin = parentAsin;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public boolean isHasOtherSellers() {
        return hasOtherSellers;
    }

    public void setHasOtherSellers(boolean hasOtherSellers) {
        this.hasOtherSellers = hasOtherSellers;
    }

    public Integer getOtherSellersAmount() {
        return otherSellersAmount;
    }

    public void setOtherSellersAmount(Integer otherSellersAmount) {
        this.otherSellersAmount = otherSellersAmount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isFromApi() {
        return fromApi;
    }

    public void setFromApi(boolean fromApi) {
        this.fromApi = fromApi;
    }

    public String getBase64Url() {
        return base64Url;
    }

    public void setBase64Url(String base64Url) {
        this.base64Url = base64Url;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
