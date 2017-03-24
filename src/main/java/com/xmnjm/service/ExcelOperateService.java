package com.xmnjm.service;

import com.xmnjm.bean.Products;
import com.xmnjm.model.FormalProducts;
import com.xmnjm.model.ProductFilter;
import com.xmnjm.model.TmpProducts;
import com.xmnjm.model.TortProducts;
import com.xmnjm.model.TortWord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * @author mandy.huang
 */
@Service
public class ExcelOperateService {
    @Inject
    TmpProductsService tmpProductsService;
    @Inject
    TortProductsService tortProductsService;
    @Inject
    FormalProductsService formalProductsService;
    @Inject
    ProductFilterService productFilterService;
    @Inject
    TortWordService tortWordService;

    public void readXml(CommonsMultipartFile file, Integer scenarioWhat, Integer userId, Integer companyId) {
        boolean isE2007 = false;    //判断是否是excel2007格式
        if (file.getOriginalFilename().endsWith("xlsx"))
            isE2007 = true;
        InputStream input = null;
        Workbook wb = null;
        try {
            input = file.getInputStream();  //建立输入流
            //根据文件格式(2003或者2007)来初始化
            if (isE2007)
                wb = new XSSFWorkbook(input);
            else
                wb = new HSSFWorkbook(input);
            Sheet sheet = wb.getSheetAt(0);     //获得第一个表单
            Iterator<Row> rows = sheet.rowIterator(); //获得第一个表单的迭代器
            rows.next();//第一行不读取
            while (rows.hasNext()) {
                try {
                    Row row = rows.next();  //获得行数据

                    //当ASIN和型号两单位格没有数据里跳出行循环
                    if ((row.getCell(3) == null || !StringUtils.hasText(row.getCell(3).getStringCellValue())) && (row.getCell(9) == null || !StringUtils.hasText(row.getCell(9).getStringCellValue()))) {
                        break;
                    }

                    //当型号为Parent,不读此行数据
                    if (row.getCell(9) == null || !StringUtils.hasText(row.getCell(9).getStringCellValue()) || "Parent".equals(row.getCell(9).getStringCellValue())) {
                        continue;
                    }

                    Products products = new Products();

                    //如果此ASIN数据已经存在数据库里，直接更新
                    String asin = row.getCell(3).getStringCellValue();
                    TmpProducts tmpProducts = tmpProductsService.findByAsin(asin);
                    if (tmpProducts != null && tmpProducts.getId() != null) {
                        BeanUtils.copyProperties(tmpProducts, products);
                        this.setProduct(products, row, scenarioWhat);
                        BeanUtils.copyProperties(products, tmpProducts);
                        tmpProductsService.update(tmpProducts);
                        continue;
                    }


                    TortProducts tortProducts = tortProductsService.findByAsin(asin);
                    if (tortProducts != null && tortProducts.getId() != null) {
                        BeanUtils.copyProperties(tortProducts, products);
                        this.setProduct(products, row, scenarioWhat);
                        BeanUtils.copyProperties(products, tortProducts);
                        tortProductsService.update(tortProducts);
                        continue;
                    }


                    FormalProducts formalProducts = formalProductsService.findByAsin(asin);
                    if (formalProducts != null && formalProducts.getId() != null) {
                        BeanUtils.copyProperties(formalProducts, products);
                        this.setProduct(products, row, scenarioWhat);
                        BeanUtils.copyProperties(products, formalProducts);
                        formalProductsService.update(formalProducts);
                        continue;
                    }

                    //判断是否过滤此数据
                    boolean isFilter = this.isFilter(row);
                    if (isFilter) continue;

                    this.setProduct(products, row, scenarioWhat);

                    //过滤品牌
                    boolean isTort = this.isTort(row);
                    if (isTort) {
                        tortProducts = new TortProducts();
                        BeanUtils.copyProperties(products, tortProducts);
                        tortProducts.setUserId(userId);
                        tortProducts.setCompanyId(companyId);
                        tortProductsService.save(tortProducts);
                    } else {
                        tmpProducts = new TmpProducts();
                        BeanUtils.copyProperties(products, tmpProducts);
                        tmpProducts.setUserId(userId);
                        tmpProducts.setCompanyId(companyId);
                        tmpProductsService.save(tmpProducts);
                    }
                } catch (Exception ex) {
                    continue;
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (wb != null) wb.close();
                if (input != null) input.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setProduct(Products products, Row row, Integer scenarioWhat) {
        products.setScenarioWhat(scenarioWhat);
        //品牌
        if (row.getCell(1) != null && StringUtils.hasText(row.getCell(1).toString())) products.setBrand(row.getCell(1).toString().trim());
        //产品名称
        if (row.getCell(2) != null && StringUtils.hasText(row.getCell(2).toString())) products.setProductName(row.getCell(2).toString().trim());
        //ASIN
        if (row.getCell(3) != null && StringUtils.hasText(row.getCell(3).toString())) products.setAsin(row.getCell(3).toString().trim());
        //分类
        if (row.getCell(4) != null && StringUtils.hasText(row.getCell(4).toString())) products.setProductTypeName(row.getCell(4).toString().trim());
        //价格
        if (row.getCell(5) != null && StringUtils.hasText(row.getCell(5).toString())) {
            try {
                products.setPrice(Double.valueOf(row.getCell(5).toString().trim()));
            } catch (Exception e) {

            }
        }
        //BuyBox价格
        if (row.getCell(6) != null && StringUtils.hasText(row.getCell(6).toString())) {
            try {
                products.setBuyBoxPrice(row.getCell(6).toString().trim());
            } catch (Exception e) {

            }
        }
        //Offer
        if (row.getCell(7) != null && StringUtils.hasText(row.getCell(7).toString())) {
            try {
                products.setOffer(Double.valueOf(row.getCell(7).toString().trim()));
            } catch (Exception e) {

            }
        }

        //评论数
        if (row.getCell(8) != null && StringUtils.hasText(row.getCell(8).toString())) {
            try {
                products.setCommentNumber(Float.valueOf(row.getCell(8).toString().trim()).intValue());
            } catch (Exception e) {

            }
        } else {
            products.setCommentNumber(0);
        }

        //型号
        if (row.getCell(9) != null && StringUtils.hasText(row.getCell(9).toString())) {
            String kind[] = row.getCell(9).toString().split(",");
            if (kind.length > 0) products.setProductSize(kind[0].trim());
            if (kind.length > 1) products.setProductColour(kind[1].trim());
        }

        //国家
        if (row.getCell(11) != null && StringUtils.hasText(row.getCell(11).toString())) products.setCountry(row.getCell(11).toString().trim());

        //产品网址
        if (row.getCell(13) != null && StringUtils.hasText(row.getCell(13).toString())) products.setProductUrl(row.getCell(13).toString().trim());

        //缩略图地址
        if (row.getCell(14) != null && StringUtils.hasText(row.getCell(14).toString())) products.setProductThumbnail(row.getCell(14).toString().trim());

        //高清图地址
        if (row.getCell(15) != null && StringUtils.hasText(row.getCell(15).toString())) products.setProductImageUrl(row.getCell(15).toString().trim());

        //备注
        if (row.getCell(17) != null && StringUtils.hasText(row.getCell(17).toString())) products.setComment(row.getCell(17).toString().trim());

        //产品短描述
        if (row.getCell(18) != null && StringUtils.hasText(row.getCell(18).toString())) products.setShortDescription(row.getCell(18).toString().trim());

        //产品长描述
        if (row.getCell(19) != null && StringUtils.hasText(row.getCell(19).toString())) products.setLongDescription(row.getCell(19).toString().trim());

        //Date First Available
        if (row.getCell(22) != null && StringUtils.hasText(row.getCell(22).toString())) products.setDataFirstAvailable(row.getCell(22).toString().trim());

        //Best Sellers Rank
        if (row.getCell(23) != null && StringUtils.hasText(row.getCell(23).toString())) products.setBestSellersRank(row.getCell(23).toString().trim());
    }

    //根据过滤条件判断是否过滤
    public boolean isFilter(Row row) {
        String type = null;
        String size = null;
        String colour = null;
        if (row.getCell(4) != null && StringUtils.hasText(row.getCell(4).toString())) type = row.getCell(4).toString().trim();
        if (row.getCell(9) != null && StringUtils.hasText(row.getCell(9).toString())) {
            String kind[] = row.getCell(9).toString().split(",");
            if (kind.length > 0) size = kind[0].trim();
            if (kind.length > 1) colour = kind[1].trim();
        }

        List<ProductFilter> productFilters = productFilterService.find(type, colour, size);
        return !CollectionUtils.isEmpty(productFilters);
    }

    //是否侵权
    public boolean isTort(Row row) {
        String brand = null;
        if (row.getCell(1) != null && StringUtils.hasText(row.getCell(1).toString())) brand = row.getCell(1).toString().trim();
        if (brand == null) return false;
        List<TortWord> tortWords = tortWordService.findByTortWordName(brand);
        return !CollectionUtils.isEmpty(tortWords);
    }
}
