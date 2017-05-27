package com.xmnjm.repository;

import com.xmnjm.model.CrawlerTargetUrls;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by jilion.chen on 5/17/2017.
 */
public interface TargetUrlsRepository extends CrudRepository<CrawlerTargetUrls, Integer> {

    List<CrawlerTargetUrls> findByBase64Url(String base64Url);

    List<CrawlerTargetUrls> findByCrawled(boolean crawled);
}
