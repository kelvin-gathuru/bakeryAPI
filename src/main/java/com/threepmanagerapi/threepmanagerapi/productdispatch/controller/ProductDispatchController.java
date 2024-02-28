package com.threepmanagerapi.threepmanagerapi.productdispatch.controller;

import com.threepmanagerapi.threepmanagerapi.materialdispatch.dto.MaterialDispatchCreateDto;
import com.threepmanagerapi.threepmanagerapi.productdispatch.model.ProductDispatch;
import com.threepmanagerapi.threepmanagerapi.productdispatch.service.ProductDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/sales")
public class ProductDispatchController {
    @Autowired
    private ProductDispatchService productDispatchService;
    @PostMapping("/productDispatch/create")
    public ResponseEntity createProductDispatch(@RequestHeader("Authorization") String token,
                                                 @RequestBody ProductDispatch productDispatch) {
        return productDispatchService.createProductDispatch(token, productDispatch);
    }
    @GetMapping("/productDispatch/get")
    public ResponseEntity getProductDispatch(@RequestHeader ("Authorization") String token) {
        return productDispatchService.getProductDispatch();
    }
}
