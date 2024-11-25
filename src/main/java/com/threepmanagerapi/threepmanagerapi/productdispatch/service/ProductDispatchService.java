package com.threepmanagerapi.threepmanagerapi.productdispatch.service;

import com.threepmanagerapi.threepmanagerapi.client.model.Client;
import com.threepmanagerapi.threepmanagerapi.client.repository.ClientRepository;
import com.threepmanagerapi.threepmanagerapi.materialstocking.model.MaterialStocking;
import com.threepmanagerapi.threepmanagerapi.materialstocking.repository.MaterialStockingRepository;
import com.threepmanagerapi.threepmanagerapi.materialstocking.specification.MaterialStockingSpecification;
import com.threepmanagerapi.threepmanagerapi.mpesaintegration.service.MpesaIntegrationService;
import com.threepmanagerapi.threepmanagerapi.productdispatch.dto.ClientDispatchDto;
import com.threepmanagerapi.threepmanagerapi.productdispatch.dto.CratesDto;
import com.threepmanagerapi.threepmanagerapi.productdispatch.dto.PaymentDto;
import com.threepmanagerapi.threepmanagerapi.productdispatch.model.ClientsDispatchedProducts;
import com.threepmanagerapi.threepmanagerapi.productdispatch.model.ClientsProductDispatch;
import com.threepmanagerapi.threepmanagerapi.productdispatch.model.DispatchedProducts;
import com.threepmanagerapi.threepmanagerapi.productdispatch.model.ProductDispatch;
import com.threepmanagerapi.threepmanagerapi.productdispatch.repository.ClientDispatchedProductsRepository;
import com.threepmanagerapi.threepmanagerapi.productdispatch.repository.ClientsProductDispatchRepository;
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
import com.threepmanagerapi.threepmanagerapi.user.model.User;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientDispatchedProductsRepository clientDispatchedProductsRepository;
    @Autowired
    private ClientsProductDispatchRepository clientsProductDispatchRepository;

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
                dispatchedProducts.setPrice(dispatchedProducts.getPrice());
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
                    dispatchedProducts1.setSalesPrice(dispatchedProducts1.getPrice().multiply(dispatchedProducts1.getQuantity()));
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

    public ResponseEntity createProductDispatchForClient(String token, List<ClientDispatchDto> clientDispatchDtos){
        try{
            Long userID = jwtService.extractuserID(token);
            if(clientDispatchDtos.isEmpty()){
                return responseService.formulateResponse(
                        null,
                        "No Clients dispatch. Proceed...",
                        HttpStatus.OK,
                        null,
                        true
                );
            }
            ProductDispatch existingProductDispatch = productDispatchRepository.findByProductDispatchCode(clientDispatchDtos.get(0).getDispatchedProducts().get(0).getProductDispatchCode());
            User user = userRepository.findByUserID(userID);

            if(existingProductDispatch==null){
                return responseService.formulateResponse(
                        null,
                        "A problem occurred in finding the dispatch. Try Again...",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        null,
                        false
                );
            }
            BigDecimal amount = BigDecimal.valueOf(0);
            for (ClientDispatchDto clientDispatchDto : clientDispatchDtos) {
                BigDecimal total = BigDecimal.valueOf(0);
                BigDecimal clientsTotals = BigDecimal.valueOf(0);
                for(DispatchedProducts clientsDispatchedProducts: clientDispatchDto.getDispatchedProducts()){
                    if(clientsDispatchedProducts.getDeliveredQuantity().compareTo(BigDecimal.ZERO) > 0){
                        ClientsDispatchedProducts clientsDispatchedProducts1 = new ClientsDispatchedProducts();
                        clientsDispatchedProducts1.setProductDispatchCode(existingProductDispatch.getProductDispatchCode());
                        clientsDispatchedProducts1.setProductID(clientsDispatchedProducts.getProductID());
                        clientsDispatchedProducts1.setClientID(clientDispatchDto.getClientID());
                        clientsDispatchedProducts1.setName(clientsDispatchedProducts.getName());
                        clientsDispatchedProducts1.setMetric(clientsDispatchedProducts.getMetric());
                        clientsDispatchedProducts1.setDeliveredQuantity(clientsDispatchedProducts.getDeliveredQuantity());
                        clientsDispatchedProducts1.setUser(user);
                        clientsDispatchedProducts1.setSaleDate(LocalDateTime.now());
                        clientsDispatchedProducts1.setPrice(clientsDispatchedProducts.getPrice());
                        clientsDispatchedProducts1.setDeliveredProductPrice(clientsDispatchedProducts.getPrice().multiply(clientsDispatchedProducts.getDeliveredQuantity()));
                        clientDispatchedProductsRepository.save(clientsDispatchedProducts1);
                        total = total.add(clientsDispatchedProducts1.getDeliveredProductPrice());

                        DispatchedProducts dispatchedProducts = dispatchedProductsRepository.findByProductDispatchCodeAndDispatchedProductID(existingProductDispatch.getProductDispatchCode(), clientsDispatchedProducts.getDispatchedProductID());
                        dispatchedProducts.setSalesPrice(dispatchedProducts.getSalesPrice().subtract(clientsDispatchedProducts.getTotalPrice()));
                        dispatchedProducts.setQuantity(dispatchedProducts.getQuantity().subtract(clientsDispatchedProducts.getDeliveredQuantity()));
                        dispatchedProducts.setTotalPrice(dispatchedProducts.getTotalPrice().subtract(clientsDispatchedProducts.getDeliveredProductPrice()));
                        dispatchedProductsRepository.save(dispatchedProducts);

                        Client client = clientRepository.findByClientID(clientsDispatchedProducts1.getClientID());
                        clientsTotals = clientsTotals.add(clientsDispatchedProducts.getDeliveredProductPrice());
                        client.setCumulativeAmountToPay(client.getCumulativeAmountToPay().add(clientsTotals));
                        clientRepository.save(client);
                    }

                }

                ClientsProductDispatch clientsProductDispatch = new ClientsProductDispatch();
                clientsProductDispatch.setProductDispatchCode(existingProductDispatch.getProductDispatchCode());
                clientsProductDispatch.setDispatchDate(LocalDateTime.now());
                clientsProductDispatch.setTotalDispatchPrice(total);
                clientsProductDispatchRepository.save(clientsProductDispatch);
                amount = amount.add(clientsProductDispatch.getTotalDispatchPrice());
            }

            existingProductDispatch.setTotalDispatchPrice(existingProductDispatch.getTotalDispatchPrice().subtract(amount));
            productDispatchRepository.save(existingProductDispatch);
            return responseService.formulateResponse(
                    existingProductDispatch.getProductDispatchCode(),
                    "Clients Dispatch done successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );

        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception dispatching clients products ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }

    public ResponseEntity getProductDispatchForClient() {
        try {
            // Fetch all dispatched products
            List<ClientsDispatchedProducts> clientsDispatchedProducts = clientDispatchedProductsRepository.findAll();

            // Initialize a map to hold the data grouped by productDispatchCode
            Map<String, Map<Integer, List<ClientsDispatchedProducts>>> groupedData = new HashMap<>();

            // Populate the map
            for (ClientsDispatchedProducts product : clientsDispatchedProducts) {
                // Fetch client details and set it in the product object
                Client client = clientRepository.findByClientID(product.getClientID());
                product.setClient(client); // Set client object in the product

                // Group by productDispatchCode
                groupedData
                        .computeIfAbsent(product.getProductDispatchCode(), k -> new HashMap<>()) // Create productDispatchCode entry if it doesn't exist
                        .computeIfAbsent(product.getClientID().intValue(), k -> new ArrayList<>())           // Create clientID entry if it doesn't exist
                        .add(product);                                                           // Add product to the client array
            }

            // Convert the map to the structure expected by the frontend
            List<Map<String, Object>> responseData = new ArrayList<>();
            for (String dispatchCode : groupedData.keySet()) {
                Map<String, Object> dispatchGroup = new HashMap<>();
                dispatchGroup.put("productDispatchCode", dispatchCode);

                List<Map<String, Object>> clientsList = new ArrayList<>();
                for (Integer clientId : groupedData.get(dispatchCode).keySet()) {
                    Map<String, Object> clientData = new HashMap<>();
                    Client client = groupedData.get(dispatchCode).get(clientId).get(0).getClient();  // Get client object from the first product

                    // Add entire client object to the response
                    clientData.put("client", client);

                    // Collect products for this client
                    List<Map<String, Object>> productsList = new ArrayList<>();
                    for (ClientsDispatchedProducts product : groupedData.get(dispatchCode).get(clientId)) {
                        Map<String, Object> productData = new HashMap<>();
                        productData.put("productID", product.getProductID());
                        productData.put("name", product.getName());
                        productData.put("unitPrice", product.getUnitPrice());
                        productData.put("deliveredQuantity", product.getDeliveredQuantity());
                        productData.put("deliveredProductPrice", product.getDeliveredProductPrice());
                        productData.put("saleDate", product.getSaleDate());
                        productsList.add(productData);
                    }
                    clientData.put("products", productsList);
                    clientsList.add(clientData);
                }
                dispatchGroup.put("clients", clientsList);
                responseData.add(dispatchGroup);
            }

            // Return the formatted response
            return responseService.formulateResponse(
                    responseData,
                    "Clients Dispatch fetched successfully",
                    HttpStatus.OK,
                    null,
                    true
            );

        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception fetching dispatch",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }

    public ResponseEntity createClientPayment(String token, PaymentDto paymentDto){
        try{
            System.out.println(paymentDto);
            Long userID = jwtService.extractuserID(token);
            Client client = clientRepository.findByClientID(paymentDto.getPayerID());
            client.setCumulativeAmountPaid(client.getCumulativeAmountPaid().add(paymentDto.getAmount()));
            clientRepository.save(client);
            return responseService.formulateResponse(
                    null,
                    "Payment Successful",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception making payment ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }

    public ResponseEntity createSupplierPayment(String token, PaymentDto paymentDto){
        try{
            Long userID = jwtService.extractuserID(token);
            Supplier supplier = supplierRepository.findBySupplierID(paymentDto.getPayerID());
            supplier.setCumulativeAmountPaid(supplier.getCumulativeAmountPaid().add(paymentDto.getAmount()));
            supplierRepository.save(supplier);
            return responseService.formulateResponse(
                    null,
                    "Payment Successful",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception making payment ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }

    public ResponseEntity returnSupplierCrates(String token, CratesDto cratesDto){
        try{
            Long userID = jwtService.extractuserID(token);
            Supplier supplier = supplierRepository.findBySupplierID(cratesDto.getSupplierID());
            supplier.setCumulativeCratesIn(supplier.getCumulativeCratesIn().add(cratesDto.getCrates()));
            supplierRepository.save(supplier);
            return responseService.formulateResponse(
                    null,
                    "Crates return Successful",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception returning crates ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }


}
