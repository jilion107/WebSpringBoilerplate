package com.xmnjm.controller;

import com.xmnjm.model.ProductSize;
import com.xmnjm.service.ProductSizeService;
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
public class ProductSizeController {
    @Inject
    ProductSizeService productSizeService;

    @RequestMapping(value = "/api/product-size/{id}", method = RequestMethod.GET)
    @ResponseBody
    ProductSize findById(@PathVariable("id") Long id) {
        ProductSize productSize = productSizeService.findById(id);
        return productSize;
    }

    @RequestMapping(value = "/api/product-size", method = RequestMethod.POST)
    @ResponseBody
    ProductSize save(@Valid @RequestBody ProductSize productSize) {
        List<ProductSize> productSizes = productSizeService.findByName(productSize.getName());
        if (!CollectionUtils.isEmpty(productSizes)) return null;
        productSizeService.save(productSize);
        return productSize;
    }

    @RequestMapping(value = "/api/product-size/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    void delete(@PathVariable("id") Long id) {
        productSizeService.delete(id);
    }

    @RequestMapping(value = "/api/product-size", method = RequestMethod.PUT)
    @ResponseBody
    ProductSize update(@Valid @RequestBody ProductSize productSize) {
        productSizeService.update(productSize);
        return productSize;
    }

    @RequestMapping(value = "/api/product-size", method = RequestMethod.GET)
    @ResponseBody
    List<ProductSize> list(@RequestParam String name) {
        ProductSize productSize = new ProductSize();
        productSize.setName(name);
        productSize.setStatus(1);
        return productSizeService.list(productSize, 0, Integer.MAX_VALUE, null);
    }

    @RequestMapping(value = "/api/product-size/all", method = RequestMethod.GET)
    @ResponseBody
    List<ProductSize> list() {
        ProductSize productSize = new ProductSize();
        productSize.setStatus(1);
        return productSizeService.list(productSize, 0, Integer.MAX_VALUE, null);
    }

}
