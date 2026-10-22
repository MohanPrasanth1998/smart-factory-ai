package com.smartfactory.factory.repository;

import com.smartfactory.factory.dto.FactoryResponse;
import com.smartfactory.factory.entity.Factory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FactoryRepository extends JpaRepository<Factory,Long>,FactoryRepositoryCustom {

    boolean existsByCode(String code);
    Optional<Factory> findByCode(String code);
}
