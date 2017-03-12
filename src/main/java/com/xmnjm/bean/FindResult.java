package com.xmnjm.bean;

import java.util.List;

public class FindResult<T> {
    private List<T> list;
    private int offset;
    private long total;

    public FindResult(List<T> list, int offset, long total) {
        this.list = list;
        this.offset = offset;
        this.total = total;
    }

    public FindResult() {

    }

    public List<T> getList() {
        return list;
    }

    public int getOffset() {
        return offset;
    }

    public long getTotal() {
        return total;
    }
}
