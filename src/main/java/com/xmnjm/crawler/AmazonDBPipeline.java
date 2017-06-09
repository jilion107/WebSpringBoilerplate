package com.xmnjm.crawler;

import com.xmnjm.model.AmazonProduct;
import com.xmnjm.repository.AmazonProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;

/**
 * Created by Jilion on 2017/5/07.
 */
@Repository
public class AmazonDBPipeline implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AmazonProductRepository amazonProductRepository;

    @Override
    public void process(ResultItems resultItems, Task task) {
        try {
            AmazonProduct amazonProduct = new AmazonProduct();
            boolean isTwister = resultItems.get("parentAsin") != null;
            amazonProduct.setAsin(resultItems.get("asin").toString());
            amazonProduct.setUrl(resultItems.get("originalUrl").toString());
            amazonProduct.setTitle(resultItems.get("title").toString());
            amazonProduct.setThumbnailUrl(resultItems.get("thumbnailUrl").toString());
            amazonProduct.setImageUrl(resultItems.get("imgUrl").toString());
            if(new Integer(resultItems.get("fromApi").toString()) > 0) {
                amazonProduct.setFromApi(true);
            } else {
                amazonProduct.setFromApi(false);
            }
            if(isTwister) {
                amazonProduct.setParentAsin(resultItems.get("parentAsin").toString());
                amazonProduct.setSizeType(resultItems.get("size").toString());
                amazonProduct.setColor(resultItems.get("color").toString());
                amazonProduct.setPrice(resultItems.get("price").toString());
                amazonProduct.setRank(new Integer(resultItems.get("rank").toString().replace(",", "")));
                amazonProduct.setLowestprice(resultItems.get("lowestPrice"));
                amazonProduct.setLowestprice(resultItems.get("lowestPrice"));
                if(resultItems.get("comments") != null) {
                    amazonProduct.setComments(new Integer(resultItems.get("comments").toString().replace(",", "")));
                }
                if (resultItems.get("hasOtherSellers").toString() == "true") {
                    amazonProduct.setHasOtherSellers(true);
                    amazonProduct.setOtherSellersAmount( new Integer(resultItems.get("otherSellersAmount").toString().replace(",", "")));
                } else {
                    amazonProduct.setHasOtherSellers(false);
                }
            }
            amazonProduct.setCreatedTime(new Date());

            amazonProductRepository.save(amazonProduct);
            logger.info("save page info successfully....");
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("generate entity error", e);
        }
    }
}
