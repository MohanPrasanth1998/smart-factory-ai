package com.smartfactory.telemetry_service.repository;


import com.smartfactory.telemetry_service.entity.Telemetry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TelemetryRepository extends JpaRepository<Telemetry, Long> {

    /*
            SELECT *
        FROM telemetry
        WHERE machine_code = ?
        ORDER BY timestamp DESC;
     */


    List<Telemetry> findByMachineCodeOrderByTimestampDesc(
            String machineCode
    );


    /*
            SELECT *
        FROM telemetry
        WHERE machine_code = ?
          AND timestamp BETWEEN ? AND ?
        ORDER BY timestamp DESC;
     */

    List<Telemetry> findByMachineCodeAndTimestampBetweenOrderByTimestampDesc(
            String machineCode,
            Instant from,
            Instant to
    );


    @Query("""
            SELECT t FROM Telemetry t
            WHERE t.machineCode = :machineCode
            ORDER BY t.timestamp DESC, t.id DESC
""")
   List<Telemetry> findFirstTelemetry(@Param("machineCode") String machineCode,Pageable pageable);


    @Query("""
        SELECT t FROM Telemetry t
        WHERE t.machineCode = :machineCode
        AND(
        t.timestamp < :timestamp
        OR(
            t.timestamp = timestamp
            AND t.id < :id
        )
        )
        ORDER BY t.timestamp DESC, t.id DESC
""")
    List<Telemetry> findNextTelemetry(@Param("machineCode") String machineCode,
                                      @Param("timestamp") Instant timestamp,
                                         @Param("id") Long id, Pageable pageable);
}
