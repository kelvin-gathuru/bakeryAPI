package com.threepmanagerapi.threepmanagerapi.analytics.service;

import com.threepmanagerapi.threepmanagerapi.analytics.dto.SalesDataDto;
import com.threepmanagerapi.threepmanagerapi.client.model.Client;
import com.threepmanagerapi.threepmanagerapi.client.repository.ClientRepository;
import com.threepmanagerapi.threepmanagerapi.materials.model.Material;
import com.threepmanagerapi.threepmanagerapi.materials.repository.MaterialRepository;
import com.threepmanagerapi.threepmanagerapi.productdispatch.model.ProductDispatch;
import com.threepmanagerapi.threepmanagerapi.productdispatch.repository.ProductDispatchRepository;
import com.threepmanagerapi.threepmanagerapi.products.model.Product;
import com.threepmanagerapi.threepmanagerapi.products.repository.ProductRepository;
import com.threepmanagerapi.threepmanagerapi.region.model.Region;
import com.threepmanagerapi.threepmanagerapi.region.repository.RegionRepository;
import com.threepmanagerapi.threepmanagerapi.settings.Model.Status;
import com.threepmanagerapi.threepmanagerapi.settings.service.JwtService;
import com.threepmanagerapi.threepmanagerapi.settings.utility.ResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class AnalyticsService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductDispatchRepository productDispatchRepository;


    public ResponseEntity getAnalytics(){
        try{
            List<Client> clients = clientRepository.findAll();
            List<Client> activeClients = clientRepository.findByStatus(Status.ACTIVE);
            List<Region> regions = regionRepository.findAll();
            List<Region> activeRegions = regionRepository.findByStatus(Status.ACTIVE);
            List<Material> materials = materialRepository.findAll();
            List<Material> materialsBelowReorderPoint = materials.stream()
                    .filter(material -> material.getRemainingQuantity().compareTo(material.getReorderPoint()) < 0)
                    .toList();
            List<Material> latestMaterials = materials.stream().filter(
                    material -> material.getDateCreated().isAfter(LocalDateTime.now().minusWeeks(1))).toList();
            List<Material> recentMaterials = materials.stream()
                    .sorted(Comparator.comparing(Material::getDateCreated).reversed())
                    .limit(3)
                    .toList();
            List<Product> products = productRepository.findAll();
            List<Product> productsBelowReorderPoint = products.stream()
                    .filter(product -> product.getRemainingQuantity().compareTo(product.getReorderPoint()) < 0)
                    .toList();
            List<Product> latestProducts = products.stream().filter(
                    product -> product.getDateCreated().isAfter(LocalDateTime.now().minusDays(1))).toList();

            List<Product> bestPricedProducts = productRepository.findAll().stream()
                    .sorted(Comparator.comparing(Product::getUnitPrice).reversed())
                    .limit(3)
                    .toList();

            Map<String, Object> response = new HashMap<>();
            response.put("clients", clients.size());
            response.put("activeClients",activeClients.size());
            response.put("regions",regions.size());
            response.put("activeRegions",activeRegions.size());
            response.put("materials",materials.size());
            response.put("materialsBelowReorderPoint",materialsBelowReorderPoint.size());
            response.put("latestMaterials",latestMaterials.size());
            response.put("recentMaterials",recentMaterials);
            response.put("bestPricedProducts",bestPricedProducts);
            response.put("products",products.size());
            response.put("productsBelowReorderPoint",productsBelowReorderPoint.size());
            response.put("latestProducts",latestProducts.size());
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
    public ResponseEntity getChartData(){
        try {
            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = endDate.minusDays(6);
            List<SalesDataDto> salesDataDtos = new ArrayList<>();

            List<ProductDispatch> productDispatches = productDispatchRepository.findByReturnedDateBetween(startDate, endDate).stream().filter(
                    ProductDispatch::isReturned).toList();

            for(ProductDispatch productDispatch: productDispatches){
                SalesDataDto salesDataDto = new SalesDataDto();
                salesDataDto.setDay(productDispatch.getReturnedDate().getDayOfWeek().toString());
                salesDataDto.setAmount(productDispatch.getAmountPaid());
                salesDataDtos.add(salesDataDto);
            }
            for (int i = 0; i < 7; i++) {
                SalesDataDto salesDataDto2 = new SalesDataDto();
                salesDataDto2.setDay(endDate.getDayOfWeek().toString());
                salesDataDto2.setAmount(BigDecimal.valueOf(0));
                salesDataDtos.add(salesDataDto2);
                endDate = endDate.minusDays(1);
            }

            Map<String, BigDecimal> totals = new HashMap<>();
            for (SalesDataDto salesDataDtos1: salesDataDtos) {
                String day = (String) salesDataDtos1.getDay();
                BigDecimal amount = (BigDecimal) salesDataDtos1.getAmount();
                totals.put(day, totals.getOrDefault(day, BigDecimal.valueOf(0.0)).add(amount));
            }
            return responseService.formulateResponse(
                    totals,
                    "Success fetching sales analytics ",
                    HttpStatus.OK,
                    null,
                    true
            );
        }catch (Exception exception) {
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
