package com.xmnjm.controller;

import com.xmnjm.service.ExcelOperateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.inject.Inject;

/**
 * @author mandy.huang
 */
@RestController
public class ImportDataController {
    @Inject
    ExcelOperateService excelOperateService;

    @RequestMapping(value = "/api/upload", method = RequestMethod.GET)
    public void upload(@RequestParam("file") CommonsMultipartFile file, @RequestParam("scenarioWhat") Integer scenarioWhat) {
      //  String fileName = "D:\\fannieERP\\test1.xls";
        excelOperateService.readXml(file, scenarioWhat);
    }


}
