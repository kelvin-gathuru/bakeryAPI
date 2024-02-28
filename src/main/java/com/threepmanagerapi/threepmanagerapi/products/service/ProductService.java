package com.threepmanagerapi.threepmanagerapi.products.service;

import com.threepmanagerapi.threepmanagerapi.materials.model.Material;
import com.threepmanagerapi.threepmanagerapi.products.model.Product;
import com.threepmanagerapi.threepmanagerapi.products.repository.ProductRepository;
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
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity createProduct(String token, Product product){
        try{
            Product existingProduct = productRepository.findByName(product.getName());
            Long userID = jwtService.extractuserID(token);
            if(existingProduct!=null){
                return responseService.formulateResponse(
                        null,
                        "Product Already Exists",
                        HttpStatus.BAD_REQUEST,
                        null,
                        false
                );
            }

            product.setUser(userRepository.findByUserID(userID));
            product.setName(product.getName().toUpperCase());
            product.setMetric(product.getMetric().toUpperCase());
            product.setDateCreated(LocalDateTime.now());
            productRepository.save(product);
            return responseService.formulateResponse(
                    null,
                    "Product added successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception creating product ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
    public ResponseEntity getProducts(){
        try{
            List<Product> products = productRepository.findAll();
            return responseService.formulateResponse(
                    products,
                    "Products fetched successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception fetching products ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
    public ResponseEntity getProductsLowOnStock(){
        try{
            List<Product> products = productRepository.findAll();
            List<Product> productsBelowReorderPoint = products.stream()
                    .filter(product -> product.getRemainingQuantity().compareTo(product.getReorderPoint()) < 0)
                    .toList();
            return responseService.formulateResponse(
                    productsBelowReorderPoint,
                    "Products fetched successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception fetching Products ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
    public ResponseEntity updateProduct(Product product){
        try{
            Product existingProduct = productRepository.findByProductID(product.getProductID());
            if(existingProduct==null){
                return responseService.formulateResponse(
                        null,
                        "Product does not Exist",
                        HttpStatus.BAD_REQUEST,
                        null,
                        false
                );
            }


            product.setName(product.getName().toUpperCase());

            product.setMetric(product.getMetric().toUpperCase());

            productRepository.save(product);

            return responseService.formulateResponse(
                    null,
                    "Product updated successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception updating Product ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
}
