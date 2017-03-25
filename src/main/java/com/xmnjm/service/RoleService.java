package com.xmnjm.service;

import com.xmnjm.bean.ProductRequest;
import com.xmnjm.dao.UserDao;
import com.xmnjm.model.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.inject.Inject;

/**
 * @author mandy.huang
 */
@Service
public class RoleService {
    @Inject
    UserDao userDao;

    public void updateProductRequestByRole(ProductRequest productRequest) {
        //没有传user，查不出数据
        if (productRequest.getUserId() == null) {
            productRequest.setUserId(-1);
            return;
        }
        User user = userDao.findById(productRequest.getUserId());
        //user不存在，查不出数据
        if (user == null) {
            productRequest.setUserId(-1);
            return;
        }
        //没配权限，查不出数据
        if (StringUtils.isEmpty(user.getRole())) {
            productRequest.setUserId(-1);
            return;
        }
        //集团admin
        if ("1".equals(user.getRole())) {
            productRequest.setUserId(null);
            productRequest.setCompanyId(null);
            return;
        }
        Integer companyId = user.getCompanyId() == null ? -1 : user.getCompanyId();
        //公司admin
        if ("2".equals(user.getRole())) {
            productRequest.setUserId(null);
            productRequest.setCompanyId(companyId);
            return;
        }
        //公司经理
        if ("3".equals(user.getRole()) || "4".equals(user.getRole())) {
            productRequest.setUserId(user.getId());
            productRequest.setCompanyId(companyId);
        }
    }
}
