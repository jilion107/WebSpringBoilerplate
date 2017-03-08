package com.xmnjm.controller;

import com.xmnjm.model.Tort;
import com.xmnjm.service.TortService;
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
@CrossOrigin(origins = "http://localhost:8080")
public class TortController {
    @Inject
    TortService tortService;

    @RequestMapping(value = "/api/tort/{id}", method = RequestMethod.GET)
    @ResponseBody
    Tort findById(@PathVariable("id") Long id) {
        Tort tort = tortService.findById(id);
        return tort;
    }

    @RequestMapping(value = "/api/tort", method = RequestMethod.POST)
    @ResponseBody
    Tort save(@Valid @RequestBody Tort tort) {
        List<Tort> torts = tortService.findByName(tort.getName());
        if (!CollectionUtils.isEmpty(torts)) return null;
        tortService.save(tort);
        return tort;
    }

    @RequestMapping(value = "/api/tort/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    void delete(@PathVariable("id") Long id) {
        tortService.delete(id);
    }

    @RequestMapping(value = "/api/tort", method = RequestMethod.PUT)
    @ResponseBody
    Tort update(@Valid @RequestBody Tort tort) {
        tortService.update(tort);
        return tort;
    }

    @RequestMapping(value = "/api/tort", method = RequestMethod.GET)
    @ResponseBody
    List<Tort> list(@RequestParam String name) {
        Tort tort = new Tort();
        tort.setName(name);
        tort.setStatus(1);
        return tortService.list(tort, 0, Integer.MAX_VALUE, null);
    }

    @RequestMapping(value = "/api/tort", method = RequestMethod.GET)
    @ResponseBody
    List<Tort> list() {
        Tort tort = new Tort();
        tort.setStatus(1);
        return tortService.list(tort, 0, Integer.MAX_VALUE, null);
    }
}
