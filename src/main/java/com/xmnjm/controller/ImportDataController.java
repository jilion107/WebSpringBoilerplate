package com.xmnjm.controller;

import com.xmnjm.service.ImportDataService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.inject.Inject;

/**
 * @author mandy.huang
 */
@CrossOrigin(origins = "http://localhost:8082")
@RestController
public class ImportDataController {
    @Inject
    ImportDataService importDataService;

    @RequestMapping(value = "/api/upload", method = RequestMethod.POST)
    public void upload(@RequestParam("file") CommonsMultipartFile file, @RequestParam("scenarioWhat") Integer scenarioWhat, @RequestParam("userId") Integer userId) {
        try {
            importDataService.readXml(file, scenarioWhat, userId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
