package com.xmnjm.service;


import com.xmnjm.dao.TortDao;
import com.xmnjm.model.Tort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;


/**
 * @author mandy.huang
 */
@Service
public class TortService {
    @Inject
    TortDao tortDao;

    @Transactional
    public void save(Tort tort) {
        tort.setCreateTime(new Date());
        tort.setUpdateTime(new Date());
        tort.setStatus(1);
        tortDao.save(tort);
    }

    public List<Tort> list(Tort tort, int offset, int fetchSize, String orderField) {
        return tortDao.list(tort, offset, fetchSize, orderField, true);
    }

    public int count(Tort tort) {
        return tortDao.count(tort);
    }

    @Transactional
    public void delete(Long id) {
        Tort tort = findById(id);
        tort.setStatus(0);
        update(tort);
    }

    public Tort findById(Long id) {
        return tortDao.findById(id);
    }

    @Transactional
    public void update(Tort tort) {
        tort.setUpdateTime(new Date());
        tortDao.update(tort);
    }

    public List<Tort> findByName(String name) {
        return tortDao.findByName(name);
    }

    @Transactional
    public void delete(Tort tort) {
        tort.setStatus(0);
        update(tort);
    }
}