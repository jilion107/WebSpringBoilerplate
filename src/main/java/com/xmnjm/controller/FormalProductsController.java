package com.xmnjm.controller;

import com.google.gson.Gson;
import com.xmnjm.bean.FindResult;
import com.xmnjm.bean.FormalProductsSaveRequest;
import com.xmnjm.bean.ProductRequest;
import com.xmnjm.model.FormalProducts;
import com.xmnjm.service.FormalProductsService;
import org.springframework.beans.BeanUtils;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author mandy.huang
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class FormalProductsController {
    @Inject
    FormalProductsService formalProductsService;

    public static void main(String[] arg) {
        FormalProductsSaveRequest formalProductsSaveRequest = new FormalProductsSaveRequest();
        List<Long> tmpProductIds = new ArrayList<>();
        tmpProductIds.add(39l);
        tmpProductIds.add(40l);
        tmpProductIds.add(41l);
        formalProductsSaveRequest.setTmpProductIds(tmpProductIds);
        Gson gson = new Gson();
        System.out.print(gson.toJson(formalProductsSaveRequest));

    }

    @RequestMapping(value = "/api/formal-products/{id}", method = RequestMethod.GET)
    @ResponseBody
    FormalProducts findById(@PathVariable("id") Long id) {
        FormalProducts formalProducts = formalProductsService.findById(id);
        return formalProducts;
    }

    @RequestMapping(value = "/api/formal-products", method = RequestMethod.POST)
    @ResponseBody
    FormalProducts save(@Valid @RequestBody FormalProducts formalProducts) {
        formalProductsService.save(formalProducts);
        return formalProducts;
    }

    @RequestMapping(value = "/api/formal-products/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    void delete(@PathVariable("id") Long id) {
        formalProductsService.delete(id);
    }

    @RequestMapping(value = "/api/formal-products", method = RequestMethod.PUT)
    @ResponseBody
    FormalProducts update(@Valid @RequestBody FormalProducts formalProducts) {
        formalProductsService.update(formalProducts);
        return formalProducts;
    }

    @RequestMapping(value = "/api/formal-products/list", method = RequestMethod.POST)
    @ResponseBody
    FindResult<FormalProducts> list(@Valid @RequestBody ProductRequest productRequest, @RequestParam(value = "offset", defaultValue = "0") int offset,
                                    @RequestParam(value = "fetchSize", defaultValue = "20") int fetchSize) {
        FormalProducts formalProducts = new FormalProducts();
        BeanUtils.copyProperties(productRequest, formalProducts);
        List<FormalProducts> formalProductses = formalProductsService.list(productRequest, offset, fetchSize);
        Long total = formalProductsService.count(productRequest);
        return new FindResult<FormalProducts>(formalProductses, offset, total);
    }

    @RequestMapping(value = "/api/formal-products/count", method = RequestMethod.POST)
    @ResponseBody
    Long count(@Valid @RequestBody ProductRequest productRequest) {
        FormalProducts formalProducts = new FormalProducts();
        BeanUtils.copyProperties(productRequest, formalProducts);
        return formalProductsService.count(productRequest);
    }

    @RequestMapping(value = "/api/formal-products/by-tmp-product-id", method = RequestMethod.GET)
    @ResponseBody
    void save(@RequestParam("tmpProductId") Long tmpProductId) {
        formalProductsService.saveFromTmpProduct(tmpProductId);
    }

    /**
     * 批量添加正式库
     *
     * @param formalProductsSaveRequest
     */
    @RequestMapping(value = "/api/formal-products/multi", method = RequestMethod.POST)
    @ResponseBody
    void save(@Valid @RequestBody FormalProductsSaveRequest formalProductsSaveRequest) {
        List<Long> tmpProductIds = formalProductsSaveRequest.getTmpProductIds();
        for (int i = 0; i < tmpProductIds.size(); i++) {
            formalProductsService.saveFromTmpProduct(tmpProductIds.get(i));
        }
    }
}
