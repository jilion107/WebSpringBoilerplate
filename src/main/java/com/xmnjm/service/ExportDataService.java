package com.xmnjm.service;

import com.xmnjm.bean.ExportDataRequest;
import com.xmnjm.model.FormalProducts;
import com.xmnjm.util.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author mandy.huang
 */
@Service
public class ExportDataService {
    @Inject
    FormalProductsService formalProductsService;
    @Inject
    GenDataService genDataService;

    @Transactional
    public StringBuilder genExportData(ExportDataRequest exportDataRequest) {
        Date endUpdateTime = DateUtils.getDate(new Date(), exportDataRequest.getBeforeDays() * (-1));
        List<FormalProducts> formalProductses = formalProductsService.export(exportDataRequest.getProductIds(), endUpdateTime, exportDataRequest.getTotal());
        StringBuilder builder = new StringBuilder();
        builder.append("sku\tproduct-id\tproduct-id-type\tprice\tminimum-seller-allowed-price\tmaximum-seller-allowed-price\titem-condition\tquantity\tadd-delete\twill-ship-internationally\texpedited-shipping\tstandard-plus\titem-note\tfulfillment-center-id\tproduct-tax-code\tleadtime-to-ship").append("\n");
        if (!CollectionUtils.isEmpty(formalProductses)) {
            for (FormalProducts formalProducts : formalProductses) {
                builder.append(randomSkuId()).append('\t');
                builder.append(formalProducts.getAsin()).append('\t');
                builder.append(formalProducts.getProductTypeId()).append('\t');
                builder.append(exportDataRequest.getPrices()).append('\t');
                builder.append("\t\t");
                builder.append(11).append('\t');//item-condition
                builder.append(randomQuantity(exportDataRequest.getMinQuantity(), exportDataRequest.getMaxQuantity())).append('\t');
                builder.append("a\t");//add-delete
                builder.append("1\t");//will-ship-internationally
                builder.append("\t\t\t\t\t");
                builder.append(exportDataRequest.getDeliveryDays());//leadtime-to-ship
                builder.append("\n");
                formalProductsService.update(formalProducts);
            }
        }
        return builder;
    }

    /**
     * 随机生成20位的sku
     *
     * @return
     */
    public String randomSkuId() {
        String base = "abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int length = 20;
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 库存随机
     *
     * @param minQuantity
     * @param maxQuantity
     * @return
     */
    public int randomQuantity(int minQuantity, int maxQuantity) {
        Random random = new Random();
        return random.nextInt(maxQuantity - minQuantity) + minQuantity;
    }
}
