package com.smartfactory.production.repository;
import com.smartfactory.production.entity.ProductionOrder; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface ProductionOrderRepository extends JpaRepository<ProductionOrder,Long>{List<ProductionOrder> findByMachineCodeOrderByCreatedAtDesc(String machineCode);}
