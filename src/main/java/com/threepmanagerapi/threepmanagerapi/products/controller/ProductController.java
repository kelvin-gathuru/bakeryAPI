package com.threepmanagerapi.threepmanagerapi.products.controller;
import com.threepmanagerapi.threepmanagerapi.products.model.Product;
import com.threepmanagerapi.threepmanagerapi.products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/sales")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/product/create")
    public ResponseEntity createProduct(@RequestHeader("Authorization") String token,
                                         @RequestBody Product product) {
        return productService.createProduct(token, product);
    }
    @GetMapping("/product/get")
    public ResponseEntity getProduct(@RequestHeader ("Authorization") String token) {
        return productService.getProducts();
    }
    @PostMapping("/product/update")
    public ResponseEntity updateProduct(@RequestHeader("Authorization") String token,
                                         @RequestBody Product product) {
        return productService.updateProduct(product);
    }
}
