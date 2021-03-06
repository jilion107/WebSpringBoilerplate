package com.xmnjm.controller;

import com.xmnjm.bean.ProductRequest;
import com.xmnjm.bean.ProductsIdRequest;
import com.xmnjm.model.FormalProducts;
import com.xmnjm.model.TmpProducts;
import com.xmnjm.service.FormalProductsService;
import com.xmnjm.service.TmpProductsService;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
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
 *         正式库
 */
@RestController
@CrossOrigin(origins = "http://localhost:8082")
public class FormalProductsController {
    @Inject
    FormalProductsService formalProductsService;
    @Inject
    TmpProductsService tmpProductsService;

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
        //删除变体
        List<FormalProducts> formalProductses = formalProductsService.findByParent(id);
        if (!CollectionUtils.isEmpty(formalProductses)) {
            for (FormalProducts formalProducts : formalProductses) {
                formalProductsService.delete(formalProducts);
            }
        }
        //删除父节点
        formalProductsService.delete(id);

    }

    @RequestMapping(value = "/api/formal-products", method = RequestMethod.PUT)
    @ResponseBody
    FormalProducts update(@Valid @RequestBody FormalProducts formalProducts) {
        formalProductsService.update(formalProducts);
        return formalProducts;
    }

    /**
     * 搜索
     *
     * @param productRequest
     * @param offset
     * @param fetchSize
     * @return
     */
    @RequestMapping(value = "/api/formal-products/list", method = RequestMethod.POST)
    @ResponseBody
    List<FormalProducts> list(@Valid @RequestBody ProductRequest productRequest, @RequestParam(value = "offset", defaultValue = "0") int offset,
                              @RequestParam(value = "fetchSize", defaultValue = "20") int fetchSize) {
        return formalProductsService.list(productRequest, offset, fetchSize);
    }

    @RequestMapping(value = "/api/formal-products/count", method = RequestMethod.POST)
    @ResponseBody
    Long count(@Valid @RequestBody ProductRequest productRequest) {
        FormalProducts formalProducts = new FormalProducts();
        BeanUtils.copyProperties(productRequest, formalProducts);
        return formalProductsService.count(productRequest);
    }

    /**
     * 添加正式库
     *
     * @param tmpProductId
     */
    @RequestMapping(value = "/api/add-to-formal-products/{productTypeId}", method = RequestMethod.GET)
    @ResponseBody
    void save(@PathVariable("productTypeId") Long productTypeId, @RequestParam("tmpProductId") Long tmpProductId) {
        FormalProducts formalProducts = formalProductsService.saveFromTmpProduct(productTypeId, tmpProductId);

        //添加变体
        List<TmpProducts> tmpProductses = tmpProductsService.findByParent(tmpProductId);
        if (!CollectionUtils.isEmpty(tmpProductses)) {
            for (TmpProducts tmpProducts : tmpProductses) {
                formalProductsService.saveFromTmpProduct(formalProducts, tmpProducts);
            }
        }
    }

    /**
     * 批量添加正式库
     *
     * @param productsIdRequest
     */
    @RequestMapping(value = "/api/formal-products/batch/{productTypeId}", method = RequestMethod.POST)
    @ResponseBody
    void save(@Valid @RequestBody ProductsIdRequest productsIdRequest, @PathVariable("productTypeId") Long productTypeId) {
        List<Long> productIds = productsIdRequest.getProductIds();
        if (CollectionUtils.isEmpty(productIds)) return;
        for (int i = 0; i < productIds.size(); i++) {
            FormalProducts formalProducts = formalProductsService.saveFromTmpProduct(productTypeId, productIds.get(i));

            List<TmpProducts> tmpProductses = tmpProductsService.findByParent(productIds.get(i));
            if (!CollectionUtils.isEmpty(tmpProductses)) {
                for (TmpProducts tmpProducts : tmpProductses) {
                    formalProductsService.saveFromTmpProduct(formalProducts, tmpProducts);
                }
            }

        }
    }

    /**
     * 出单
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/formal-products/scenarioWhat/{id}", method = RequestMethod.GET)
    @ResponseBody
    FormalProducts update(@PathVariable Long id) {
        FormalProducts formalProducts = formalProductsService.findById(id);
        if (formalProducts == null) return null;

        List<FormalProducts> formalProductses = formalProductsService.findByParent(id);
        if (!CollectionUtils.isEmpty(formalProductses)) {
            for (FormalProducts rp : formalProductses) {
                rp.setScenarioWhat(1);
                formalProductsService.update(rp);
            }
        }

        formalProducts.setScenarioWhat(1);
        formalProductsService.update(formalProducts);
        return formalProducts;
    }

}
