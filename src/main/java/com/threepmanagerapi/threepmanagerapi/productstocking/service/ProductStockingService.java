package com.threepmanagerapi.threepmanagerapi.productstocking.service;

import com.threepmanagerapi.threepmanagerapi.materials.model.Material;
import com.threepmanagerapi.threepmanagerapi.materialstocking.dto.MaterialStockUpdateDto;
import com.threepmanagerapi.threepmanagerapi.materialstocking.model.MaterialStocking;
import com.threepmanagerapi.threepmanagerapi.products.model.Product;
import com.threepmanagerapi.threepmanagerapi.products.repository.ProductRepository;
import com.threepmanagerapi.threepmanagerapi.productstocking.dto.ProductStockUpdateDto;
import com.threepmanagerapi.threepmanagerapi.productstocking.model.ProductStocking;
import com.threepmanagerapi.threepmanagerapi.productstocking.repository.ProductStockingRepository;
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
public class ProductStockingService {
    @Autowired
    private ProductStockingRepository productStockingRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity createProductStock(String token, ProductStocking productStocking){
        try{
            Long userID = jwtService.extractuserID(token);
            Product product = productRepository.findByProductID(productStocking.getProduct().getProductID());

            if(product==null){
                return responseService.formulateResponse(
                        null,
                        "Product not found ",
                        HttpStatus.NOT_FOUND,
                        null,
                        false
                );
            }
            product.setRemainingQuantity(product.getRemainingQuantity().add(productStocking.getQuantity()));
            productStocking.setUser(userRepository.findByUserID(userID));
            productStocking.setStockDate(LocalDateTime.now());
            productStocking.setTotalPrice(productStocking.getUnitPrice().multiply(productStocking.getQuantity()));
            productStockingRepository.save(productStocking);
            productRepository.save(product);
            return responseService.formulateResponse(
                    null,
                    "Product Stock added successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception adding stock product ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
    public ResponseEntity getProductStock(){
        try{
            List<ProductStocking> productStockings = productStockingRepository.findAll();
            return responseService.formulateResponse(
                    productStockings,
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
    public ResponseEntity updateProductStock(ProductStockUpdateDto productStockUpdateDto){
        try{
            ProductStocking existingProductStocking = productStockingRepository.findByProductStockID(productStockUpdateDto.getProductStockID());
            if(existingProductStocking==null){
                return responseService.formulateResponse(
                        null,
                        "Product Stock does not Exist",
                        HttpStatus.BAD_REQUEST,
                        null,
                        false
                );
            }


            Product product = productRepository.findByProductID(productStockUpdateDto.getProduct().getProductID());

            if(product==null){
                return responseService.formulateResponse(
                        null,
                        "Product not found ",
                        HttpStatus.NOT_FOUND,
                        null,
                        false
                );
            }
            product.setRemainingQuantity((product.getRemainingQuantity().subtract(productStockUpdateDto.getInitialQuantity()).add(productStockUpdateDto.getUpdatedQuantity())));
            existingProductStocking.setProduct(productStockUpdateDto.getProduct());
            existingProductStocking.setQuantity(productStockUpdateDto.getUpdatedQuantity());
            existingProductStocking.setStockDate(LocalDateTime.now());
            existingProductStocking.setSpoiledAtProduction(productStockUpdateDto.getSpoiledAtProduction());
            existingProductStocking.setSpoiledAtPackaging(productStockUpdateDto.getSpoiledAtPackaging());
            existingProductStocking.setUser(productStockUpdateDto.getUser());
            existingProductStocking.setDescription(productStockUpdateDto.getDescription());
            existingProductStocking.setTotalPrice(productStockUpdateDto.getUnitPrice().multiply(productStockUpdateDto.getUpdatedQuantity()));
            productStockingRepository.save(existingProductStocking);
            productRepository.save(product);
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
