package com.xmnjm.controller;

import com.xmnjm.model.ProductFilter;
import com.xmnjm.service.ProductFilterService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
public class ProductFilterController {
    @Inject
    ProductFilterService productFilterService;

    @RequestMapping(value = "/api/product-filter/{id}", method = RequestMethod.GET)
    @ResponseBody
    ProductFilter findById(@PathVariable("id") Long id) {
        ProductFilter productFilter = productFilterService.findById(id);
        return productFilter;
    }

    @RequestMapping(value = "/api/product-filter", method = RequestMethod.POST)
    @ResponseBody
    ProductFilter save(@Valid @RequestBody ProductFilter productFilter) {
        List<ProductFilter> productFilters = productFilterService.find(productFilter.getProductTypeId(), productFilter.getProductColourId(), productFilter.getProductSizeId());
        if (!CollectionUtils.isEmpty(productFilters)) return null;
        productFilterService.save(productFilter);
        return productFilter;
    }

    @RequestMapping(value = "/api/product-filter/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    void delete(@PathVariable("id") Long id) {
        productFilterService.delete(id);
    }

    @RequestMapping(value = "/api/product-filter", method = RequestMethod.PUT)
    @ResponseBody
    ProductFilter update(@Valid @RequestBody ProductFilter productFilter) {
        productFilterService.update(productFilter);
        return productFilter;
    }

    @RequestMapping(value = "/api/product-filter/type/{productTypeId}", method = RequestMethod.GET)
    @ResponseBody
    List<ProductFilter> list(@PathVariable("productTypeId") Long productTypeId) {
        ProductFilter productFilter = new ProductFilter();
        productFilter.setProductTypeId(productTypeId);
        productFilter.setStatus(1);
        return productFilterService.list(productFilter, 0, Integer.MAX_VALUE, null);
    }

    @RequestMapping(value = "/api/product-filter/all", method = RequestMethod.GET)
    @ResponseBody
    List<ProductFilter> list() {
        ProductFilter productFilter = new ProductFilter();
        productFilter.setStatus(1);
        return productFilterService.list(productFilter, 0, Integer.MAX_VALUE, null);
    }

}
