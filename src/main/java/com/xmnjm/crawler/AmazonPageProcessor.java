package com.xmnjm.crawler;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmnjm.model.CrawlerTargetUrls;
import com.xmnjm.repository.TargetUrlsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.PriorityScheduler;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.util.*;

/**
 * Created by jilion.chen on 4/19/2017.
 */
public class AmazonPageProcessor implements PageProcessor{
    private final static Logger logger = LoggerFactory.getLogger(AmazonPageProcessor.class);
    private String targetUrl;
    private String helpUrl;
    private String initUrl;
    private String keyword;
    private TargetUrlsRepository targetUrlsRepository;

    private String[] userAgents = new String[]{
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.96 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586",
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.277.400 QQBrowser/9.4.7658.400",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2"
    };

    private Site site = Site
            .me()
            .setDomain("www.amazon.com")
            .setSleepTime(2000)
            .setRetrySleepTime(3000)
            .setRetryTimes(2)
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.96 Safari/537.36")
            .setUserAgents(this.userAgents);

    public AmazonPageProcessor(String targetUrl, String helpUrl, String initUrl, String keyword, TargetUrlsRepository targetUrlsRepository) {
        this.targetUrl = targetUrl;
        this.helpUrl = helpUrl;
        this.initUrl = initUrl;
        this.keyword = keyword;
        this.targetUrlsRepository = targetUrlsRepository;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getHelpUrl() {
        return helpUrl;
    }

    public void setHelpUrl(String helpUrl) {
        this.helpUrl = helpUrl;
    }

    public String getInitUrl() {
        return initUrl;
    }

    public void setInitUrl(String initUrl) {
        this.initUrl = initUrl;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public TargetUrlsRepository getTargetUrlsRepository() {
        return targetUrlsRepository;
    }

    public void setTargetUrlsRepository(TargetUrlsRepository targetUrlsRepository) {
        this.targetUrlsRepository = targetUrlsRepository;
    }

    private static class AsinVariation {
        private String size_name;
        private String asin;
        private String color_name;
        private String number_of_items;

        public AsinVariation() {
        }

        public String getSize_name() {
            return size_name;
        }

        public void setSize_name(String size_name) {
            this.size_name = size_name;
        }

        public String getAsin() {
            return asin;
        }

        public void setAsin(String asin) {
            this.asin = asin;
        }

        public String getColor_name() {
            return color_name;
        }

        public void setColor_name(String color_name) {
            this.color_name = color_name;
        }

        public String getNumber_of_items() {
            return number_of_items;
        }

        public void setNumber_of_items(String number_of_items) {
            this.number_of_items = number_of_items;
        }
    }

    @Override
    public void process(Page page) {
        Selectable pageUrl = page.getUrl();
        Selectable pageHtml = page.getHtml();

        if(pageHtml.xpath("//title/text()").toString().indexOf("Robot")> -1) {
            logger.info("robot check");
            logger.info("re-add failedUrl into targetUrl>>>>>:" + pageUrl.toString());
            List<String> failedUrl = new ArrayList<>();
            failedUrl.add(pageUrl.toString());
            page.addTargetRequests(failedUrl, 1);
        } else {
            try {
                logger.info("start crawl URL>>>>>>>>>>:" + pageUrl);
                //list page
                if (pageUrl.regex(this.helpUrl).match()) {
                    List<String> targetUrl0 = pageHtml.xpath("//a[@id='pagnNextLink']").links().all();
                    List<String> targetUrl1 = pageHtml.xpath("//ul[@class='s-result-list']/li/div/div[@class='a-spacing-base']").links().all();
                    if(targetUrl0.size() > 0) {//for no duplicate, only add next page one by one
                        List<String> nextPage = new ArrayList<>();
                        nextPage.add(targetUrl0.get(0));
                        page.addTargetRequests(nextPage, 1000);
                        this.saveTargeUrls(nextPage);
                    }
                    page.addTargetRequests(targetUrl1, 100);
                    this.saveTargeUrls(targetUrl1);
                    page.setSkip(true);
                } else if (!pageUrl.regex(".*&th=1&psc=1").match()){//product page(transition page due to not include other sellers info)
                    page.putField("originalUrl", pageUrl.toString());
                    if(pageHtml.xpath("//div[@id='detailBullets_feature_div']/ul/li/").get() != null) {
                        page.putField("asin", pageHtml.xpath("//div[@id='detailBullets_feature_div']/ul/li/").regex(".*ASIN:.*").regex("<span>\\w+</span>").regex("\\w+").all().get(1));
                    } else if(pageHtml.xpath("//div[@id='detail-bullets']").get() != null){
                        page.putField("asin", pageHtml.xpath("//div[@id='detail-bullets']/table/tbody/tr/td/div/ul/li").regex(".*ASIN:.*").regex(">.\\w+<").regex("\\w+").get());
                    }
                    page.putField("title", pageHtml.xpath("//div[@id='titleSection']/h1/span[@id='productTitle']/text()"));
                    page.putField("thumbnailUrl", pageHtml.xpath("//span[contains(@class, 'a-button-thumbnail')]/span/span/img/@src"));
                    page.putField("imgUrl", pageHtml.xpath("//div[@id='imgTagWrapperId']/img/@data-old-hires"));
                    page.putField("fromApi", 0);
                    Selectable initScript = pageHtml.xpath("//div[@id='twisterJsInitializer_feature_div']/script");
                    List<String> asinVariationList = initScript.regex(".asinVariationValues.*}}").regex("(\\{\"size_name\":\"\\d+\",(\"number_of_items\":\"\\d+\",)?\"ASIN\":\"\\w+\",\"color_name\":\"\\d+\"\\})").all();
                    ObjectMapper mapper = new ObjectMapper();
                    for (String asinVariationStr:asinVariationList) {
                        try {
                            AsinVariation asinVariation = mapper.readValue(asinVariationStr.replace("ASIN", "asin"), AsinVariation.class);//json string can not include upper case character or will throw exception
                            String hashPattern = "#.*";
                            String regexPattern = "\\/dp/\\w+/";
                            String newUrl = pageUrl.toString().replace(hashPattern, "").replaceAll(regexPattern, "/dp/" + asinVariation.getAsin() + "/") + "&th=1&psc=1";
                            List<String> newRequestUrlList = new ArrayList<>();
                            newRequestUrlList.add(newUrl);
                            page.addTargetRequests(newRequestUrlList, 10);
                            this.saveTargeUrls(newRequestUrlList);
                        } catch (JsonGenerationException e) {
                            e.printStackTrace();
                        } catch (JsonMappingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {//target page to extract product info
                    page.putField("originalUrl", pageUrl.toString());
                    page.putField("title", pageHtml.xpath("//div[@id='titleSection']/h1/span[@id='productTitle']/text()"));
                    page.putField("price", pageHtml.xpath("//span[@id='priceblock_ourprice']/text()"));
                    if(pageHtml.xpath("//div[@id='detailBullets_feature_div']/ul/li/").get() != null) {
                        page.putField("parentAsin", pageHtml.xpath("//div[@id='detailBullets_feature_div']/ul/li/").regex(".*ASIN:.*").regex("<span>\\w+</span>").regex("\\w+").all().get(1));
                    } else if(pageHtml.xpath("//div[@id='detail-bullets']").get() != null){
                        page.putField("parentAsin", pageHtml.xpath("//div[@id='detail-bullets']/table/tbody/tr/td/div/ul/li").regex(".*ASIN:.*").regex(">.\\w+<").regex("\\w+").get());
                    }
                    page.putField("asin", pageUrl.regex("https://www\\.amazon\\.com/.+/dp/(\\w+)/.*"));
                    page.putField("thumbnailUrl", pageHtml.xpath("//span[contains(@class, 'a-button-thumbnail')]/span/span/img/@src"));
                    page.putField("imgUrl", pageHtml.xpath("//div[@id='imgTagWrapperId']/img/@data-old-hires"));
                    page.putField("comments", pageHtml.xpath("//div[@id='averageCustomerReviews']//span[@class='a-declarative']/a[@id='acrCustomerReviewLink']/span[@id='acrCustomerReviewText']/text()").regex("((\\d+,)*[0-9]*)"));
                    page.putField("hasOtherSellers", pageHtml.xpath("//div[@id='moreBuyingChoices_feature_div']/div[@class='a-section a-spacing-medium']").toString() != null);
                    page.putField("otherSellersAmount", pageHtml.xpath("//div[@id='mbc']/div[@class='a-box']/div[@class='a-box-inner a-padding-base']/div[@class='a-row']/span/a/text()").regex("(\\d+)"));
                    page.putField("size", pageHtml.xpath("//select[@id='native_dropdown_selected_size_name']/option[@class='dropdownSelect']/text()"));
                    String dropdownColor = pageHtml.xpath("//select[@id='native_dropdown_selected_color_name']/option[@class='dropdownSelect']/text()").toString();
                    String radioColor = pageHtml.xpath("//div[@id='native_dropdown_selected_color_name']/option[@class='swatchSelect']/text()").toString();
                    String listColor = pageHtml.xpath("//div[@id='variation_color_name']/div/span/text()").toString();
                    page.putField("color", listColor == null ? (dropdownColor == null ? radioColor : dropdownColor) : listColor);
                    page.putField("fromApi", 0);
                }
                this.markedUrlCompleted(pageUrl.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public String[] getUserAgents() {
        return userAgents;
    }

    public Downloader getDownloader() {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("127.0.0.1", 1087)));
        return httpClientDownloader;
    }

    public void saveTargeUrls(List<String> targetUrl) {
        for (String s : targetUrl) {
            CrawlerTargetUrls crawlerTargetUrls = new CrawlerTargetUrls();
            crawlerTargetUrls.setUrl(s);
            crawlerTargetUrls.setDomain(this.site.getDomain());
            crawlerTargetUrls.setCreatedTime(new Date());
            crawlerTargetUrls.setBase64Url(Base64.getEncoder().encodeToString(s.getBytes()));
            crawlerTargetUrls.setCrawled(false);
            targetUrlsRepository.save(crawlerTargetUrls);
        }
    }

    public void markedUrlCompleted(String url) {
        String currentBase64Url = Base64.getEncoder().encodeToString(url.getBytes());
        List<CrawlerTargetUrls> crawlerTargetUrls = targetUrlsRepository.findByBase64Url(currentBase64Url);
        for (CrawlerTargetUrls crawlerTargetUrl: crawlerTargetUrls) {
            crawlerTargetUrl.setCrawled(true);
            targetUrlsRepository.save(crawlerTargetUrl);
        }
    }


    public static void main(String[] args) {
        String hUrl = "https://www\\.amazon\\.com/s/.+page=\\d+.+|https://www\\.amazon\\.com/s/ref=nb_sb_noss_2\\?url=search.*";;
        String tUrl = "https://www\\.amazon\\.com/.+/dp/.+/ref.+";
        String initUrl = "https://www.amazon.com/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords=t+shirt";
        //String initUrl = "https://www.amazon.com/s/ref=sr_pg_4?rh=i%3Aaps%2Ck%3At+shirt&page=4&keywords=t+shirt&ie=UTF8&qid=1495097601&spIA=B06WWM5MS1,B06XTFV2LX,B071NKBBR9,B06X9JL999,B01M1K4ABG,B07166Y6TP";
        //String initUrl2 = "https://www.amazon.com/Hanes-ComfortSoft-T-Shirt-Black-X-Large/dp/B013X4Z4HS/ref=sr_1_2/139-7255659-8221437?ie=UTF8&qid=1494995306&sr=8-2&keywords=t+shirt&th=1&psc=1";
       // Spider.create(new AmazonPageProcessor(tUrl, hUrl, initUrl)).addUrl(new String[] {initUrl, initUrl2}).addPipeline(new AmazonDBPipeline()).thread(1).run();
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("104.198.32.133", 80)));
        Spider.create(new AmazonPageProcessor(tUrl, hUrl, initUrl, "t shirt",null)).setDownloader(httpClientDownloader).setScheduler(new PriorityScheduler()).addUrl("https://www.amazon.com/ONeill-Wetsuits-Protection-Sleeve-X-Large/dp/B004I44IW0/ref=sr_1_304?ie=UTF8&qid=1495619063&sr=8-304&keywords=t+shirt").addPipeline(new AmazonDBPipeline()).thread(1).run();
    }
}
