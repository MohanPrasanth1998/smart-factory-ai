package com.smartfactory.production.controller;
import com.smartfactory.production.dto.*; import com.smartfactory.production.entity.ProductionOrder; import com.smartfactory.production.repository.ProductionOrderRepository; import jakarta.validation.Valid; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException; import java.util.List;
@RestController @RequestMapping("/api/v1/production/orders")
public class ProductionController {
 private final ProductionOrderRepository repository; public ProductionController(ProductionOrderRepository repository){this.repository=repository;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public ProductionOrder create(@Valid @RequestBody CreateProductionOrderRequest r){ProductionOrder o=new ProductionOrder();o.setMachineCode(r.machineCode());o.setProductCode(r.productCode());o.setPlannedQuantity(r.plannedQuantity());o.setCompletedQuantity(0);o.setStatus("PLANNED");return repository.save(o);}
 @GetMapping public List<ProductionOrder> list(@RequestParam(required=false) String machineCode){return machineCode==null||machineCode.isBlank()?repository.findAll():repository.findByMachineCodeOrderByCreatedAtDesc(machineCode);}
 @GetMapping("/{id}") public ProductionOrder get(@PathVariable Long id){return repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Production order not found"));}
 @PatchMapping("/{id}/progress") public ProductionOrder progress(@PathVariable Long id,@Valid @RequestBody UpdateProductionProgressRequest r){ProductionOrder o=get(id);if(r.completedQuantity()>o.getPlannedQuantity())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Completed quantity cannot exceed planned quantity");o.setCompletedQuantity(r.completedQuantity());o.setStatus(r.completedQuantity()==o.getPlannedQuantity()?"COMPLETED":r.completedQuantity()>0?"IN_PROGRESS":"PLANNED");return repository.save(o);}
}
