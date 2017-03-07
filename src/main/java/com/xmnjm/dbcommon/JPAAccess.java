package com.xmnjm.dbcommon;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
public class JPAAccess {

    private EntityManager entityManager;

    public <T> T get(Class<T> entityClass, Object id) {
        try {
            return entityManager.find(entityClass, id);
        } catch (Exception e) {
            throw e;
        }
    }

    public void save(Object entity) {

        try {
            entityManager.persist(entity);
        } catch (Exception e) {
            throw e;
        }
    }

    public void update(Object entity) {
        try {
            entityManager.merge(entity);
        } catch (Exception e) {
            throw e;
        }
    }

    public void delete(Object entity) {
        try {
            entityManager.remove(entityManager.merge(entity));
        } catch (Exception e) {
            throw e;
        }
    }

    public void refresh(Object entity) {
        try {
            entityManager.refresh(entity);
        } catch (Exception e) {
            throw e;
        }
    }

    public void detach(Object entity) {
        try {
            entityManager.detach(entity);
        } catch (Exception e) {
            throw e;
        }
    }

    public <T> List<T> find(CriteriaQuery<T> query) {
        try {
            return entityManager.createQuery(query).getResultList();
        } catch (Exception e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> find(Query query) {
        try {
            return query.query(entityManager).getResultList();
        } catch (Exception e) {
            throw e;
        }
    }

    public <T> T findOne(CriteriaQuery<T> query) {
        try {
            List<T> results = entityManager.createQuery(query).getResultList();
            return getOne(results);
        } catch (Exception e) {
            throw e;
        }
    }

    private <T> T getOne(List<T> results) {
        if (results.isEmpty()) return null;
        if (results.size() > 1) {
            throw new NonUniqueResultException("result returned more than one element, returnedSize=" + results.size());
        }
        return results.get(0);
    }

    @SuppressWarnings("unchecked")
    public <T> T findOne(Query query) {
        try {
            List<T> results = query.query(entityManager).getResultList();
            return getOne(results);
        } catch (Exception e) {
            throw e;
        }
    }

    public int update(Query query) {
        try {
            return query.query(entityManager).executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    public void flush() {
        try {
            entityManager.flush();
        } catch (Exception e) {
            throw e;
        }
    }

    public CriteriaBuilder criteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
