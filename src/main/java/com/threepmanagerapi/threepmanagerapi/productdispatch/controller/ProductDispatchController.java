package com.threepmanagerapi.threepmanagerapi.productdispatch.controller;

import com.threepmanagerapi.threepmanagerapi.materialdispatch.dto.MaterialDispatchCreateDto;
import com.threepmanagerapi.threepmanagerapi.productdispatch.dto.ClientDispatchDto;
import com.threepmanagerapi.threepmanagerapi.productdispatch.model.ProductDispatch;
import com.threepmanagerapi.threepmanagerapi.productdispatch.service.ProductDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("/productDispatchReturn/create")
    public ResponseEntity createProductDispatchReturn(@RequestHeader("Authorization") String token,
                                                @RequestBody ProductDispatch productDispatch) {
        return productDispatchService.createProductDispatchReturn(productDispatch);
    }
    @GetMapping("/productDispatch/get")
    public ResponseEntity getProductDispatch(@RequestHeader ("Authorization") String token) {
        return productDispatchService.getProductDispatch();
    }
    @GetMapping("/productDispatchReturn/get")
    public ResponseEntity getProductDispatchReturn(@RequestHeader ("Authorization") String token,
                                                   @RequestParam(name = "startDate",required = false) String startDate,
                                                   @RequestParam(name = "endDate",required = false) String endDate) {
        return productDispatchService.getProductDispatchReturn(startDate, endDate);
    }
    @GetMapping("/ingredientVsProduct/get")
    public ResponseEntity getIngredientVsProduct(@RequestHeader ("Authorization") String token,
                                                   @RequestParam(name = "startDate",required = false) String startDate,
                                                   @RequestParam(name = "endDate",required = false) String endDate) {
        return productDispatchService.getIngredientVsProduct(startDate, endDate);
    }
    @PostMapping("/productDispatchForClient/create")
    public ResponseEntity createProductDispatchForClient(@RequestHeader("Authorization") String token,
                                                @RequestBody List<ClientDispatchDto> clientDispatchDtos) {
        System.out.println(clientDispatchDtos);
        return productDispatchService.createProductDispatchForClient(token, clientDispatchDtos);
    }
}
