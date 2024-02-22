package com.threepmanagerapi.threepmanagerapi.productstocking.controller;
import com.threepmanagerapi.threepmanagerapi.productstocking.model.MaterialStocking;
import com.threepmanagerapi.threepmanagerapi.productstocking.service.MaterialStockingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/sales")
public class MaterialStockingController {
    @Autowired
    private MaterialStockingService productStockingService;

    @PostMapping("/materialStock/create")
    public ResponseEntity createMaterialStock(@RequestHeader("Authorization") String token,
                                         @RequestBody MaterialStocking materialStocking) {
        return productStockingService.createMaterialStock(token, materialStocking);
    }
    @GetMapping("/materialStock/get")
    public ResponseEntity getMaterialStock(@RequestHeader ("Authorization") String token) {
        return productStockingService.getMaterialStock();
    }
    @PostMapping("/materialStock/update")
    public ResponseEntity updateMaterialStock(@RequestHeader("Authorization") String token,
                                         @RequestBody MaterialStocking materialStocking) {
        return productStockingService.updateMaterialStock(materialStocking);
    }
}
