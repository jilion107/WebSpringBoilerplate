package com.xmnjm.service;

import com.xmnjm.bean.Products;
import com.xmnjm.model.FormalProducts;
import com.xmnjm.model.ProductFilter;
import com.xmnjm.model.TmpProducts;
import com.xmnjm.model.TortProducts;
import com.xmnjm.model.TortWord;
import com.xmnjm.model.User;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author mandy.huang
 */
@Service
public class ImportDataService {
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
    @Inject
    UserService userService;

    public static void main(String[] arg) {
     /*   String fileName = "D:\\fannieERP\\妹娇卫衣采集器采集过(1).xlsx";
        File file = new File(fileName);
        List<List<String>> rows = readExcelFile(file);
        if (rows == null) rows = readCsvFile(file);*/
    }

    public void readXml(CommonsMultipartFile file, Integer scenarioWhat, Integer userId) throws Exception {
        List<List<String>> rows = readExcelFile(file);
        System.out.println("----readExcelFile----" + rows == null ? null : rows.size());
        if (rows == null) rows = readCsvFile(file);
        System.out.println("----readCsvFile----" + rows == null ? null : rows.size());
        if (rows == null) throw new Exception("上传文件解析出错，只支持xls,xlsx,csv格式");
        addToDB(rows, scenarioWhat, userId);
    }

