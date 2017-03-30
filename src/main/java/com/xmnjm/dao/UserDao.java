package com.xmnjm.dao;

import com.xmnjm.dbcommon.JPAAccess;
import com.xmnjm.dbcommon.Query;
import com.xmnjm.dbcommon.QueryBuilder;
import com.xmnjm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
public class UserDao {
    @Autowired
    JPAAccess jpaAccess;

    @Transactional
    public void save(User user) {
        jpaAccess.save(user);
    }

    @Transactional
    public void update(User user) {
        jpaAccess.update(user);
    }

    @Transactional
    public void delete(Long id) {
        jpaAccess.update(Query.create("delete User where id=:id").param("id", id));
    }

    public User findById(Integer id) {
        return jpaAccess.findOne(QueryBuilder.query("from User").append("id", id).append("status", 1).build());
    }

    public List<User> list(User user, int offset, int fetchSize) {
        QueryBuilder builder = QueryBuilder.query("from Order").skipNullFields()
            .append("status", user.getStatus())
            .desc();

        Query query = builder.build();
        query.from(offset);
        query.fetch(fetchSize);
        return jpaAccess.find(query);
    }

   public List<User> list(String queryField, String queryStr) {
       QueryBuilder queryBuilder = QueryBuilder.query("from User").skipEmptyFields()
               .append(queryField, '%' + queryStr + '%', "like");
       return jpaAccess.find(queryBuilder.build());
   }
}
