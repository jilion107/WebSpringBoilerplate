package com.xmnjm.crawler;

import org.apache.http.ParseException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jilion.chen on 5/13/2017.
 */
public class AmazonAPIPageProcessor implements PageProcessor {
    private final static Logger logger = LoggerFactory.getLogger(AmazonAPIPageProcessor.class);

    private Site site = Site
            .me()
            .setDomain("www.amazon.com")
            .setSleepTime(2000)
            .setRetrySleepTime(10000)
            .setRetryTimes(2)
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.96 Safari/537.36");

    public AmazonAPIPageProcessor() {
    }

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        String plainResponse = page.getRawText();
        try {
            logger.info("start crawl URL>>>>>>>>>>:" + page.getUrl());
            Document document = DocumentHelper.parseText(plainResponse);
            Map<String,String> uris = new HashMap<String, String>();
            String nsURI = document.getRootElement().getNamespaceURI();
            uris.put("aws"  , nsURI);
            XPath xpath=document.createXPath("//aws:ItemSearchResponse/aws:Items/aws:Item");
            xpath.setNamespaceURIs(uris);
            List itemNodes = xpath.selectNodes(document);
            for (int i = 0; i < itemNodes.size(); i++) {
                Element item = (Element) itemNodes.get(i);
                String asin = item.elementText("ASIN");
                String parentAsin = item.elementText("ParentASIN");
                String detailPageURL = item.elementText("DetailPageURL");
                Element smallImage = item.element("SmallImage");
                String thumbnailUrl = smallImage.elementText("URL");
                Element largeImage = item.element("LargeImage");
                String imgUrl = largeImage.elementText("URL");
                Element itemAttributes = item.element("ItemAttributes");
                String brand = itemAttributes.elementText("Brand");
                String title = itemAttributes.elementText("Title");
                String size = itemAttributes.elementText("Size");
                String color = itemAttributes.elementText("Color");
                Element listPrice = itemAttributes.element("ListPrice");
                String price = listPrice == null ? "" : listPrice.elementText("FormattedPrice");
                Element offerSummary = item.element("OfferSummary");
                String otherSellersAmount = offerSummary.elementText("TotalNew");
/*                logger.info("ASIN::" + asin);
                logger.info("ParentASIN::" + parentAsin);
                logger.info("originalUrl::" + detailPageURL);
                logger.info("thumbnailUrl::" + thumbnailUrl);
                logger.info("imgUrl::" + imgUrl);
                logger.info("brand::" + brand);
                logger.info("title::" + title);
                logger.info("size::" + size);
                logger.info("color::" + color);
                logger.info("price::" + price);
                logger.info("otherSellersAmount::" + otherSellersAmount);*/
                page.putField("originalUrl", detailPageURL);
                page.putField("title", title);
                page.putField("price", price);
                page.putField("parentAsin", parentAsin);
                page.putField("asin", asin);
                page.putField("thumbnailUrl", thumbnailUrl);
                page.putField("imgUrl", imgUrl);
                page.putField("hasOtherSellers", (new Integer(otherSellersAmount)) > 0);
                page.putField("otherSellersAmount", otherSellersAmount);
                page.putField("size", size);
                page.putField("color", color);
                page.putField("fromApi", 1);
            }
        } catch (final ParseException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
