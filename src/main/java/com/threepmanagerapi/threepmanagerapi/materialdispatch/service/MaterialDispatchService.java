package com.threepmanagerapi.threepmanagerapi.materialdispatch.service;

import com.threepmanagerapi.threepmanagerapi.materialdispatch.dto.MaterialDispatchCreateDto;
import com.threepmanagerapi.threepmanagerapi.materialdispatch.model.MaterialDispatch;
import com.threepmanagerapi.threepmanagerapi.materialdispatch.model.Shift;
import com.threepmanagerapi.threepmanagerapi.materialdispatch.repository.MaterialDispatchRepository;
import com.threepmanagerapi.threepmanagerapi.materials.model.Material;
import com.threepmanagerapi.threepmanagerapi.materials.repository.MaterialRepository;
import com.threepmanagerapi.threepmanagerapi.materialstocking.dto.MaterialStockUpdateDto;
import com.threepmanagerapi.threepmanagerapi.materialstocking.model.MaterialStocking;
import com.threepmanagerapi.threepmanagerapi.settings.service.JwtService;
import com.threepmanagerapi.threepmanagerapi.settings.utility.ResponseService;
import com.threepmanagerapi.threepmanagerapi.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MaterialDispatchService {
    @Autowired
    private MaterialDispatchRepository materialDispatchRepository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity createMaterialDispatch(String token, MaterialDispatchCreateDto materialDispatchCreateDto){
        try{
            Long userID = jwtService.extractuserID(token);
            Material material = materialRepository.findByMaterialID(materialDispatchCreateDto.getMaterial().getMaterialID());

            if(material==null){
                return responseService.formulateResponse(
                        null,
                        "Material not found ",
                        HttpStatus.NOT_FOUND,
                        null,
                        false
                );
            }
            if(material.getRemainingQuantity().compareTo(materialDispatchCreateDto.getInitialQuantity())<0){
                return responseService.formulateResponse(
                        null,
                        "Material not enough for this dispatch ",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        null,
                        false
                );
            }
            material.setRemainingQuantity(material.getRemainingQuantity().subtract(materialDispatchCreateDto.getInitialQuantity()));
            MaterialDispatch materialDispatch = new MaterialDispatch();
            materialDispatch.setUser(userRepository.findByUserID(userID));
            materialDispatch.setShift(Shift.valueOf(materialDispatchCreateDto.getShift()));
            materialDispatch.setDispatchDate(LocalDateTime.now());
            materialDispatch.setQuantity(materialDispatchCreateDto.getInitialQuantity());
            materialDispatch.setMaterial(materialDispatchCreateDto.getMaterial());
            materialDispatch.setDescription(materialDispatchCreateDto.getDescription());
            materialDispatch.setTotalPrice(materialDispatchCreateDto.getMaterial().getUnitPrice().multiply(materialDispatchCreateDto.getInitialQuantity()));
            materialDispatchRepository.save(materialDispatch);
            materialRepository.save(material);
            return responseService.formulateResponse(
                    null,
                    "Dispatch done successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception dispatching material ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
    public ResponseEntity getMaterialDispatch(){
        try{
            List<MaterialDispatch> materialDispatches = materialDispatchRepository.findAll();
            return responseService.formulateResponse(
                    materialDispatches,
                    "Dispatches fetched successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception fetching dispatches ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }

    public ResponseEntity updateMaterialDispatch(String token,MaterialDispatchCreateDto materialDispatchCreateDto){
        try{
            Long userID = jwtService.extractuserID(token);
            MaterialDispatch existingMaterialDispatch = materialDispatchRepository.findByMaterialDispatchID(materialDispatchCreateDto.getMaterialDispatchID());
            if(existingMaterialDispatch==null){
                return responseService.formulateResponse(
                        null,
                        "Dispatch does not Exist",
                        HttpStatus.BAD_REQUEST,
                        null,
                        false
                );
            }


            Material material = materialRepository.findByMaterialID(materialDispatchCreateDto.getMaterial().getMaterialID());

            if(material==null){
                return responseService.formulateResponse(
                        null,
                        "Material not found  ",
                        HttpStatus.NOT_FOUND,
                        null,
                        false
                );
            }
            BigDecimal updatedRemainingQuantity = material.getRemainingQuantity().add(materialDispatchCreateDto.getInitialQuantity());
            if(updatedRemainingQuantity.compareTo(materialDispatchCreateDto.getUpdatedQuantity())<0){
                return responseService.formulateResponse(
                        null,
                        "Material not enough for this dispatch ",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        null,
                        false
                );
            }
            material.setRemainingQuantity(updatedRemainingQuantity.subtract(materialDispatchCreateDto.getUpdatedQuantity()));
            existingMaterialDispatch.setMaterial(materialDispatchCreateDto.getMaterial());
            existingMaterialDispatch.setQuantity(materialDispatchCreateDto.getUpdatedQuantity());
            existingMaterialDispatch.setShift(Shift.valueOf(materialDispatchCreateDto.getShift()));
            existingMaterialDispatch.setDispatchDate(LocalDateTime.now());
            existingMaterialDispatch.setUser(userRepository.findByUserID(userID));
            existingMaterialDispatch.setDescription(materialDispatchCreateDto.getDescription());
            existingMaterialDispatch.setTotalPrice(materialDispatchCreateDto.getMaterial().getUnitPrice().multiply(materialDispatchCreateDto.getUpdatedQuantity()));
            materialDispatchRepository.save(existingMaterialDispatch);
            materialRepository.save(material);
            return responseService.formulateResponse(
                    null,
                    "dispatch UPDATED successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception updating dispatch ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
}
