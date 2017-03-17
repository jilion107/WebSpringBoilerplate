package com.xmnjm.controller;

import com.xmnjm.model.ProductCategory;
import com.xmnjm.model.ProductFilter;
import com.xmnjm.service.ProductCategoryService;
import com.xmnjm.service.ProductFilterService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mandy.huang
 */
@RestController
@CrossOrigin(origins = "http://localhost:8082")
public class ProductCategoryController {
    @Inject
    ProductCategoryService productCategoryService;
    @Inject
    ProductFilterService productFilterService;

    @RequestMapping(value = "/api/categories/{id}", method = RequestMethod.GET)
    @ResponseBody
    ProductCategory findById(@PathVariable("id") Long id) {
        ProductCategory productCategory = productCategoryService.findById(id);
        return productCategory;
    }

    @RequestMapping(value = "/api/category", method = RequestMethod.POST)
    @ResponseBody
    Object save(@Valid @RequestBody ProductCategory productCategory) {
        Map<String, Object> result = new HashMap<>();
        List<ProductCategory> productCategories = productCategoryService.findByCategoryName(productCategory.getCategoryName());
        if (!CollectionUtils.isEmpty(productCategories)) {
            result.put("result", "fail");
            result.put("message", "类别已经存在！");
        } else {
            productCategoryService.save(productCategory);
            result.put("result", "success");
            result.put("category",productCategory);
        }
        return result;
    }

    @RequestMapping(value = "/api/categories/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    Object delete(@PathVariable("id") Long id) {
        Map<String, Object> result = new HashMap<>();
        List<ProductFilter> productFilters = productFilterService.findByProductCategoryId(id);
        if(CollectionUtils.isEmpty(productFilters)) {
            productCategoryService.delete(id);
            result.put("result", "success");
        } else {
            result.put("result", "fail");
            result.put("message", "请先删除或修改关联的过滤！");
        }
        return result;
    }

    @RequestMapping(value = "/api/categories/{id}", method = RequestMethod.PUT)
    @ResponseBody
    Object update(@Valid @RequestBody ProductCategory productCategory) {
        Map<String, Object> result = new HashMap<>();
        String categoryName = productCategory.getCategoryName();
        List<ProductCategory> productCategories = productCategoryService.findByCategoryName(categoryName);
        if(!CollectionUtils.isEmpty(productCategories)) {
            result.put("result", "fail");
            result.put("message", "类别名不能重复！");
        } else {
            ProductCategory prdctCtgry = productCategoryService.findById(productCategory.getId());
            prdctCtgry.setCategoryName(productCategory.getCategoryName());
            productCategoryService.update(prdctCtgry);
            result.put("result", "success");
            result.put("category", prdctCtgry);
        }
        return result;
    }

    @RequestMapping(value = "/api/product-type", method = RequestMethod.GET)
    @ResponseBody
    List<ProductCategory> list(@RequestParam String name) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName(name);
        productCategory.setStatus(1);
        return productCategoryService.list(productCategory, 0, Integer.MAX_VALUE, null);
    }

    @RequestMapping(value = "/api/categories", method = RequestMethod.GET)
    @ResponseBody
    List<ProductCategory> list() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setStatus(1);
        return productCategoryService.list(productCategory, 0, Integer.MAX_VALUE, null);
    }


}
