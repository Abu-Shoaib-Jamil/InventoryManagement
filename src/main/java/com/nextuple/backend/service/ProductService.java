package com.nextuple.backend.service;

import com.nextuple.backend.entity.Inventory;
import com.nextuple.backend.entity.Product;
import com.nextuple.backend.exception.ProductExistException;
import com.nextuple.backend.exception.ProductNotFoundException;
import com.nextuple.backend.repository.InventoryRepository;
import com.nextuple.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,InventoryRepository inventoryRepository){
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
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
        String pname = product.getName();
        boolean productExist = productRepository.existsByName(pname);
        if(productExist){
            Product p  = productRepository.findByName(pname);
            throw new ProductExistException("Product : " + p.getName() + " already exist with id : " + p.getPid() + ". Duplicate items cannot be added.");
        }else{
            productRepository.save(product);
            inventoryRepository.save(new Inventory(product,0));
            return product;
        }
    }

    public Product updateProduct(Product product){
        String productName = product.getName();
        boolean productIsExist = productRepository.existsByName(productName);
        if(productIsExist){
            Product oldProduct = productRepository.findByName(productName);
            if(product.getName()!=null && !product.getName().equals(oldProduct.getName())){
                oldProduct.setName(product.getName());
            }
            if(product.getCategory()!=null && !product.getCategory().equals(oldProduct.getCategory())){
                oldProduct.setCategory(product.getCategory());
            }
            if(product.getPrice()!=oldProduct.getPrice()){
                oldProduct.setPrice(product.getPrice());
            }
            productRepository.save(oldProduct);
            return oldProduct;
        }else{
            throw new ProductNotFoundException("Product Not Found with name : " + productName + ". Please provide correct product name or add a product with the same name first to update it");
        }
    }

    public Product deleteProduct(String productName){
        boolean productIsExist = productRepository.existsByName(productName);
        if(productIsExist){
            Product toBeDeletedProduct = productRepository.findByName(productName);
            productRepository.deleteById(toBeDeletedProduct.getPid());
            boolean inventoryIsExist = inventoryRepository.existsByProduct(toBeDeletedProduct);
            if(inventoryIsExist){
                inventoryRepository.deleteByProduct(toBeDeletedProduct);
            }
            return toBeDeletedProduct;
        }else{
            throw new ProductNotFoundException("Product Not Found with name : " + productName + ". Please provide correct product name or add a product with the same name first to delete it");
        }
    }
}
