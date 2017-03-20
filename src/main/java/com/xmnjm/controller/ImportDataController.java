package com.xmnjm.controller;

import com.xmnjm.service.ExcelOperateService;
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
    ExcelOperateService excelOperateService;

    @RequestMapping(value = "/api/upload", method = RequestMethod.GET)
    public void upload(@RequestParam("file") CommonsMultipartFile file, @RequestParam("scenarioWhat") Integer scenarioWhat) {
        String fileName = "D:\\fannieERP\\妹娇卫衣采集器采集过(1).xls";
        excelOperateService.readXml(fileName, 0);
        //excelOperateService.readXml(file, scenarioWhat);
    }

    @RequestMapping(value = "/api/upload/test", method = RequestMethod.GET)
    public void upload() {
        String fileName = "D:\\fannieERP\\妹娇卫衣采集器采集过(1).xlsx";
        excelOperateService.readXml(fileName, 0);
    }

}
