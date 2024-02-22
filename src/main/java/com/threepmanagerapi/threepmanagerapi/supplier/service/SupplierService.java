package com.threepmanagerapi.threepmanagerapi.supplier.service;
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

import java.util.List;

@Service
@Slf4j
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity createSupplier(String token, Supplier supplier){
        try{
            Supplier existingSupplier = supplierRepository.findByPhone(supplier.getPhone());
            Long userID = jwtService.extractuserID(token);
            if(existingSupplier!=null){
                return responseService.formulateResponse(
                        null,
                        "Phone Number Already Exists",
                        HttpStatus.BAD_REQUEST,
                        null,
                        false
                );
            }

            supplier.setUser(userRepository.findByUserID(userID));

            supplierRepository.save(supplier);
            return responseService.formulateResponse(
                    null,
                    "Supplier added successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception creating supplier ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }

    public ResponseEntity getSupplier(){
        try{
            List<Supplier> suppliers = supplierRepository.findAll();
            return responseService.formulateResponse(
                    suppliers,
                    "Suppliers fetched successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception fetching suppliers ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }

        public ResponseEntity updateSupplier(Supplier supplier){
        try{
            Supplier existingSupplier = supplierRepository.findBySupplierID(supplier.getSupplierID());
            if(existingSupplier==null){
                return responseService.formulateResponse(
                        null,
                        "Supplier does not Exist",
                        HttpStatus.BAD_REQUEST,
                        null,
                        false
                );
            }

            supplierRepository.save(supplier);
            return responseService.formulateResponse(
                    null,
                    "Supplier updated successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception updating supplier ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
}
