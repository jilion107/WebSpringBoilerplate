package com.xmnjm.bean;

/**
 * @author mandy.huang
 *         权限说明：
 *         admin--对集团下任何公司数据做操作
 *         companyAdmin -- 只能对某一公司的数据进行操作
 *         companyManager -- 只能对自己负责的数据进行操作
 *         companyOperator -- 只能对自己负责的数据进行操作（除删除和汇出功能）
 */
public enum RoleEnum {
    admin("集团admin"), companyAdmin("公司admin"), companyManager("公司经理"), companyOperator("公司操作员");

    private String value;

    private RoleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
