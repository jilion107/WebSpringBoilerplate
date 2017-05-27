package com.xmnjm.crawler;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by jilion.chen on 5/06/2017.
 */
public class AmazonAPIProcessor {
    private final static Logger logger = LoggerFactory.getLogger(AmazonAPIProcessor.class);

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


    /*
     * Utility function to fetch the response from the service and extract the
     * title from the XML.
     */
    private static String fetchTitle(String requestUrl) {
        String title = null;
        try {
/*            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            Node titleNode = doc.getElementsByTagName("Title").item(0);
            title = titleNode.getTextContent();*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return title;
    }

    /*
     * The Item ID to lookup. The value below was selected for the US locale.
     * You can choose a different value if this value does not work in the
     * locale of your choice.
     */
    private static final String ITEM_ID = "0545010225";

    public static void main(String[] args) {
        /*
         * Set up the signed requests helper
         */
        SignedRequestsHelper helper;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String requestUrl = null;

        for(int p=1; p<1000; p++) {
            logger.info("Start the " + p + " page......");            ;
            Map<String, String> params = new HashMap<String, String>();
            params.put("Service", "AWSECommerceService");
            params.put("Operation", "ItemSearch");
            params.put("AWSAccessKeyId", AWS_ACCESS_KEY_ID);
            params.put("AssociateTag", AWS_ASSOCIATE_TAG);
            params.put("SearchIndex", "All");
            params.put("Keywords", "t shirt");
            params.put("ResponseGroup", "Images,ItemAttributes,Large");
            params.put("ItemPage", (new Integer(p)).toString());

            requestUrl = helper.sign(params);
            try {
                Thread.sleep(1000);
                int statusCode = 0;
                String plainResponse = null;
                HttpUriRequest httpUriRequest = RequestBuilder.get().setUri(requestUrl).build();
                CloseableHttpClient httpclient = HttpClients.createDefault();
                CloseableHttpResponse httpResponse = httpclient.execute(httpUriRequest);
                statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode >= 200 && statusCode < 300) {
                    HttpEntity entity = httpResponse.getEntity();
                    try {
                        plainResponse = entity != null ? EntityUtils.toString(entity) : null;
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
                            logger.info("ASIN::" + asin);
                            logger.info("ParentASIN::" + parentAsin);
                            logger.info("thumbnailUrl::" + thumbnailUrl);
                            logger.info("imgUrl::" + imgUrl);
                            logger.info("brand::" + brand);
                            logger.info("title::" + title);
                            logger.info("size::" + size);
                            logger.info("color::" + color);
                            logger.info("price::" + price);
                            logger.info("otherSellersAmount::" + otherSellersAmount);
                        }
                        logger.info("End of page " + p + "***********");
                    } catch (final ParseException ex) {
                        throw new ClientProtocolException(ex);
                    }
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + statusCode);
                }
            } catch (IOException e) {
                logger.warn("call AWS API error", requestUrl, e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
