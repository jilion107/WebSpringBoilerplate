package com.xmnjm.dbcommon;

import com.google.common.collect.Maps;

import javax.persistence.EntityManager;
import java.util.Map;


public final class Query {
    final String queryString;
    final Map<String, Object> params = Maps.newHashMap();
    Integer from;
    Integer size;

    private Query(String queryString) {
        this.queryString = queryString;
    }

    public static Query create(String queryString) {
        return new Query(queryString);
    }

    public Query param(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public Query from(int from) {
        this.from = from;
        return this;
    }

    public Query fetch(int size) {
        this.size = size;
        return this;
    }

    javax.persistence.Query query(EntityManager entityManager) {
        javax.persistence.Query query = entityManager.createQuery(queryString);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        if (from != null)
            query.setFirstResult(from);
        if (size != null)
            query.setMaxResults(size);
        return query;
    }

    @Override
    public String toString() {
        return String.format("{queryString='%s', params=%s, from=%d, size=%d}", queryString, params, from, size);
    }
}
