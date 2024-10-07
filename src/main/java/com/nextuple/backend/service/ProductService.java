package com.nextuple.backend.service;

import com.nextuple.backend.entity.Product;
import com.nextuple.backend.exception.ProductExistException;
import com.nextuple.backend.exception.ProductNotFoundException;
import com.nextuple.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<Product> findAllProduct(){
        return productRepository.findAll();
    }

    public Product findProductById(String pid){
        Optional<Product> product = productRepository.findById(pid);
        if(product.isPresent()){
            return product.get();
        }
//      If not found throw ProductNotFoundException
        throw new ProductNotFoundException("Product Not Found with id : " + pid);
    }

    public Product addProduct(Product product){
//      Check if it already exists. If exist throw Product Already exist exception
        String pname = product.getName();
        boolean productExist = productRepository.existsByName(pname);
//      Throw Product already exist exception
        if(productExist){
            Product p  = productRepository.findByName(pname);
            throw new ProductExistException("Product : " + p.getName() + " already exist with id : " + p.getPid() + ". Duplicate items cannot be added.");
        }else{
//      If not, call the product service and push the product in the repo
            productRepository.save(product);
            return product;

        }
    }

    public Product updateProductName(String productName,String newProductName){
        boolean productIsExist = productRepository.existsByName(productName);
        if(productIsExist){
            Product product = productRepository.findByName(productName);
            product.setName(newProductName);
            productRepository.save(product);
            return product;
        }else{
            throw new ProductNotFoundException("Product Not Found with name : " + productName + ". Please provide correct product name or add a product with the same name first to update it");
        }
    }

    public Product updateProductPrice(String productName,int newProductPrice){
        boolean productIsExist = productRepository.existsByName(productName);
        if(productIsExist){
            Product product = productRepository.findByName(productName);
            product.setPrice(newProductPrice);
            productRepository.save(product);
            return product;
        }else{
            throw new ProductNotFoundException("Product Not Found with name : " + productName + ". Please provide correct product name or add a product with the same name first to update it");
        }
    }

    public Product updateProductCategory(String productName,String newProductCategory){
        boolean productIsExist = productRepository.existsByName(productName);
        if(productIsExist){
            Product product = productRepository.findByName(productName);
            product.setCategory(newProductCategory);
            productRepository.save(product);
            return product;
        }else{
            throw new ProductNotFoundException("Product Not Found with name : " + productName + ". Please provide correct product name or add a product with the same name first to update it");
        }
    }


}
