package com.threepmanagerapi.threepmanagerapi.analytics.service;

import com.threepmanagerapi.threepmanagerapi.client.model.Client;
import com.threepmanagerapi.threepmanagerapi.client.repository.ClientRepository;
import com.threepmanagerapi.threepmanagerapi.materials.model.Material;
import com.threepmanagerapi.threepmanagerapi.materials.repository.MaterialRepository;
import com.threepmanagerapi.threepmanagerapi.region.model.Region;
import com.threepmanagerapi.threepmanagerapi.region.repository.RegionRepository;
import com.threepmanagerapi.threepmanagerapi.settings.Model.Status;
import com.threepmanagerapi.threepmanagerapi.settings.service.JwtService;
import com.threepmanagerapi.threepmanagerapi.settings.utility.ResponseService;
import com.threepmanagerapi.threepmanagerapi.supplier.model.Supplier;
import com.threepmanagerapi.threepmanagerapi.supplier.repository.SupplierRepository;
import com.threepmanagerapi.threepmanagerapi.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnalyticsService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ClientRepository clientRepository;

    public ResponseEntity getAnalytics(){
        try{
            List<Client> clients = clientRepository.findAll();
            List<Client> activeClients = clientRepository.findByStatus(Status.ACTIVE);
            List<Region> regions = regionRepository.findAll();
            List<Region> activeRegions = regionRepository.findByStatus(Status.ACTIVE);
            List<Material> materials = materialRepository.findAll();
            List<Material> latestMaterials = materials.stream().filter(
                    material -> material.getDateCreated().isAfter(LocalDateTime.now().minusWeeks(1))).toList();
            List<Material> recentMaterials = materials.stream()
                    .sorted(Comparator.comparing(Material::getDateCreated).reversed())
                    .limit(3)
                    .toList();

            Map<String, Object> response = new HashMap<>();
            response.put("clients", clients.size());
            response.put("activeClients",activeClients.size());
            response.put("regions",regions.size());
            response.put("activeRegions",activeRegions.size());
            response.put("materials",materials.size());
            response.put("latestMaterials",latestMaterials.size());
            response.put("recentMaterials",recentMaterials);
            return responseService.formulateResponse(
                    response,
                    "Analytics fetched successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception fetching analytics ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
}
