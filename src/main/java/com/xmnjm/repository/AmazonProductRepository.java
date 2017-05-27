package com.xmnjm.repository;

import com.xmnjm.model.AmazonProduct;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Jilion on 2017/5/13.
 */
public interface AmazonProductRepository extends CrudRepository<AmazonProduct, Integer> {
}
