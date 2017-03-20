package com.xmnjm.controller;

import com.xmnjm.bean.ProductRequest;
import com.xmnjm.model.TortProducts;
import com.xmnjm.service.TortProductsService;
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
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author mandy.huang
 */
@RestController
@CrossOrigin(origins = "http://localhost:8082")
public class TortProductsController {
    @Inject
    TortProductsService tortProductsService;

    @RequestMapping(value = "/api/tort-products/{id}", method = RequestMethod.GET)
    @ResponseBody
    TortProducts findById(@PathVariable("id") Long id) {
        TortProducts tortProducts = tortProductsService.findById(id);
        return tortProducts;
    }

    @RequestMapping(value = "/api/tort-products", method = RequestMethod.POST)
    @ResponseBody
    TortProducts save(@Valid @RequestBody TortProducts tortProducts) {
        tortProductsService.save(tortProducts);
        return tortProducts;
    }

    @RequestMapping(value = "/api/tort-products/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    void delete(@PathVariable("id") Long id) {
        tortProductsService.delete(id);
    }

    @RequestMapping(value = "/api/tort-products", method = RequestMethod.PUT)
    @ResponseBody
    TortProducts update(@Valid @RequestBody TortProducts tortProducts) {
        tortProductsService.update(tortProducts);
        return tortProducts;
    }

    /**
     * 搜索
     *
     * @param productRequest
     * @param offset
     * @param fetchSize
     * @return
     */
    @RequestMapping(value = "/api/tort-products/list", method = RequestMethod.POST)
    @ResponseBody
    List<TortProducts> list(@Valid @RequestBody ProductRequest productRequest, @RequestParam(value = "offset", defaultValue = "0") int offset,
                            @RequestParam(value = "fetchSize", defaultValue = "20") int fetchSize) {
        return tortProductsService.list(productRequest, offset, fetchSize);
    }

    @RequestMapping(value = "/api/tort-products/count", method = RequestMethod.POST)
    @ResponseBody
    Long count(@Valid @RequestBody ProductRequest productRequest) {
        TortProducts tortProducts = new TortProducts();
        BeanUtils.copyProperties(productRequest, tortProducts);
        return tortProductsService.count(productRequest);
    }

    /**
     * 出单
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/tort-products/scenarioWhat/{id}", method = RequestMethod.GET)
    @ResponseBody
    TortProducts update(@PathVariable Long id, HttpServletRequest request) {
        TortProducts tortProducts = tortProductsService.findById(id);
        tortProductsService.update(tortProducts);
        return tortProducts;
    }


}
