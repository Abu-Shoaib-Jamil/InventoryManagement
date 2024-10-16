package com.nextuple.backend.controller;


import com.nextuple.backend.entity.Product;
import com.nextuple.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<List<Product>> getProducts(){
        List<Product> products = productService.findAllProduct();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{pid}")
    public ResponseEntity<Product> getProductById(@PathVariable String pid){
        Product product = productService.findProductById(pid);
        return new ResponseEntity<>(product,HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        Product savedProduct = productService.addProduct(product);
        return new ResponseEntity<>(savedProduct,HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@RequestBody Product product){
        Product p = productService.updateProduct(product);
        return new ResponseEntity<>(p,HttpStatus.CREATED);
    }

    @DeleteMapping("{productName}")
    public ResponseEntity<Product> deleteProductByName(@PathVariable String productName){
        Product product = productService.deleteProduct(productName);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

}
