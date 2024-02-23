package com.threepmanagerapi.threepmanagerapi.materialstocking.controller;
import com.threepmanagerapi.threepmanagerapi.materialstocking.dto.MaterialStockUpdateDto;
import com.threepmanagerapi.threepmanagerapi.materialstocking.model.MaterialStocking;
import com.threepmanagerapi.threepmanagerapi.materialstocking.service.MaterialStockingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/sales")
public class MaterialStockingController {
    @Autowired
    private MaterialStockingService materialStockingService;

    @PostMapping("/materialStock/create")
    public ResponseEntity createMaterialStock(@RequestHeader("Authorization") String token,
                                         @RequestBody MaterialStocking materialStocking) {
        return materialStockingService.createMaterialStock(token, materialStocking);
    }
    @GetMapping("/materialStock/get")
    public ResponseEntity getMaterialStock(@RequestHeader ("Authorization") String token) {
        return materialStockingService.getMaterialStock();
    }
    @PostMapping("/materialStock/update")
    public ResponseEntity updateMaterialStock(@RequestHeader("Authorization") String token,
                                         @RequestBody MaterialStockUpdateDto materialStockUpdateDto) {
        return materialStockingService.updateMaterialStock(materialStockUpdateDto);
    }
}
