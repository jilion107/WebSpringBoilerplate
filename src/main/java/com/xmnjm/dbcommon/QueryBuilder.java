package com.xmnjm.dbcommon;


import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class QueryBuilder {
    private static final String OPERATOR = "AND";
    private final String typeQuery;
    private final List<FieldQuery> fieldQueries = Lists.newArrayList();
    private final List<String> orderByFields = Lists.newArrayList();
    private final List<Boolean> isDescs = Lists.newArrayList();
    private final List<String> groupByFields = Lists.newArrayList();
    private boolean desc = false;
    private boolean skipNullFields = false;
    private boolean skipEmptyFields = false;
    private Integer from;
    private Integer size;

    private QueryBuilder(String typeQuery) {
        this.typeQuery = typeQuery;
    }

    public static QueryBuilder query(String query) {
        Preconditions.checkState(!query.toLowerCase().contains(" where "), "where doesn't allow to be here");
        return new QueryBuilder(query);
    }

    public QueryBuilder skipNullFields() {
        skipNullFields = true;
        return this;
    }

    public QueryBuilder skipEmptyFields() {
        skipNullFields = true;
        skipEmptyFields = true;
        return this;
    }

    public QueryBuilder orderBy(String... fields) {
        orderByFields.addAll(Arrays.asList(fields));
        return this;
    }

    public QueryBuilder groupBy(String... fields) {
        groupByFields.addAll(Arrays.asList(fields));
        return this;
    }


    public QueryBuilder orderBy(String field, boolean isDesc) {
        orderByFields.add(field);
        isDescs.add(isDesc);
        return this;
    }

    public QueryBuilder desc() {
        desc = true;
        return this;
    }

    public QueryBuilder append(String field, Object value) {
        return append(field, value, "=");
    }

    public QueryBuilder append(String field, Object value, String operator) {
        return append(field, value, field, operator);
    }

    public QueryBuilder append(String field, Object value, String variableName, String operator) {
        fieldQueries.add(new FieldQuery(field, value, variableName, operator));
        return this;
    }

    public QueryBuilder from(int from) {
        this.from = from;
        return this;
    }

    public QueryBuilder fetch(int size) {
        this.size = size;
        return this;
    }

    public Query build() {
        StringBuilder b = new StringBuilder(typeQuery).append(' ');

        boolean firstFieldQuery = true;

        Map<String, Object> params = Maps.newHashMap();

        for (FieldQuery fieldQuery : fieldQueries) {

            if (!isFieldSkipped(fieldQuery)) {
                if (firstFieldQuery) {
                    b.append("WHERE ");
                    firstFieldQuery = false;
                } else {
                    b.append(' ').append(OPERATOR).append(' ');
                }

                b.append(fieldQuery.getField()).append(' ');
                b.append(fieldQuery.getOperator()).append(" :").append(fieldQuery.getVariableName());

                params.put(fieldQuery.getVariableName(), fieldQuery.getValue());
            }
        }

        if (!groupByFields.isEmpty()) {
            b.append(" GROUP BY ");
            for (String groupByField : groupByFields) {
                b.append(groupByField);
                b.append(',');
            }
            b.deleteCharAt(b.length() - 1);
        }


        if (!orderByFields.isEmpty()) {
            b.append(" ORDER BY ");
            for (int i = 0; i < orderByFields.size(); i++) {
                b.append(orderByFields.get(i));
                if (!isDescs.isEmpty() && isDescs.size() >= orderByFields.size() && isDescs.get(i)) {
                    b.append(" DESC");
                }
                b.append(',');
            }

            b.deleteCharAt(b.length() - 1);

            if (desc) {
                b.append(" DESC");
            }
        }

        Query query = Query.create(b.toString());
        query.params.putAll(params);
        query.from = from;
        query.size = size;

        return query;
    }

    boolean isFieldSkipped(FieldQuery fieldQuery) {
        return skipNullFields && fieldQuery.getValue() == null || skipEmptyFields && fieldQuery.getValue() instanceof String && Strings.isNullOrEmpty((String) fieldQuery.getValue());

    }

    private class FieldQuery {
        private final String field;
        private final Object value;
        private final String operator;
        private final String variableName;

        public FieldQuery(String field, Object value, String variableName, String operator) {
            this.field = field;
            this.value = value;
            this.operator = operator;
            this.variableName = variableName;
        }

        public String getField() {
            return field;
        }

        public Object getValue() {
            return value;
        }

        public String getOperator() {
            return operator;
        }

        public String getVariableName() {
            return variableName;
        }
    }
}
