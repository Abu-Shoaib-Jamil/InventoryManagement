package com.nextuple.backend.service;

import com.nextuple.backend.entity.Inventory;
import com.nextuple.backend.entity.Product;
import com.nextuple.backend.exception.*;
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


    public Inventory updateInventory(String productName, int newQty, String operationType) {
        // Check if the product exists
        if (!productRepository.existsByName(productName)) {
            throw new ProductNotFoundException("Product with name: " + productName + " not found in the product list");
        }
        Product product = productRepository.findByName(productName);
        // Validate operationType
        if (!operationType.equals("add") && !operationType.equals("remove")) {
            throw new InvalidInventoryOperationException("Invalid Inventory operation '" + operationType + "'.Inventory supports only 'add' or 'remove' operations");
        }
        boolean inventoryExists = inventoryRepository.existsByProduct(product);
        Inventory inventory;
        if (!inventoryExists) {
            if (operationType.equals("add")) {
                // Create new inventory if not found and operation is 'add'
                inventory = new Inventory(product, newQty);
                inventoryRepository.save(inventory);
                return inventory;
            } else {
                // If trying to remove and inventory doesn't exist
                throw new InventoryNotFoundException("Inventory for product: " + product.getName() + " not found");
            }
        }
        // Fetch existing inventory
        inventory = inventoryRepository.findByProduct(product);
        if (operationType.equals("add")) {
            inventory.setQty(inventory.getQty() + newQty);
        } else {
            int currentQty = inventory.getQty();
            if (currentQty - newQty < 0) {
                throw new InvalidInventoryQuantityException(
                        currentQty + " - " + newQty + " = " + (currentQty - newQty) + ". Inventory cannot be negative"
                );
            }
            inventory.setQty(currentQty - newQty);
        }
        inventoryRepository.save(inventory);
        return inventory;
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
