package com.nextuple.backend.service;

import com.nextuple.backend.entity.Inventory;
import com.nextuple.backend.entity.Product;
import com.nextuple.backend.exception.InvalidInventoryQuantityException;
import com.nextuple.backend.exception.InventoryNotFoundException;
import com.nextuple.backend.exception.ProductExistException;
import com.nextuple.backend.exception.ProductNotFoundException;
import com.nextuple.backend.repository.InventoryRepository;
import com.nextuple.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository,ProductRepository productRepository){
        this.inventoryRepository = inventoryRepository;
        this.productRepository =  productRepository;
    }

    public List<Inventory> getInventories(){
        return inventoryRepository.findAll();
    }

    public Inventory getInventory(String productName){
        boolean productIsExist = productRepository.existsByName(productName);
        if(productIsExist){
            Product product = productRepository.findByName(productName);
            boolean inventoryIsExist = inventoryRepository.existsByProduct(product);
            if(!inventoryIsExist){
                throw new InventoryNotFoundException("Inventory for product : " + product.getName() + " not found");
            }else{
                return inventoryRepository.findByProduct(product);
            }
        }else{
            throw new ProductNotFoundException("Product with name : " + productName + " not found in the available product list. Please add the product to get it in to inventory");
        }
    }

    public Inventory addProductInInventory(String productName,int qty){
        boolean productIsExist = productRepository.existsByName(productName);
        if(!productIsExist){
            throw new ProductNotFoundException("Product with name : " + productName + " not found in the available product list. Please add the product to get it in to inventory");
        }else{
            Product product = productRepository.findByName(productName);
            boolean inventoryIsExist = inventoryRepository.existsByProduct(product);
            if(inventoryIsExist) {
                Inventory inventory = inventoryRepository.findByProduct(product);
                throw new ProductExistException("Product : " + product.getName() + " already exist in the inventory with inventory id : " + inventory.getId() + " and having quantity : " + inventory.getQty());
            }else{
                Inventory inventory = new Inventory(product,qty);
                inventoryRepository.save(inventory);
                return inventory;
            }
        }
    }

    public Inventory increaseInventory(String productName,int newQty){
        boolean productIsExist = productRepository.existsByName(productName);
        if(!productIsExist){
            throw new ProductNotFoundException("Product with name : " + productName + " not found in the available product list");
        }else{
            Product product = productRepository.findByName(productName);
            boolean inventoryIsExist = inventoryRepository.existsByProduct(product);
            Inventory inventory;
            if(!inventoryIsExist){
                inventory = new Inventory(product,newQty);
                inventoryRepository.save(inventory);
                return inventory;
            }else{
                inventory = inventoryRepository.findByProduct(product);
                inventory.setQty(inventory.getQty()+newQty);
                inventoryRepository.save(inventory);
            }
            return inventory;
        }
    }

    public Inventory decreaseInventory(String productName,int newQty){
        boolean productIsExist = productRepository.existsByName(productName);
        if(!productIsExist){
            throw new ProductNotFoundException("Product with name : " + productName + " not found in the available product list");
        }else{
            Product product = productRepository.findByName(productName);
            boolean inventoryIsExist = inventoryRepository.existsByProduct(product);
            if(!inventoryIsExist){
                throw new InventoryNotFoundException("Inventory for product : " + product.getName() + " not found");
            }else{
                Inventory inventory = inventoryRepository.findByProduct(product);
                int qty = inventory.getQty();
                if(qty-newQty<0){
                    throw new InvalidInventoryQuantityException(qty + " - " + newQty + " = " + (qty-newQty) + ".Product Inventory cannot be negative");
                }else{
                    inventory.setQty(inventory.getQty()-newQty);
                    inventoryRepository.save(inventory);
                    return inventory;
                }
            }
        }
    }

    public void deleteInventory(String productName){
        boolean productIsExist = productRepository.existsByName(productName);
        if(!productIsExist){
            throw new ProductNotFoundException("Product with name : " + productName + " not found in the available product list");
        }else{
            Product product = productRepository.findByName(productName);
            boolean inventoryIsExist = inventoryRepository.existsByProduct(product);
            if(!inventoryIsExist) {
                throw new InventoryNotFoundException("Inventory for product : " + product.getName() + " not found");
            }else{
                inventoryRepository.deleteByProduct(product);
            }
        }
    }

}
