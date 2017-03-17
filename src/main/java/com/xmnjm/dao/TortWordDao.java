package com.xmnjm.dao;

import com.google.common.base.Strings;
import com.xmnjm.dbcommon.JPAAccess;
import com.xmnjm.dbcommon.Query;
import com.xmnjm.dbcommon.QueryBuilder;
import com.xmnjm.model.TortWord;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.List;

/**
 * @author mandy.huang
 */
@Component
public class TortWordDao {
    @Inject
    JPAAccess jpaAccess;

    @Transactional
    public void save(TortWord tortWord) {
        tortWord.setStatus(1);
        jpaAccess.save(tortWord);
    }

    @Transactional
    public void update(TortWord tortWord) {
        jpaAccess.update(tortWord);
    }

    public TortWord findById(Long id) {
        return jpaAccess.findOne(Query.create("from TortWord where status=1 and id=:id").param("id", id));
    }

    public List<TortWord> findByTortWordName(String tortWordName) {
        return jpaAccess.find(Query.create("from TortWord where status=1 and tortWordName=:tortWordName").param("tortWordName", tortWordName));
    }

    public List<TortWord> list(TortWord tortWord, int offset, int fetchSize, String orderField, Boolean isDesc) {
        String vOrderField = Strings.isNullOrEmpty(orderField) ? "id" : orderField;
        Boolean vIsDesc = null == isDesc ? Boolean.FALSE : isDesc;
        QueryBuilder queryBuilder = QueryBuilder.query("from TortWord").skipEmptyFields()
            .append("status", tortWord.getStatus())
            .orderBy(vOrderField, vIsDesc);
        if (StringUtils.hasText(tortWord.getTortWordName())) {
            queryBuilder.append("name", '%' + tortWord.getTortWordName() + '%', "like");
        }

        return jpaAccess.find(queryBuilder.build().from(offset).fetch(fetchSize));
    }

    public int count(TortWord tortWord) {
        QueryBuilder queryBuilder = QueryBuilder.query("select count(*) from TortWord").skipEmptyFields()
            .append("status", tortWord.getStatus());
        if (StringUtils.hasText(tortWord.getTortWordName())) {
            queryBuilder.append("name", '%' + tortWord.getTortWordName() + '%', "like");
        }
        return Integer.parseInt(jpaAccess.find(queryBuilder.build()).get(0).toString());
    }

    public void delete(TortWord tortWord) {
        jpaAccess.delete(tortWord);
    }
}