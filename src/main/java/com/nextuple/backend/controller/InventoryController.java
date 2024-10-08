package com.nextuple.backend.controller;

import com.nextuple.backend.entity.Inventory;
import com.nextuple.backend.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getInventory(){
        List<Inventory> inventory = inventoryService.getInventories();
        return new ResponseEntity<>(inventory, HttpStatus.OK);
    }

    @GetMapping("/{productName}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable String productName){
        Inventory inventory = inventoryService.getInventory(productName);
        return new ResponseEntity<>(inventory,HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Inventory> addInventory(@RequestBody Map<String,Object> body){
        String productName = (String) body.get("productName");
        int qty = (int) body.get("qty");
        Inventory inventory = inventoryService.addProductInInventory(productName,qty);
        return new ResponseEntity<>(inventory,HttpStatus.CREATED);
    }

    @PutMapping("/add")
    public ResponseEntity<Inventory> addQuantity(@RequestBody Map<String,Object> body){
        String productName = (String) body.get("productName");
        int newQty = (int) body.get("newQty");
        Inventory inventory = inventoryService.increaseInventory(productName,newQty);
        return new ResponseEntity<>(inventory,HttpStatus.CREATED);
    }

    @PutMapping("/subtract")
    public ResponseEntity<Inventory> subtractQuantity(@RequestBody Map<String,Object> body){
        String productName = (String) body.get("productName");
        int newQty = (int) body.get("newQty");
        Inventory inventory = inventoryService.decreaseInventory(productName,newQty);
        return new ResponseEntity<>(inventory,HttpStatus.CREATED);
    }

    @DeleteMapping("/{productName}")
    public ResponseEntity<HttpStatus> deleteInventory(@PathVariable String productName){
        inventoryService.deleteInventory(productName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
