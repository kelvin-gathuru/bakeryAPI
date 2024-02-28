package com.threepmanagerapi.threepmanagerapi.productstocking.controller;

import com.threepmanagerapi.threepmanagerapi.materialstocking.dto.MaterialStockUpdateDto;
import com.threepmanagerapi.threepmanagerapi.materialstocking.model.MaterialStocking;
import com.threepmanagerapi.threepmanagerapi.productstocking.dto.ProductStockUpdateDto;
import com.threepmanagerapi.threepmanagerapi.productstocking.model.ProductStocking;
import com.threepmanagerapi.threepmanagerapi.productstocking.service.ProductStockingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/sales")
public class ProductStockingController {
    @Autowired
    private ProductStockingService productStockingService;

    @PostMapping("/productStock/create")
    public ResponseEntity createProductStock(@RequestHeader("Authorization") String token,
                                              @RequestBody ProductStocking productStocking) {
        return productStockingService.createProductStock(token, productStocking);
    }
    @GetMapping("/productStock/get")
    public ResponseEntity getProductStock(@RequestHeader ("Authorization") String token) {
        return productStockingService.getProductStock();
    }
    @PostMapping("/productStock/update")
    public ResponseEntity updateProductStock(@RequestHeader("Authorization") String token,
                                              @RequestBody ProductStockUpdateDto productStockUpdateDto) {
        return productStockingService.updateProductStock(productStockUpdateDto);
    }
}
