package com.smartfactory.factory.repository;

import com.smartfactory.factory.entity.Factory;

import java.util.List;

public interface FactoryRepositoryCustom {

    List<Factory> findFactories(int page, int size);

    long countFactories();
}
