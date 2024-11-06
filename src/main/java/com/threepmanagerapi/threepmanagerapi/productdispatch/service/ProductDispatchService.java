package com.threepmanagerapi.threepmanagerapi.productdispatch.service;

import com.threepmanagerapi.threepmanagerapi.client.model.Client;
import com.threepmanagerapi.threepmanagerapi.client.repository.ClientRepository;
import com.threepmanagerapi.threepmanagerapi.materialstocking.model.MaterialStocking;
import com.threepmanagerapi.threepmanagerapi.materialstocking.repository.MaterialStockingRepository;
import com.threepmanagerapi.threepmanagerapi.materialstocking.specification.MaterialStockingSpecification;
import com.threepmanagerapi.threepmanagerapi.mpesaintegration.service.MpesaIntegrationService;
import com.threepmanagerapi.threepmanagerapi.productdispatch.model.DispatchedProducts;
import com.threepmanagerapi.threepmanagerapi.productdispatch.model.ProductDispatch;
import com.threepmanagerapi.threepmanagerapi.productdispatch.repository.ProductDispatchRepository;
import com.threepmanagerapi.threepmanagerapi.productdispatch.specification.ProductDispatchSpecification;
import com.threepmanagerapi.threepmanagerapi.products.model.Product;
import com.threepmanagerapi.threepmanagerapi.productdispatch.repository.DispatchedProductsRepository;
import com.threepmanagerapi.threepmanagerapi.products.repository.ProductRepository;
import com.threepmanagerapi.threepmanagerapi.settings.service.EmailService;
import com.threepmanagerapi.threepmanagerapi.settings.service.JwtService;
import com.threepmanagerapi.threepmanagerapi.settings.utility.RandomCodeGenerator;
import com.threepmanagerapi.threepmanagerapi.settings.utility.ResponseService;
import com.threepmanagerapi.threepmanagerapi.supplier.model.Supplier;
import com.threepmanagerapi.threepmanagerapi.supplier.repository.SupplierRepository;
import com.threepmanagerapi.threepmanagerapi.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private MpesaIntegrationService mpesaIntegrationService;
    @Autowired
    private DispatchedProductsRepository dispatchedProductsRepository;
    @Autowired
    private ProductDispatchSpecification productDispatchSpecification;
    @Autowired
    private MaterialStockingRepository materialStockingRepository;
    @Autowired
    private MaterialStockingSpecification materialStockingSpecification;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    public ResponseEntity createProductDispatch(String token, ProductDispatch productDispatch){
        try{
            Long userID = jwtService.extractuserID(token);
            String productDispatchCode = RandomCodeGenerator.generateRandomCode();
            ProductDispatch existingProductDispatch = productDispatchRepository.findByProductDispatchCode(productDispatchCode);
            List<ProductDispatch> productDispatchList = productDispatchRepository.findBySupplier(productDispatch.getSupplier());
            boolean clientHasUnreturned = false;
            for(ProductDispatch prod: productDispatchList){
                if (!prod.isReturned()) {
                    clientHasUnreturned = true;
                    break;
                }
            }
            if(clientHasUnreturned){
                return responseService.formulateResponse(
                        null,
                        "The Supplier has an Unreturned Dispatch...Select Another Supplier or wait for Dispatch Return..",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        null,
                        false
                );
            }
            if(existingProductDispatch!=null){
                return responseService.formulateResponse(
                        null,
                        "A problem found occurred in generating code. Try Again...",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        null,
                        false
                );
            }
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
                dispatchedProducts.setReturnedQuantity(BigDecimal.valueOf(0));
                dispatchedProducts.setReturnedSpoiled(BigDecimal.valueOf(0));
                dispatchedProducts.setSoldQuantity(dispatchedProducts.getQuantity());
                dispatchedProducts.setSalesPrice(BigDecimal.valueOf(0));
                dispatchedProductsRepository.save(dispatchedProducts);
                price = price.add(dispatchedProducts.getTotalPrice());
            }
            productDispatch.setTotalDispatchPrice(price);
            productDispatch.setBalance(BigDecimal.valueOf(0));
            productDispatch.setTotalSalesPrice(BigDecimal.valueOf(0));
            productDispatch.setCratesIn(BigDecimal.valueOf(0));
            productDispatch.setProductDispatchCode(productDispatchCode);
            productDispatch.setReturned(false);
            productDispatch.setDispatchDate(LocalDateTime.now());
            productDispatchRepository.save(productDispatch);
            Supplier supplier = productDispatch.getSupplier();
            supplier.setCumulativeCratesOut(productDispatch.getCratesOut().add(supplier.getCumulativeCratesOut()));
            supplierRepository.save(supplier);
            emailService.sendProductDispatchCode(productDispatch.getSupplier().getAlternativeContact(),productDispatchCode,LocalDateTime.now().toString());
            emailService.sendProductDispatchCodeSms(productDispatch.getSupplier().getPhone(),productDispatchCode,LocalDateTime.now().toString());
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

    public ResponseEntity createProductDispatchReturn(ProductDispatch productDispatch){
        try{
            ProductDispatch existingProductDispatch = productDispatchRepository.findByProductDispatchCode(productDispatch.getProductDispatchCode());
            if(existingProductDispatch==null){
                return responseService.formulateResponse(
                        null,
                        "A problem found occurred in getting dispatch. Try Again...",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        null,
                        false
                );
            }
            for(DispatchedProducts dispatchedProducts: productDispatch.getDispatchedProducts()){
                DispatchedProducts existingDispatchedProduct = dispatchedProductsRepository.findByDispatchedProductID(dispatchedProducts.getDispatchedProductID());
                Product product = productRepository.findByProductID((existingDispatchedProduct.getProductID()));
                product.setRemainingQuantity(product.getRemainingQuantity().add(dispatchedProducts.getReturnedQuantity()));
                productRepository.save(product);
                existingDispatchedProduct.setReturnedQuantity(dispatchedProducts.getReturnedQuantity());
                existingDispatchedProduct.setReturnedSpoiled(dispatchedProducts.getReturnedSpoiled());
                existingDispatchedProduct.setSoldQuantity(dispatchedProducts.getSoldQuantity());
                existingDispatchedProduct.setSalesPrice(dispatchedProducts.getSalesPrice());
                dispatchedProductsRepository.save(existingDispatchedProduct);
            }

            existingProductDispatch.setBalance(productDispatch.getBalance());
            existingProductDispatch.setTotalSalesPrice(productDispatch.getTotalSalesPrice());
            existingProductDispatch.setCratesIn(productDispatch.getCratesIn());
            existingProductDispatch.setAmountPaid(productDispatch.getAmountPaid());
            existingProductDispatch.setReturnedDate(LocalDateTime.now());
            existingProductDispatch.setPaymentMode(productDispatch.getPaymentMode());
            existingProductDispatch.setReturned(true);
            productDispatchRepository.save(existingProductDispatch);
            Supplier supplier = existingProductDispatch.getSupplier();
            supplier.setCumulativeAmountToPay(supplier.getCumulativeAmountToPay().add(productDispatch.getTotalSalesPrice()));
            supplier.setCumulativeAmountPaid(supplier.getCumulativeAmountPaid().add(productDispatch.getAmountPaid()));
            supplier.setCumulativeAmountBalance(supplier.getCumulativeAmountBalance().add(productDispatch.getBalance()));
            supplier.setCumulativeCratesIn(supplier.getCumulativeCratesIn().add(productDispatch.getCratesIn()));
            supplierRepository.save(supplier);
            emailService.sendProductDispatchReturn(existingProductDispatch.getSupplier().getAlternativeContact(),existingProductDispatch.getProductDispatchCode(),LocalDateTime.now().toString(),productDispatch.getAmountPaid().toString(),productDispatch.getBalance().toString() );
            emailService.sendProductDispatchReturnSms(existingProductDispatch.getSupplier().getPhone(), existingProductDispatch.getProductDispatchCode(), LocalDateTime.now().toString(),productDispatch.getAmountPaid().toString(),productDispatch.getBalance().toString());
            return responseService.formulateResponse(
                    null,
                    "Dispatch Return done successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception returning material ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
    public ResponseEntity getProductDispatch(){
        try{
            List<ProductDispatch> productDispatches = productDispatchRepository.findByReturned(false);
            for (ProductDispatch productDispatch : productDispatches) {
                List<DispatchedProducts> dispatchedProducts = dispatchedProductsRepository.findByProductDispatchCode(productDispatch.getProductDispatchCode());
                for(DispatchedProducts dispatchedProducts1: dispatchedProducts){
                    dispatchedProducts1.setSalesPrice(dispatchedProducts1.getUnitPrice().multiply(dispatchedProducts1.getQuantity()));
                }
                productDispatch.setDispatchedProducts(dispatchedProducts);
                Duration duration = Duration.between(LocalDateTime.now(),productDispatch.getDispatchDate());
                productDispatch.setOverdueDays(Math.abs(duration.toDays()));
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
    public ResponseEntity getProductDispatchReturn(String startDate, String endDate){
        try{
            List<ProductDispatch> productDispatches = productDispatchRepository.findAll(productDispatchSpecification.getProductDispatchReturned(startDate, endDate));
            for (ProductDispatch productDispatch : productDispatches) {
                List<DispatchedProducts> dispatchedProducts = dispatchedProductsRepository.findByProductDispatchCode(productDispatch.getProductDispatchCode());
                productDispatch.setDispatchedProducts(dispatchedProducts);
                Duration duration = Duration.between(productDispatch.getReturnedDate(),productDispatch.getDispatchDate());
                productDispatch.setDaysTaken(Math.abs(duration.toDays()));
            }
            return responseService.formulateResponse(
                    productDispatches,
                    "Dispatch Returns fetched successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception fetching dispatch returns ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }

    public ResponseEntity getIngredientVsProduct(String startDate, String endDate){
        try{
            List<ProductDispatch> productDispatches = productDispatchRepository.findAll(productDispatchSpecification.getProductDispatchReturned(startDate, endDate));
            List<Object> objects = new ArrayList<>();
            BigDecimal total = BigDecimal.valueOf(0);
            for (ProductDispatch productDispatch : productDispatches) {
                List<DispatchedProducts> dispatchedProducts = dispatchedProductsRepository.findByProductDispatchCode(productDispatch.getProductDispatchCode());
                productDispatch.setDispatchedProducts(dispatchedProducts);
                total = total.add(productDispatch.getAmountPaid());
                for(DispatchedProducts dispatchedProducts1: dispatchedProducts){
                    dispatchedProducts1.setSaleDate(productDispatch.getReturnedDate());
                    dispatchedProducts1.setTotalSalesForRetrieval(total);
                }
                objects.add(dispatchedProducts);
            }
            List<MaterialStocking> materialStockings = materialStockingRepository.findAll(materialStockingSpecification.getMaterialStocking(startDate, endDate));
            HashMap<String, List> map = new HashMap<>();
            map.put("materials", materialStockings);
            map.put("products", objects);
            return responseService.formulateResponse(
                    map,
                    "Dispatch Returns fetched successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception fetching dispatch returns ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
}
