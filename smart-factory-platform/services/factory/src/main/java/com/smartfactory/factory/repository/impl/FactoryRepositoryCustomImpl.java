package com.smartfactory.factory.repository.impl;

import com.smartfactory.factory.entity.Factory;
import com.smartfactory.factory.repository.FactoryRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FactoryRepositoryCustomImpl implements FactoryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Factory> findFactories(int page, int size) {
        int offset = page * size;

        return entityManager
                .createQuery("SELECT f FROM Factory f ORDER BY f.id",Factory.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public long countFactories() {
        return  entityManager
                .createQuery("SELECT COUNT(f) FROM Factory f" , Long.class)
                .getSingleResult();
    }
}
