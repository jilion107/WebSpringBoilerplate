package com.xmnjm.controller;

import com.xmnjm.model.ProductColour;
import com.xmnjm.service.ProductColourService;
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
@CrossOrigin(origins = "http://localhost:8082")
public class ProductColourController {
    @Inject
    ProductColourService productColourService;

    @RequestMapping(value = "/api/product-colour/{id}", method = RequestMethod.GET)
    @ResponseBody
    ProductColour findById(@PathVariable("id") Long id) {
        ProductColour productColour = productColourService.findById(id);
        return productColour;
    }

    @RequestMapping(value = "/api/product-colour", method = RequestMethod.POST)
    @ResponseBody
    ProductColour save(@Valid @RequestBody ProductColour productColour) {
        List<ProductColour> productColours = productColourService.findByName(productColour.getName());
        if (!CollectionUtils.isEmpty(productColours)) return null;
        productColourService.save(productColour);
        return productColour;
    }

    @RequestMapping(value = "/api/product-colour/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    void delete(@PathVariable("id") Long id) {
        productColourService.delete(id);
    }

    @RequestMapping(value = "/api/product-colour", method = RequestMethod.PUT)
    @ResponseBody
    ProductColour update(@Valid @RequestBody ProductColour productColour) {
        productColourService.update(productColour);
        return productColour;
    }

    @RequestMapping(value = "/api/product-colour", method = RequestMethod.GET)
    @ResponseBody
    List<ProductColour> list(@RequestParam String name) {
        ProductColour productColour = new ProductColour();
        productColour.setName(name);
        productColour.setStatus(1);
        return productColourService.list(productColour, 0, Integer.MAX_VALUE, null);
    }

    @RequestMapping(value = "/api/product-colour/all", method = RequestMethod.GET)
    @ResponseBody
    List<ProductColour> list() {
        ProductColour productColour = new ProductColour();
        productColour.setStatus(1);
        return productColourService.list(productColour, 0, Integer.MAX_VALUE, null);
    }

}
