package com.xmnjm.dao;

import com.google.common.base.Strings;
import com.xmnjm.dbcommon.JPAAccess;
import com.xmnjm.dbcommon.Query;
import com.xmnjm.dbcommon.QueryBuilder;
import com.xmnjm.model.Tort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.List;

/**
 * @author mandy.huang
 */
@Component
public class TortDao {
    @Inject
    JPAAccess jpaAccess;

    @Transactional
    public void save(Tort tort) {
        tort.setStatus(1);
        jpaAccess.save(tort);
    }

    @Transactional
    public void update(Tort tort) {
        jpaAccess.update(tort);
    }

    public Tort findById(Long id) {
        return jpaAccess.findOne(Query.create("from Tort where status=1 and id=:id").param("id", id));
    }

    public List<Tort> list(Tort tort, int offset, int fetchSize, String orderField, Boolean isDesc) {
        String vOrderField = Strings.isNullOrEmpty(orderField) ? "id" : orderField;
        Boolean vIsDesc = null == isDesc ? Boolean.FALSE : isDesc;
        QueryBuilder queryBuilder = QueryBuilder.query("from Tort").skipEmptyFields()
            .append("status", tort.getStatus())
            .orderBy(vOrderField, vIsDesc);
        if (StringUtils.hasText(tort.getName())) {
            queryBuilder.append("name", '%' + tort.getName() + '%', "like");
        }

        return jpaAccess.find(queryBuilder.build().from(offset).fetch(fetchSize));
    }

    public int count(Tort tort) {
        QueryBuilder queryBuilder = QueryBuilder.query("select count(*) from Tort").skipEmptyFields()
            .append("status", tort.getStatus());
        if (StringUtils.hasText(tort.getName())) {
            queryBuilder.append("name", '%' + tort.getName() + '%', "like");
        }
        return Integer.parseInt(jpaAccess.find(queryBuilder.build()).get(0).toString());
    }
}