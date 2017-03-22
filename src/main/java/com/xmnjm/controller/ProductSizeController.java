package com.xmnjm.controller;

import com.xmnjm.model.ProductFilter;
import com.xmnjm.model.ProductSize;
import com.xmnjm.service.ProductFilterService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mandy.huang
 */
@RestController
@CrossOrigin(origins = "http://localhost:8082")
public class ProductSizeController {
    @Inject
    ProductSizeService productSizeService;
    @Inject
    ProductFilterService productFilterService;

    @RequestMapping(value = "/api/sizes/{id}", method = RequestMethod.GET)
    @ResponseBody
    ProductSize findById(@PathVariable("id") Long id) {
        ProductSize productSize = productSizeService.findById(id);
        return productSize;
    }

    @RequestMapping(value = "/api/size", method = RequestMethod.POST)
    @ResponseBody
    Object save(@Valid @RequestBody ProductSize productSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ProductSize> productSizes = productSizeService.findBySizeName(productSize.getSizeName());
            if (!CollectionUtils.isEmpty(productSizes)) {
                result.put("result", "fail");
                result.put("message", "尺寸已经存在！");
            } else {
                productSizeService.save(productSize);
                result.put("result", "success");
                result.put("size",productSize);
            }
            return result;
        } catch (Exception ex) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
    }

    @RequestMapping(value = "/api/sizes/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    Object delete(@PathVariable("id") Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ProductFilter> productFilters = productFilterService.findByProductSizeId(id);
            if(CollectionUtils.isEmpty(productFilters)) {
                productSizeService.delete(id);
                result.put("result", "success");
            } else {
                result.put("result", "fail");
                result.put("message", "请先删除或修改关联的过滤！");
            }
            return result;
        } catch (Exception ex) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
    }

    @RequestMapping(value = "/api/sizes/{id}", method = RequestMethod.PUT)
    @ResponseBody
    Object update(@Valid @RequestBody ProductSize productSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            String sizeName = productSize.getSizeName();
            List<ProductSize> productSizes = productSizeService.findBySizeName(sizeName);
            if(!CollectionUtils.isEmpty(productSizes)) {
                result.put("result", "fail");
                result.put("message", "尺寸名不能重复！");
            } else {
                ProductSize prdctSz =productSizeService.findById(productSize.getId());
                prdctSz.setSizeName(productSize.getSizeName());
                productSizeService.update(prdctSz);
                result.put("result", "success");
                result.put("size", prdctSz);
            }
            return result;
        } catch (Exception ex) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
    }

    @RequestMapping(value = "/api/product-size", method = RequestMethod.GET)
    @ResponseBody
    List<ProductSize> list(@RequestParam String name) {
        ProductSize productSize = new ProductSize();
        productSize.setSizeName(name);
        productSize.setStatus(1);
        return productSizeService.list(productSize, 0, Integer.MAX_VALUE, null);
    }

    @RequestMapping(value = "/api/sizes", method = RequestMethod.GET)
    @ResponseBody
    List<ProductSize> list() {
        ProductSize productSize = new ProductSize();
        productSize.setStatus(1);
        return productSizeService.list(productSize, 0, Integer.MAX_VALUE, null);
    }

}
