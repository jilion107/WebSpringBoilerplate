package com.xmnjm.controller;

import com.xmnjm.crawler.AmazonAPIPageProcessor;
import com.xmnjm.crawler.AmazonDBPipeline;
import com.xmnjm.crawler.SignedRequestsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jilion.chen on 5/13/2017.
 */
@CrossOrigin(origins = "http://localhost:8082")
@RestController
public class AmazonAPIController {
    private final static Logger logger = LoggerFactory.getLogger(AmazonAPIController.class);

    /*
     * Your AWS Access Key ID, as taken from the AWS Your Account page.
     */
    private static final String AWS_ACCESS_KEY_ID = "AKIAJGLUBXVOQ3K2KGNQ";

    /*
     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
     * Your Account page.
     */
    private static final String AWS_SECRET_KEY = "XopVQwTkDVLjnZWIO41XU7882d11PhVOYhdIlI21";

    private static final String AWS_ASSOCIATE_TAG = "ProductAdvertisingAPI-Jilion";

    /*
     * Use one of the following end-points, according to the region you are
     * interested in:
     *
     *      US: ecs.amazonaws.com
     *      CA: ecs.amazonaws.ca
     *      UK: ecs.amazonaws.co.uk
     *      DE: ecs.amazonaws.de
     *      FR: ecs.amazonaws.fr
     *      JP: ecs.amazonaws.jp
     *
     */
    private static final String ENDPOINT = "ecs.amazonaws.com";

    private static final int pageThreshold = 1000;

    @Autowired
    private AmazonDBPipeline amazonDBPipeline;

    @RequestMapping(value = "/api/apiCrawl", method = RequestMethod.POST)
    public Map<String, Object> crawl(@RequestParam(value = "keyword", required = false) String keyword) {
        Map<String, Object> result = new HashMap<>();
        try {
            logger.info("jilion: api crawler 开始了.........");
            Spider.create(new AmazonAPIPageProcessor()).addUrl(this.initiateUrls(keyword).toArray(new String[0])).addPipeline(amazonDBPipeline).thread(3).run();
            result.put("result", "success");
            return result;
        } catch (Exception ex) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
    }

    private List<String> initiateUrls(String keyword) {
        List<String> targetUrls = new ArrayList<String>();

        /*
         * Set up the signed requests helper
         */
        SignedRequestsHelper helper;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
            for(int p=1; p<pageThreshold * 50 / 10; p++) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Service", "AWSECommerceService");
                params.put("Operation", "ItemSearch");
                params.put("AWSAccessKeyId", AWS_ACCESS_KEY_ID);
                params.put("AssociateTag", AWS_ASSOCIATE_TAG);
                params.put("SearchIndex", "All");
                params.put("Keywords", keyword);
                params.put("ResponseGroup", "Large");
                params.put("ItemPage", (new Integer(p)).toString());

                targetUrls.add(helper.sign(params));
            }
            return targetUrls;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }
}
