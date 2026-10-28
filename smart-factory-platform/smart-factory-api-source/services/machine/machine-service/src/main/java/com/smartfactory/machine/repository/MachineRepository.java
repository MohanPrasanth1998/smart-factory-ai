package com.smartfactory.machine.repository;

import com.smartfactory.machine.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MachineRepository extends JpaRepository<Machine,Long>, JpaSpecificationExecutor<Machine> {

    boolean existsByMachineCode(String machineCode);

    Optional<Machine> findByMachineCode(String machineCode);
}
