package com.threepmanagerapi.threepmanagerapi.materials.service;
import com.threepmanagerapi.threepmanagerapi.materials.model.Material;
import com.threepmanagerapi.threepmanagerapi.materials.repository.MaterialRepository;
import com.threepmanagerapi.threepmanagerapi.settings.service.JwtService;
import com.threepmanagerapi.threepmanagerapi.settings.utility.ResponseService;
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
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity createMaterial(String token, Material material){
        try{
            Material existingMaterial = materialRepository.findByName(material.getName());
            Long userID = jwtService.extractuserID(token);
            if(existingMaterial!=null){
                return responseService.formulateResponse(
                        null,
                        "Material Already Exists",
                        HttpStatus.BAD_REQUEST,
                        null,
                        false
                );
            }

            material.setUser(userRepository.findByUserID(userID));
            material.setName(material.getName().toUpperCase());
            material.setDateCreated(LocalDateTime.now());
            materialRepository.save(material);
            return responseService.formulateResponse(
                    null,
                    "Material added successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception creating material ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
    public ResponseEntity getMaterials(){
        try{
            List<Material> materials = materialRepository.findAll();
            return responseService.formulateResponse(
                    materials,
                    "Materials fetched successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception fetching materials ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
    public ResponseEntity updateMaterial(Material material){
        try{
            Material existingMaterial = materialRepository.findByMaterialID(material.getMaterialID());
            if(existingMaterial==null){
                return responseService.formulateResponse(
                        null,
                        "Material does not Exist",
                        HttpStatus.BAD_REQUEST,
                        null,
                        false
                );
            }


            material.setName(material.getName().toUpperCase());

            materialRepository.save(material);

            return responseService.formulateResponse(
                    null,
                    "Material updated successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception updating material ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
}
