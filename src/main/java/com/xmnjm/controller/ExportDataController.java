package com.xmnjm.controller;

import com.xmnjm.bean.ExportDataRequest;
import com.xmnjm.service.ExportDataService;
import com.xmnjm.service.FormalProductsService;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mandy.huang
 */
@CrossOrigin(origins = "http://localhost:8082")
@RestController
public class ExportDataController {
    @Inject
    ExportDataService exportDataService;
    @Inject
    FormalProductsService formalProductsService;

    @RequestMapping(value = "/api/formal-products/export", method = RequestMethod.POST)
    Object export(@Valid @RequestBody ExportDataRequest exportDataRequest, HttpServletResponse response) {


        StringBuilder builder = exportDataService.genExportData(exportDataRequest);

        Map<String, Object> result = new HashMap<>();

        try {
            String path = ClassUtils.class.getClassLoader().getResource("").getPath();
            File tmpFile = new File(path);
            String parentPath = tmpFile.getParent();
            File tmpFile2 = new File(parentPath);
            String absPath = tmpFile2.getParent() + "\\poem.txt";

            File file = new File(absPath);
            FileOutputStream fos = new FileOutputStream(file);
            Writer writer = new OutputStreamWriter(fos, "utf-8");
            writer.write(builder.toString());
            writer.close();
            fos.close();
            result.put("result", "success");
            result.put("file", file);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
