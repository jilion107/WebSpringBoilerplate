package com.xmnjm.controller;

import com.xmnjm.dao.ProductFilterDao;
import com.xmnjm.model.ProductCategory;
import com.xmnjm.model.ProductColour;
import com.xmnjm.model.ProductFilter;
import com.xmnjm.model.ProductSize;
import com.xmnjm.service.ProductCategoryService;
import com.xmnjm.service.ProductColourService;
import com.xmnjm.service.ProductFilterService;
import com.xmnjm.service.ProductSizeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mandy.huang
 */
@RestController
@CrossOrigin(origins = "http://localhost:8082")
public class ProductFilterController {
    @Inject
    ProductFilterService productFilterService;
    @Inject
    ProductCategoryService productCategoryService;
    @Inject
    ProductColourService productColourService;
    @Inject
    ProductSizeService productSizeService;

    @Autowired
    ProductFilterDao productFilterDao;

    @RequestMapping(value = "/api/filters/{id}", method = RequestMethod.GET)
    @ResponseBody
    ProductFilter findById(@PathVariable("id") Long id) {
        ProductFilter productFilter = productFilterService.findById(id);
        return productFilter;
    }

    @RequestMapping(value = "/api/filter", method = RequestMethod.POST)
    @ResponseBody
    Object save(@Valid @RequestBody ProductFilter productFilter) {
        Map<String, Object> result = new HashMap<>();
        try{
            List<ProductFilter> productFilters = productFilterService.find(productFilter.getProductCategoryId(), productFilter.getProductColourId(), productFilter.getProductSizeId());
            if (!CollectionUtils.isEmpty(productFilters)) {
                result.put("result", "fail");
                result.put("message", "过滤器已经存在！");
            } else {
                ProductCategory category = productCategoryService.findById(productFilter.getProductCategoryId());
                ProductColour colour = productColourService.findById(productFilter.getProductColourId());
                ProductSize size = productSizeService.findById(productFilter.getProductSizeId());
                if(category != null) {
                    productFilter.setProductCategoryName(category.getCategoryName());
                }
                if(colour != null) {
                    productFilter.setProductColourName(colour.getColourName());
                }
                if(size != null) {
                    productFilter.setProductSizeName(size.getSizeName());
                }
                productFilterService.save(productFilter);
                result.put("result", "success");
                result.put("filter",productFilter);
            }
            return result;
        } catch (Exception ex) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
    }

    @RequestMapping(value = "/api/filters/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    Object delete(@PathVariable("id") Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            productFilterService.delete(id);
            result.put("result", "success");
            return result;
        } catch (Exception ex) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
    }

    @RequestMapping(value = "/api/filters/{id}", method = RequestMethod.PUT)
    @ResponseBody
    Object update(@Valid @RequestBody ProductFilter productFilter) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ProductFilter> productFilters = productFilterService.find(productFilter.getProductCategoryId(), productFilter.getProductColourId(), productFilter.getProductSizeId());
            if(!CollectionUtils.isEmpty(productFilters)) {
                result.put("result", "fail");
                result.put("message", "过滤器已经存在！");
            } else {
                ProductFilter prdctFltr = productFilterService.findById(productFilter.getId());
                ProductCategory category = productCategoryService.findById(productFilter.getProductCategoryId());
                ProductColour colour = productColourService.findById(productFilter.getProductColourId());
                ProductSize size = productSizeService.findById(productFilter.getProductSizeId());
                if(category != null) {
                    prdctFltr.setProductCategoryId(productFilter.getProductCategoryId());
                    prdctFltr.setProductCategoryName(category.getCategoryName());
                }
                if(colour != null) {
                    prdctFltr.setProductColourId(productFilter.getProductColourId());
                    prdctFltr.setProductColourName(colour.getColourName());
                }
                if(size != null) {
                    prdctFltr.setProductSizeId(productFilter.getProductSizeId());
                    prdctFltr.setProductSizeName(size.getSizeName());
                }
                productFilterService.update(prdctFltr);
                result.put("result", "success");
                result.put("filter", prdctFltr);
            }
            return result;

        } catch (Exception ex) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
    }

    @RequestMapping(value = "/api/product-filter/type/{productTypeId}", method = RequestMethod.GET)
    @ResponseBody
    List<ProductFilter> list(@PathVariable("productTypeId") Long productTypeId) {
        ProductFilter productFilter = new ProductFilter();
        productFilter.setProductCategoryId(productTypeId);
        productFilter.setStatus(1);
        return productFilterService.list(productFilter, 0, Integer.MAX_VALUE, null);
    }

    @RequestMapping(value = "/api/filters/", method = RequestMethod.GET)
    @ResponseBody
    List<ProductFilter> list() {
        ProductFilter productFilter = new ProductFilter();
        productFilter.setStatus(1);
        return productFilterService.list(productFilter, 0, Integer.MAX_VALUE, null);
    }

    @RequestMapping(value= "/api/filters", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(value = "productCategoryName", required = false) String productCategoryName, @RequestParam(value = "productSizeName", required = false) String productSizeName, @RequestParam(value = "productColourName", required = false) String productColourName) {
        Map<String, Object> result = new HashMap<>();
        List<ProductFilter> productFilters = null;
        try {
            if(productCategoryName != "" && productCategoryName != null) {
                productFilters = productFilterDao.list("productCategoryName",productCategoryName);
            } else if(productSizeName != "" && productSizeName != null) {
                productFilters = productFilterDao.list("productSizeName",productSizeName);
            } else if(productColourName != "" && productColourName != null) {
                productFilters = productFilterDao.list("productColourName",productColourName);
            } else {
                ProductFilter productFilter = new ProductFilter();
                productFilter.setStatus(1);
                productFilters = productFilterService.list(productFilter, 0, Integer.MAX_VALUE, null);
            }
            result.put("result", "success");
            result.put("filters", productFilters);
            return result;
        } catch (Exception e) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
    }

}
