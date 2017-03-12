package com.xmnjm.controller;

import com.xmnjm.bean.FindResult;
import com.xmnjm.bean.ProductRequest;
import com.xmnjm.model.TmpProducts;
import com.xmnjm.service.TmpProductsService;
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
import java.util.List;

/**
 * @author mandy.huang
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class TmpProductsController {
    @Inject
    TmpProductsService tmpProductsService;

    @RequestMapping(value = "/api/tmp-products/{id}", method = RequestMethod.GET)
    @ResponseBody
    TmpProducts findById(@PathVariable("id") Long id) {
        TmpProducts tmpProducts = tmpProductsService.findById(id);
        return tmpProducts;
    }

    @RequestMapping(value = "/api/tmp-products", method = RequestMethod.POST)
    @ResponseBody
    TmpProducts save(@Valid @RequestBody TmpProducts tmpProducts) {
        tmpProductsService.save(tmpProducts);
        return tmpProducts;
    }

    @RequestMapping(value = "/api/tmp-products/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    void delete(@PathVariable("id") Long id) {
        tmpProductsService.delete(id);
    }

    @RequestMapping(value = "/api/tmp-products", method = RequestMethod.PUT)
    @ResponseBody
    TmpProducts update(@Valid @RequestBody TmpProducts tmpProducts) {
        tmpProductsService.update(tmpProducts);
        return tmpProducts;
    }

    @RequestMapping(value = "/api/tmp-products/list", method = RequestMethod.POST)
    @ResponseBody
    FindResult<TmpProducts> list(@Valid @RequestBody ProductRequest productRequest, @RequestParam(value = "offset", defaultValue = "0") int offset,
                                 @RequestParam(value = "fetchSize", defaultValue = "20") int fetchSize) {
        TmpProducts tmpProducts = new TmpProducts();
        BeanUtils.copyProperties(productRequest, tmpProducts);
        List<TmpProducts> tmpProductses = tmpProductsService.list(tmpProducts, offset, fetchSize, "createTime", productRequest.getStartCreateTime(), productRequest.getEndCreateTime());
        Long total = tmpProductsService.count(tmpProducts, productRequest.getStartCreateTime(), productRequest.getEndCreateTime());
        return new FindResult<TmpProducts>(tmpProductses, offset, total);
    }

    @RequestMapping(value = "/api/tmp-products/count", method = RequestMethod.POST)
    @ResponseBody
    Long count(@Valid @RequestBody ProductRequest productRequest) {
        TmpProducts tmpProducts = new TmpProducts();
        BeanUtils.copyProperties(productRequest, tmpProducts);
        return tmpProductsService.count(tmpProducts, productRequest.getStartCreateTime(), productRequest.getEndCreateTime());
    }
}
