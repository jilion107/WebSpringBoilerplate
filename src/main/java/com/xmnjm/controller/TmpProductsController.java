package com.xmnjm.controller;

import com.xmnjm.bean.ProductRequest;
import com.xmnjm.model.TmpProducts;
import com.xmnjm.service.TmpProductsService;
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
 *         临时库管理
 */
@CrossOrigin(origins = "http://localhost:8082")
@RestController
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

    /**
     * 搜索
     *
     * @param productRequest
     * @param offset
     * @param fetchSize
     * @return
     */
    @RequestMapping(value = "/api/tmp-products/list", method = RequestMethod.POST)
    @ResponseBody
    List<TmpProducts> list(@Valid @RequestBody ProductRequest productRequest, @RequestParam(value = "offset", defaultValue = "0") int offset,
                           @RequestParam(value = "fetchSize", defaultValue = "20") int fetchSize) {
        return tmpProductsService.list(productRequest, offset, fetchSize);
    }

    @RequestMapping(value = "/api/tmp-products/count", method = RequestMethod.POST)
    @ResponseBody
    Long count(@Valid @RequestBody ProductRequest productRequest) {
        return tmpProductsService.count(productRequest);
    }
}
