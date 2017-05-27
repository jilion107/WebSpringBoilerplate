package com.xmnjm.controller;

import com.xmnjm.crawler.AmazonDBPipeline;
import com.xmnjm.crawler.AmazonPageProcessor;
import com.xmnjm.model.CrawlerTargetUrls;
import com.xmnjm.repository.TargetUrlsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.PriorityScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jilion.chen on 5/07/2017.
 */
@CrossOrigin(origins = "http://localhost:8082")
@RestController
public class AmazonCrawlController {
    private final static Logger logger = LoggerFactory.getLogger(AmazonCrawlController.class);

    @Autowired
    private AmazonDBPipeline amazonDBPipeline;
    @Autowired
    private TargetUrlsRepository targetUrlsRepository;

    @RequestMapping(value = "/api/crawl", method = RequestMethod.POST)
    public Map<String, Object> crawl(@RequestParam(value = "keyword", required = false) String keyword) {
        Map<String, Object> result = new HashMap<>();
        try {
            logger.info("jilion: crawler 开始了.........");
            String hUrl = "https://www\\.amazon\\.com/s/.+page=\\d+.+|https://www\\.amazon\\.com/s/ref=nb_sb_noss_2\\?url=search.*";;
            String tUrl = "https://www\\.amazon\\.com/.+/dp/.+/ref.+";
            String initUrl = "https://www.amazon.com/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords=" + keyword.replaceAll(" ", "+");
            List<String> targetUrls = this.initiateUrls();
            if(targetUrls.size() == 0) {
                targetUrls.add(initUrl);
            }
            logger.info("目标URL" + targetUrls.size() + "个!!!!");
            HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
            List<Proxy> proxies = this.getProxies();
            httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(proxies.toArray(new Proxy[proxies.size()])));
            Spider.create(new AmazonPageProcessor(tUrl, hUrl, initUrl, keyword, targetUrlsRepository)).setDownloader(httpClientDownloader).setScheduler(new PriorityScheduler()).addUrl(targetUrls.toArray(new String[0])).addPipeline(amazonDBPipeline).thread(10).run();
           // Spider.create(new AmazonPageProcessor(tUrl, hUrl, initUrl, targetUrlsRepository)).setScheduler(new PriorityScheduler()).addUrl(targetUrls.toArray(new String[0])).addPipeline(amazonDBPipeline).thread(10).run();
            result.put("result", "success");
            return result;
        } catch (Exception ex) {
            result.put("result", "fail");
            result.put("message", "系统异常！");
            return result;
        }
    }

    private List<String> initiateUrls() {
        List<CrawlerTargetUrls> crawlerTargetUrlss = targetUrlsRepository.findByCrawled(false);
        List<String> targetUrls = new ArrayList<String>();
        for (CrawlerTargetUrls crawlerTargetUrl:crawlerTargetUrlss) {
            targetUrls.add(crawlerTargetUrl.getUrl());
        }
        return targetUrls;
    }

    private List<Proxy> getProxies() {
        List<Proxy> proxies = new ArrayList<>();
        List<String> listProxies = new ArrayList<>();
        listProxies.add("104.198.32.133/80");
        listProxies.add("54.185.121.52/8080");
        listProxies.add("54.71.65.172/80");
        listProxies.add("35.161.64.83/80");
        listProxies.add("54.148.76.230/80");
        listProxies.add("52.34.177.119/80");
        for (String proxyStr: listProxies) {
            Proxy proxy = new Proxy(proxyStr.split("/")[0], Integer.parseInt(proxyStr.split("/")[1]));
            proxies.add(proxy);
        }
        return proxies;
    }
}
