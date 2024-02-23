package com.threepmanagerapi.threepmanagerapi.materials.controller;
import com.threepmanagerapi.threepmanagerapi.materials.model.Material;
import com.threepmanagerapi.threepmanagerapi.materials.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/sales")
public class MaterialController {
    @Autowired
    private MaterialService materialService;

    @PostMapping("/material/create")
    public ResponseEntity createMaterial(@RequestHeader("Authorization") String token,
                                       @RequestBody Material material) {
        return materialService.createMaterial(token, material);
    }
    @GetMapping("/material/get")
    public ResponseEntity getMaterial(@RequestHeader ("Authorization") String token) {
        return materialService.getMaterials();
    }
    @GetMapping("/material/getLowOnStock")
    public ResponseEntity getMaterialLowOnstock(@RequestHeader ("Authorization") String token) {
        return materialService.getMaterialsLowOnStock();
    }
    @PostMapping("/material/update")
    public ResponseEntity updateMaterial(@RequestHeader("Authorization") String token,
                                       @RequestBody Material material) {
        return materialService.updateMaterial(material);
    }
}
