package com.xmnjm.bean;

import java.util.List;

/**
 * @author mandy.huang
 */
public class FormalProductsSaveRequest {
    private List<Long> tmpProductIds;

    public List<Long> getTmpProductIds() {
        return tmpProductIds;
    }

    public void setTmpProductIds(List<Long> tmpProductIds) {
        this.tmpProductIds = tmpProductIds;
    }
}
