package com.smartfactory.maintenance.repository;
import com.smartfactory.maintenance.entity.MaintenanceRecord; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface MaintenanceRepository extends JpaRepository<MaintenanceRecord,Long>{List<MaintenanceRecord> findByMachineCodeOrderByCreatedAtDesc(String machineCode);}
