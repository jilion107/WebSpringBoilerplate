package com.xmnjm.controller;

import com.xmnjm.model.Tort;
import com.xmnjm.service.TortService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * @author mandy.huang
 */
@RestController
public class TortController {
    @Inject
    TortService tortService;

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping("/api/tort/find/{id}")
    public
    @ResponseBody
    Tort findById(@PathVariable("id") Long id) {
        Tort tort = tortService.findById(id);
        return tort;
    }
}
