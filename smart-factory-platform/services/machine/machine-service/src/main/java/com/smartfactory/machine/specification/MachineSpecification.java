package com.smartfactory.machine.specification;

import com.smartfactory.machine.entity.Machine;
import com.smartfactory.machine.util.MachineStatus;
import org.springframework.data.jpa.domain.Specification;

public class MachineSpecification {

    private MachineSpecification() {

    }

    public static Specification<Machine> hasStatus(MachineStatus status) {

        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status));
    }

    public static Specification<Machine> hasFactoryCode(String factoryCode) {

        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("factoryCode"), factoryCode));
    }

    public static Specification<Machine> hasType(String type) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type.toLowerCase()));
    }

    /**
     * Creates a specification for searching machines.
     * <p>
     * The search value is matched against:
     * - machineCode
     * - name
     * - type
     * - factoryCode
     * <p>
     * Example:
     * GET /api/v1/machines?search=CNC
     * <p>
     * Conceptually:
     * <p>
     * WHERE
     * machine_code LIKE '%cnc%'
     * OR name LIKE '%cnc%'
     * OR type LIKE '%cnc%'
     * OR factory_code LIKE '%cnc%'
     *
     * @param search text to search for
     * @return specification containing the search conditions
     */
    public static Specification<Machine> search(String search) {

        return (root, query, criteriaBuilder) -> {

            String pattern = "%" + search.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("machineCode")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("type")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("factoryCode")), pattern));
        };
    }
}
