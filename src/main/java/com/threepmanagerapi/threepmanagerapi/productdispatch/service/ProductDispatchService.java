package com.threepmanagerapi.threepmanagerapi.productdispatch.service;

import com.threepmanagerapi.threepmanagerapi.productdispatch.model.DispatchedProducts;
import com.threepmanagerapi.threepmanagerapi.productdispatch.model.ProductDispatch;
import com.threepmanagerapi.threepmanagerapi.productdispatch.repository.ProductDispatchRepository;
import com.threepmanagerapi.threepmanagerapi.products.model.Product;
import com.threepmanagerapi.threepmanagerapi.productdispatch.repository.DispatchedProductsRepository;
import com.threepmanagerapi.threepmanagerapi.products.repository.ProductRepository;
import com.threepmanagerapi.threepmanagerapi.settings.service.JwtService;
import com.threepmanagerapi.threepmanagerapi.settings.utility.RandomCodeGenerator;
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
public class ProductDispatchService {
    @Autowired
    private ProductDispatchRepository productDispatchRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DispatchedProductsRepository dispatchedProductsRepository;

    public ResponseEntity createProductDispatch(String token, ProductDispatch productDispatch){
        try{
            Long userID = jwtService.extractuserID(token);
            String productDispatchCode = RandomCodeGenerator.generateRandomCode();
            BigDecimal price = BigDecimal.valueOf(0);
            for(DispatchedProducts dispatchedProducts: productDispatch.getDispatchedProducts()){
                Product product = productRepository.findByProductID((dispatchedProducts.getProductID()));
                if(dispatchedProducts.getRemainingStock()==null){
                    return responseService.formulateResponse(
                            null,
                            "A problem found in " + dispatchedProducts.getName()+ " Remove products not required",
                            HttpStatus.NOT_FOUND,
                            null,
                            false
                    );
                }
                if(product==null){
                    return responseService.formulateResponse(
                            null,
                            "Product not found ",
                            HttpStatus.NOT_FOUND,
                            null,
                            false
                    );
                }

                product.setRemainingQuantity(dispatchedProducts.getRemainingStock());
                productRepository.save(product);
                dispatchedProducts.setDateCreated(LocalDateTime.now());
                dispatchedProducts.setProductDispatchCode(productDispatchCode);
                dispatchedProductsRepository.save(dispatchedProducts);
                price = price.add(dispatchedProducts.getTotalPrice());
            }
            productDispatch.setTotalDispatchPrice(price);
            productDispatch.setProductDispatchCode(productDispatchCode);
            productDispatchRepository.save(productDispatch);
            return responseService.formulateResponse(
                    productDispatchCode,
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
    public ResponseEntity getProductDispatch(){
        try{
            List<ProductDispatch> productDispatches = productDispatchRepository.findAll();
            for (ProductDispatch productDispatch : productDispatches) {
                List<DispatchedProducts> dispatchedProducts = dispatchedProductsRepository.findByProductDispatchCode(productDispatch.getProductDispatchCode());
                productDispatch.setDispatchedProducts(dispatchedProducts);
            }
            return responseService.formulateResponse(
                    productDispatches,
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
}
