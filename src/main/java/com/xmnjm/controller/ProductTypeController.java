package com.xmnjm.controller;

import com.xmnjm.model.ProductType;
import com.xmnjm.service.ProductTypeService;
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
import java.util.List;

/**
 * @author mandy.huang
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class ProductTypeController {
    @Inject
    ProductTypeService productTypeService;

    @RequestMapping(value = "/api/product-type/{id}", method = RequestMethod.GET)
    @ResponseBody
    ProductType findById(@PathVariable("id") Long id) {
        ProductType productType = productTypeService.findById(id);
        return productType;
    }

    @RequestMapping(value = "/api/product-type", method = RequestMethod.POST)
    @ResponseBody
    ProductType save(@Valid @RequestBody ProductType productType) {
        List<ProductType> productTypes = productTypeService.findByName(productType.getName());
        if (!CollectionUtils.isEmpty(productTypes)) return null;
        productTypeService.save(productType);
        return productType;
    }

    @RequestMapping(value = "/api/product-type/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    void delete(@PathVariable("id") Long id) {
        productTypeService.delete(id);
    }

    @RequestMapping(value = "/api/product-type", method = RequestMethod.PUT)
    @ResponseBody
    ProductType update(@Valid @RequestBody ProductType productType) {
        productTypeService.update(productType);
        return productType;
    }

    @RequestMapping(value = "/api/product-type", method = RequestMethod.GET)
    @ResponseBody
    List<ProductType> list(@RequestParam String name) {
        ProductType productType = new ProductType();
        productType.setName(name);
        productType.setStatus(1);
        return productTypeService.list(productType, 0, Integer.MAX_VALUE, null);
    }

    @RequestMapping(value = "/api/product-type/all", method = RequestMethod.GET)
    @ResponseBody
    List<ProductType> list() {
        ProductType productType = new ProductType();
        productType.setStatus(1);
        return productTypeService.list(productType, 0, Integer.MAX_VALUE, null);
    }


}
