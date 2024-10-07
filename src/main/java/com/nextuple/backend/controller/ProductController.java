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

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        Product savedProduct = productService.addProduct(product);
        return new ResponseEntity<>(savedProduct,HttpStatus.CREATED);
    }

    @PutMapping("/name")
    public ResponseEntity<Product> updateProductName(@RequestBody Map<String,String> body){
        String productName = body.get("productName");
        String newProductName = body.get("newProductName");
        Product product = productService.updateProductName(productName,newProductName);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    @PutMapping("/price")
    public ResponseEntity<Product> updateProductPrice(@RequestBody Map<String,Object> body){
        String productName = (String) body.get("productName");
        int newProductPrice = (int) body.get("newProductPrice");
        Product product = productService.updateProductPrice(productName,newProductPrice);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    @PutMapping("/category")
    public ResponseEntity<Product> updateProductCategory(@RequestBody Map<String,String> body){
        String productName = body.get("productName");
        String newProductCategory = body.get("newProductCategory");
        Product product = productService.updateProductCategory(productName,newProductCategory);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }




}