    public List<List<String>> readExcelFile(CommonsMultipartFile file) {
        boolean isE2007 = false;    //判断是否是excel2007格式
        if (file.getOriginalFilename().endsWith("xlsx"))
            isE2007 = true;
        InputStream input = null;
        Workbook wb = null;
        List<List<String>> result = new ArrayList<>();
        try {
            input = file.getInputStream();  //建立输入流
            //input = new FileInputStream(file);
            //根据文件格式(2003或者2007)来初始化
            if (isE2007)
                wb = new XSSFWorkbook(input);
            else
                wb = new HSSFWorkbook(input);
            Sheet sheet = wb.getSheetAt(0);     //获得第一个表单
            Iterator<Row> rows = sheet.rowIterator(); //获得第一个表单的迭代器
            rows.next();//第一行不读取
            while (rows.hasNext()) {
                Row row = rows.next();  //获得行数据
                List<String> cells = new ArrayList<>();
                for (int i = 0; i <= 24; i++) {
                    String v = row.getCell(i) != null ? row.getCell(i).toString() : null;
                    cells.add(v);
                }
                result.add(cells);
            }
        } catch (IOException ex) {
            System.out.println("上传文件解析出错");
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (wb != null) wb.close();
                if (input != null) input.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    //读取csv文件
    public List<List<String>> readCsvFile(CommonsMultipartFile file) {
        List<List<String>> list = new ArrayList<>();

        InputStream input = null;
        InputStreamReader reader = null;
        BufferedReader bReader = null;
        try {
            input = file.getInputStream();
            reader = new InputStreamReader(input, "GBK");
            bReader = new BufferedReader(reader);
            String str = bReader.readLine();
            while ((str = bReader.readLine()) != null) {
                String[] arr = str.split("\t");
                list.add(Arrays.asList(arr));

            }
        } catch (Exception e) {
            System.out.println("上传文件解析出错");
            e.printStackTrace();
            return null;
        } finally {
            if (null != bReader) {
                try {
                    bReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    public void addToDB(List<List<String>> rows, Integer scenarioWhat, Integer userId) {
        User user = userService.getUser(userId);
        Long parent = 0l;
        long rowNumber = 0;
        for (List<String> row : rows) {
            try {
                //当品牌和产品名称为空，表示无变体，不保存
                if (StringUtils.isEmpty(row.get(1)) && StringUtils.isEmpty(row.get(2))) {
                    break;
                }

                Products products = new Products();
                //如果此ASIN数据已经存在数据库里，直接更新
                String asin = row.get(3);
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

                products.setUserId(userId);
                if (user.getCompanyId() != null) {
                    products.setCompanyId(user.getCompanyId());
                }

                //过滤品牌
                boolean isTort = this.isTort(row);
                boolean isParent = "Parent".equals(row.get(9));
                if (!isParent) products.setParent(parent);
                if (isTort) {
                    tortProducts = new TortProducts();
                    BeanUtils.copyProperties(products, tortProducts);
                    tortProductsService.save(tortProducts);
                    if (isParent) parent = tortProducts.getId();
                } else {
                    tmpProducts = new TmpProducts();
                    BeanUtils.copyProperties(products, tmpProducts);
                    tmpProductsService.save(tmpProducts);
                    if (isParent) parent = tmpProducts.getId();
                }

            } catch (Exception ex) {
                System.out.println("解析第" + rowNumber + "出错");
                System.out.println(ex.getMessage());
            }
            rowNumber++;
        }


    }

    public void setProduct(Products products, List<String> row, Integer scenarioWhat) {
        products.setScenarioWhat(scenarioWhat);
        //品牌
        if (StringUtils.hasText(row.get(1))) products.setBrand(row.get(1).trim());
        //产品名称
        if (StringUtils.hasText(row.get(2))) products.setProductName(row.get(2).trim());
        //ASIN
        if (StringUtils.hasText(row.get(3))) products.setAsin(row.get(3).trim());
        //分类
        if (StringUtils.hasText(row.get(4))) products.setProductTypeName(row.get(4).trim());
        //价格
        if (StringUtils.hasText(row.get(5))) {
            try {
                products.setPrice(Double.valueOf(row.get(5).trim()));
            } catch (Exception e) {
                System.out.println("价格解析出错[" + row.get(5) + "]");
            }
        }
        //BuyBox价格
        if (StringUtils.hasText(row.get(6))) products.setBuyBoxPrice(row.get(6).trim());
        //Offer
        if (StringUtils.hasText(row.get(7))) {
            try {
                products.setOffer(Double.valueOf(row.get(7).trim()));
            } catch (Exception e) {
                System.out.println("Offer解析出错[" + row.get(7) + "]");
            }
        }

        //评论数
        if (StringUtils.hasText(row.get(8))) {
            try {
                products.setCommentNumber(Integer.valueOf(row.get(8).trim()));
            } catch (Exception e) {
                System.out.println("评论数解析出错[" + row.get(8) + "]");
            }
        } else {
            products.setCommentNumber(0);
        }

        //型号
        if (StringUtils.hasText(row.get(9)) && !"-".equals(row.get(9).trim())) {
            String kind[] = row.get(9).toString().split(",");
            if (kind.length > 0) products.setProductSize(kind[0].trim());
            if (kind.length > 1) products.setProductColour(kind[1].trim());
        }

        //国家
        if (StringUtils.hasText(row.get(11))) products.setCountry(row.get(11).trim());

        //产品网址
        if (StringUtils.hasText(row.get(13))) products.setProductUrl(row.get(13).trim());

        //缩略图地址
        if (StringUtils.hasText(row.get(14))) products.setProductThumbnail(row.get(14).trim());

        //高清图地址
        if (StringUtils.hasText(row.get(15))) products.setProductImageUrl(row.get(15).trim());

        //备注
        if (StringUtils.hasText(row.get(17))) products.setComment(row.get(17).trim());

        //产品短描述
        if (StringUtils.hasText(row.get(18))) products.setShortDescription(row.get(18).trim());

        //产品长描述
        if (StringUtils.hasText(row.get(19))) products.setLongDescription(row.get(19).trim());

        //Date First Available
        if (StringUtils.hasText(row.get(22))) products.setDataFirstAvailable(row.get(22).trim());

        //Best Sellers Rank
        if (StringUtils.hasText(row.get(23))) products.setDataFirstAvailable(row.get(23).trim());

    }

    //根据过滤条件判断是否过滤
    public boolean isFilter(List<String> row) {
        String type = null;
        String size = null;
        String colour = null;
        if (StringUtils.hasText(row.get(4))) type = row.get(4).trim();
        if (StringUtils.hasText(row.get(9))) {
            String kind[] = row.get(9).split(",");
            if (kind.length > 0) size = kind[0].trim();
            if (kind.length > 1) colour = kind[1].trim();
        }

        List<ProductFilter> productFilters = productFilterService.find(type, colour, size);
        return !CollectionUtils.isEmpty(productFilters);
    }

    //是否侵权
    public boolean isTort(List<String> row) {
        String brand = null;
        if (StringUtils.hasText(row.get(1))) brand = row.get(1).trim();
        if (brand == null) return false;
        List<TortWord> tortWords = tortWordService.findByTortWordName(brand);
        return !CollectionUtils.isEmpty(tortWords);
    }

}
