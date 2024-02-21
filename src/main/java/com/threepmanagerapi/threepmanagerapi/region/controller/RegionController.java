package com.threepmanagerapi.threepmanagerapi.region.controller;
import com.threepmanagerapi.threepmanagerapi.region.model.Region;
import com.threepmanagerapi.threepmanagerapi.region.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/sales")
public class RegionController {
    @Autowired
    private RegionService regionService;
    @PostMapping("/region/create")
    public ResponseEntity createRegion(@RequestHeader("Authorization") String token,
                                       @RequestBody Region region) {
        return regionService.createRegion(token, region);
    }
    @GetMapping("/region/get")
    public ResponseEntity getRegions(@RequestHeader ("Authorization") String token) {
        return regionService.getRegions();
    }
}
