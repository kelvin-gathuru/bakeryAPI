package com.threepmanagerapi.threepmanagerapi.materialstocking.service;

import com.threepmanagerapi.threepmanagerapi.materials.model.Material;
import com.threepmanagerapi.threepmanagerapi.materials.repository.MaterialRepository;
import com.threepmanagerapi.threepmanagerapi.materialstocking.dto.MaterialStockUpdateDto;
import com.threepmanagerapi.threepmanagerapi.materialstocking.model.MaterialStocking;
import com.threepmanagerapi.threepmanagerapi.materialstocking.repository.MaterialStockingRepository;
import com.threepmanagerapi.threepmanagerapi.settings.service.JwtService;
import com.threepmanagerapi.threepmanagerapi.settings.utility.ResponseService;
import com.threepmanagerapi.threepmanagerapi.supplier.repository.SupplierRepository;
import com.threepmanagerapi.threepmanagerapi.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MaterialStockingService {
    @Autowired
    private MaterialStockingRepository materialStockingRepository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    public ResponseEntity createMaterialStock(String token, MaterialStocking materialStocking){
        try{
            Long userID = jwtService.extractuserID(token);
            Material material = materialRepository.findByMaterialID(materialStocking.getMaterial().getMaterialID());

            if(material==null){
                return responseService.formulateResponse(
                        null,
                        "Material not found ",
                        HttpStatus.NOT_FOUND,
                        null,
                        false
                );
            }
            material.setRemainingQuantity(material.getRemainingQuantity().add(materialStocking.getQuantity()));
            materialStocking.setUser(userRepository.findByUserID(userID));
            materialStocking.setPurchaseDate(LocalDateTime.now());
            materialStocking.setTotalPrice(materialStocking.getMaterial().getUnitPrice().multiply(materialStocking.getQuantity()));
            materialStockingRepository.save(materialStocking);
            materialRepository.save(material);
            return responseService.formulateResponse(
                    null,
                    "Stock added successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception adding stock material ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
    public ResponseEntity getMaterialStock(){
        try{
            List<MaterialStocking> materialStockings = materialStockingRepository.findAll();
            return responseService.formulateResponse(
                    materialStockings,
                    "Stock fetched successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception fetching stocks ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
    public ResponseEntity updateMaterialStock(MaterialStockUpdateDto materialStockUpdateDto){
        try{
            MaterialStocking existingMaterialStocking = materialStockingRepository.findByMaterialStockID(materialStockUpdateDto.getMaterialStockID());
            if(existingMaterialStocking==null){
                return responseService.formulateResponse(
                        null,
                        "Stock does not Exist",
                        HttpStatus.BAD_REQUEST,
                        null,
                        false
                );
            }


            Material material = materialRepository.findByMaterialID(materialStockUpdateDto.getMaterial().getMaterialID());

            if(material==null){
                return responseService.formulateResponse(
                        null,
                        "Material not found ",
                        HttpStatus.NOT_FOUND,
                        null,
                        false
                );
            }
            material.setRemainingQuantity((material.getRemainingQuantity().subtract(materialStockUpdateDto.getInitialQuantity()).add(materialStockUpdateDto.getUpdatedQuantity())));
            existingMaterialStocking.setMaterial(materialStockUpdateDto.getMaterial());
            existingMaterialStocking.setQuantity(materialStockUpdateDto.getUpdatedQuantity());
            existingMaterialStocking.setSupplier(materialStockUpdateDto.getSupplier());
            existingMaterialStocking.setPurchaseDate(LocalDateTime.now());
            existingMaterialStocking.setUser(materialStockUpdateDto.getUser());
            existingMaterialStocking.setDescription(materialStockUpdateDto.getDescription());
            existingMaterialStocking.setTotalPrice(materialStockUpdateDto.getMaterial().getUnitPrice().multiply(materialStockUpdateDto.getUpdatedQuantity()));
            materialStockingRepository.save(existingMaterialStocking);
            materialRepository.save(material);
            return responseService.formulateResponse(
                    null,
                    "Stock UPDATED successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception updating stock ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
}
