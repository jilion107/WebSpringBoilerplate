package com.xmnjm.service;


import com.xmnjm.dao.TortWordDao;
import com.xmnjm.model.TortWord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;


/**
 * @author mandy.huang
 */
@Service
public class TortWordService {
    @Inject
    TortWordDao tortWordDao;

    @Transactional
    public void save(TortWord tortWord) {
        tortWord.setCreateTime(new Date());
        tortWord.setUpdateTime(new Date());
        tortWord.setStatus(1);
        tortWordDao.save(tortWord);
    }

    public List<TortWord> list(TortWord tortWord, int offset, int fetchSize, String orderField) {
        return tortWordDao.list(tortWord, offset, fetchSize, orderField, true);
    }

    public int count(TortWord tortWord) {
        return tortWordDao.count(tortWord);
    }

    @Transactional
    public void delete(Long id) {
        TortWord tortWord = findById(id);
        tortWordDao.delete(tortWord);
    }

    public TortWord findById(Long id) {
        return tortWordDao.findById(id);
    }

    @Transactional
    public void update(TortWord tortWord) {
        tortWord.setUpdateTime(new Date());
        tortWordDao.update(tortWord);
    }

    public List<TortWord> findByTortWordName(String tortWordName) {
        return tortWordDao.findByTortWordName(tortWordName);
    }

    @Transactional
    public void delete(TortWord tortWord) {
        tortWord.setStatus(0);
        update(tortWord);
    }
}