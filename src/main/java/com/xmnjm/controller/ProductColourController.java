package com.xmnjm.controller;

import com.xmnjm.model.ProductColour;
import com.xmnjm.model.ProductFilter;
import com.xmnjm.service.ProductColourService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mandy.huang
 */
@RestController
@CrossOrigin(origins = "http://localhost:8082")
public class ProductColourController {
    @Inject
    ProductColourService productColourService;
    @Inject
    ProductFilterService productFilterService;

    @RequestMapping(value = "/api/colours/{id}", method = RequestMethod.GET)
    @ResponseBody
    ProductColour findById(@PathVariable("id") Long id) {
        ProductColour productColour = productColourService.findById(id);
        return productColour;
    }

    @RequestMapping(value = "/api/colour", method = RequestMethod.POST)
    @ResponseBody
    Object save(@Valid @RequestBody ProductColour productColour) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ProductColour> productColours = productColourService.findByColourName(productColour.getColourName());
            if(!CollectionUtils.isEmpty(productColours)) {
                result.put("result", "fail");
                result.put("message", "颜色已经存在！");
            } else {
                productColourService.save(productColour);
                result.put("result", "success");
                result.put("colour",productColour);
            }
            return result;
        } catch (Exception ex) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
    }

    @RequestMapping(value = "/api/colours/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    Object delete(@PathVariable("id") Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ProductFilter> productFilters = productFilterService.findByProductColourId(id);
            if(CollectionUtils.isEmpty(productFilters)) {
                productColourService.delete(id);
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

    @RequestMapping(value = "/api/colours/{id}", method = RequestMethod.PUT)
    @ResponseBody
    Object update(@Valid @RequestBody ProductColour productColour) {
        Map<String, Object> result = new HashMap<>();
        try {
            String colourName = productColour.getColourName();
            List<ProductColour> productColours = productColourService.findByColourName(colourName);
            if(!CollectionUtils.isEmpty(productColours)) {
                result.put("result", "fail");
                result.put("message", "颜色名不能重复！");
            } else {
                ProductColour prdctClur =productColourService.findById(productColour.getId());
                prdctClur.setColourName(productColour.getColourName());
                productColourService.update(prdctClur);
                result.put("result", "success");
                result.put("colour", prdctClur);
            }
            return result;
        } catch (Exception ex) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
    }

    @RequestMapping(value = "/api/product-colour", method = RequestMethod.GET)
    @ResponseBody
    List<ProductColour> list(@RequestParam String name) {
        ProductColour productColour = new ProductColour();
        productColour.setColourName(name);
        productColour.setStatus(1);
        return productColourService.list(productColour, 0, Integer.MAX_VALUE, null);
    }

    @RequestMapping(value = "/api/colours", method = RequestMethod.GET)
    @ResponseBody
    List<ProductColour> list() {
        ProductColour productColour = new ProductColour();
        productColour.setStatus(1);
        return productColourService.list(productColour, 0, Integer.MAX_VALUE, null);
    }

}
