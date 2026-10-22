package com.smartfactory.maintenance.controller;
import com.smartfactory.maintenance.dto.*; import com.smartfactory.maintenance.entity.MaintenanceRecord; import com.smartfactory.maintenance.repository.MaintenanceRepository; import jakarta.validation.Valid; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException; import java.util.List;
@RestController @RequestMapping("/api/v1/maintenance")
public class MaintenanceController {
 private final MaintenanceRepository repository; public MaintenanceController(MaintenanceRepository repository){this.repository=repository;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public MaintenanceRecord create(@Valid @RequestBody CreateMaintenanceRequest r){MaintenanceRecord m=new MaintenanceRecord();m.setMachineCode(r.machineCode());m.setDescription(r.description());m.setScheduledAt(r.scheduledAt());m.setStatus("OPEN");return repository.save(m);}
 @GetMapping public List<MaintenanceRecord> list(@RequestParam(required=false) String machineCode){return machineCode==null||machineCode.isBlank()?repository.findAll():repository.findByMachineCodeOrderByCreatedAtDesc(machineCode);}
 @GetMapping("/{id}") public MaintenanceRecord get(@PathVariable Long id){return repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Maintenance record not found"));}
 @PatchMapping("/{id}/status") public MaintenanceRecord status(@PathVariable Long id,@Valid @RequestBody UpdateMaintenanceStatusRequest r){MaintenanceRecord m=get(id);m.setStatus(r.status());return repository.save(m);}
}
