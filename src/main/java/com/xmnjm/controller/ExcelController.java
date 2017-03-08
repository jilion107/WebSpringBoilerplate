package com.xmnjm.controller;

import com.xmnjm.service.ExcelOperateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * @author mandy.huang
 */
@RestController
public class ExcelController {
    @Inject
    ExcelOperateService excelOperateService;

    @RequestMapping(value = "/api/upload", method = RequestMethod.GET)
    public void upload() {
        String fileName = "D:\\fannieERP\\test.xls";
        excelOperateService.readXml(fileName, 0);
    }


}
