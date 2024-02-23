package com.threepmanagerapi.threepmanagerapi.materialdispatch.controller;

import com.threepmanagerapi.threepmanagerapi.materialdispatch.dto.MaterialDispatchCreateDto;
import com.threepmanagerapi.threepmanagerapi.materialdispatch.model.MaterialDispatch;
import com.threepmanagerapi.threepmanagerapi.materialdispatch.service.MaterialDispatchService;
import com.threepmanagerapi.threepmanagerapi.materialstocking.model.MaterialStocking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/sales")
public class MaterialDispatchController {
    @Autowired
    private MaterialDispatchService materialDispatchService;

    @PostMapping("/materialDispatch/create")
    public ResponseEntity createMaterialDispatch(@RequestHeader("Authorization") String token,
                                              @RequestBody MaterialDispatchCreateDto materialDispatchCreateDto) {
        return materialDispatchService.createMaterialDispatch(token, materialDispatchCreateDto);
    }
    @GetMapping("/materialDispatch/get")
    public ResponseEntity getMaterialDispatch(@RequestHeader ("Authorization") String token) {
        return materialDispatchService.getMaterialDispatch();
    }
    @PostMapping("/materialDispatch/update")
    public ResponseEntity updateMaterialDispatch(@RequestHeader("Authorization") String token,
                                                 @RequestBody MaterialDispatchCreateDto materialDispatchCreateDto) {
        return materialDispatchService.updateMaterialDispatch(token, materialDispatchCreateDto);
    }
}
