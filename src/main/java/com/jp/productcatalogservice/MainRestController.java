package com.jp.productcatalogservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class MainRestController {


    @Autowired
    ProductRepository productRepo;

    @Autowired
    CustomerService customerService;

    @Autowired
    Producer producer;

    private static final Logger log = LoggerFactory.getLogger(MainRestController.class);

    @GetMapping("/test")
    public ResponseEntity<?> testProductService() {
        return ResponseEntity.ok("Product Service running fine");
    }

    @PostMapping("/products/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product,@RequestHeader("Authorization") String token) throws JsonProcessingException {
        log.info("Received request to add product : {}", product);

        if(customerService.validateToken(token)){
            productRepo.save(product);
            producer.publishProductData(product.getProductId(),product.getName(),"NEW","New product added");
            return ResponseEntity.ok("New Product Added.");
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/products/view/{productId}")
    public ResponseEntity<?> viewProduct(@PathVariable Long productId, @RequestHeader("Authorization") String token){
        log.info("Received request to view product with product id : {} ",productId);

        if(customerService.validateToken(token)){
            Optional<Product> productObj = productRepo.findById(productId);

            if(productObj.isPresent()){
                return ResponseEntity.ok(productObj);
            }else{
                return  ResponseEntity.ok("No product found with given id : "+productId);
            }
        } else {
            return ResponseEntity.status(401).build();
        }


    }

    @PostMapping("/products/update")
    public ResponseEntity<?> updateProductDetails(@RequestBody Product productParam, @RequestHeader("Authorization") String token) throws JsonProcessingException {
        log.info("Received request to update product with data : {} ",productParam);

        if(customerService.validateToken(token)){
            Optional<Product> productObj = productRepo.findById(productParam.getProductId());
            if(productObj.isPresent()){
                productRepo.save(productParam);
                producer.publishProductData(productParam.getProductId(),productParam.getName(),"UPDATE","Product data updated.");
                return ResponseEntity.ok("Product data updated successfully.");
            }else{
                return  ResponseEntity.ok("No product found with given id : "+(productParam.getProductId()));
            }
        } else {
            return ResponseEntity.status(401).build();
        }


    }

    @PostMapping("/products/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId, @RequestHeader("Authorization") String token) throws JsonProcessingException {
        log.info("Received request to delete product with product id : {} ",productId);

        if(customerService.validateToken(token)){
            Optional<Product> productObj = productRepo.findById(productId);
            if(productObj.isPresent()){
                productRepo.deleteById(productId);
                producer.publishProductData(productId,"","DELETE","Product data deleted.");
                return ResponseEntity.ok("Product data deleted successfully.");
            }else{
                return  ResponseEntity.ok("No product found with given id : "+productId);
            }
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("products/viewAll")
    public ResponseEntity<?> viewAllProducts(){
        log.info("Received request to view all products");
        return ResponseEntity.ok(productRepo.findAll());
    }


}
