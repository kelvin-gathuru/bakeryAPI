package com.threepmanagerapi.threepmanagerapi.supplier.controller;
import com.threepmanagerapi.threepmanagerapi.supplier.model.Supplier;
import com.threepmanagerapi.threepmanagerapi.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/sales/")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @PostMapping("/supplier/create")
    public ResponseEntity createSupplier(@RequestHeader("Authorization") String token,
                                       @RequestBody Supplier supplier) {
        return supplierService.createSupplier(token, supplier);
    }
    @GetMapping("/supplier/get")
    public ResponseEntity getSuppliers(@RequestHeader ("Authorization") String token) {
        return supplierService.getSupplier();
    }
    @PostMapping("/supplier/update")
    public ResponseEntity updateSupplier(@RequestHeader ("Authorization") String token,
                                        @RequestBody Supplier supplier) {
        return supplierService.updateSupplier(supplier);
    }
}
