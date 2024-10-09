package com.nextuple.backend.service;

import com.nextuple.backend.dto.OrderWrapper;
import com.nextuple.backend.entity.Inventory;
import com.nextuple.backend.entity.Order;
import com.nextuple.backend.entity.Product;
import com.nextuple.backend.exception.InsufficientStockException;
import com.nextuple.backend.exception.InventoryNotFoundException;
import com.nextuple.backend.exception.OrderNotFoundException;
import com.nextuple.backend.exception.ProductNotFoundException;
import com.nextuple.backend.repository.InventoryRepository;
import com.nextuple.backend.repository.OrderRepository;
import com.nextuple.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository,InventoryRepository inventoryRepository,ProductRepository productRepository){
        this.orderRepository = orderRepository;
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getOrders(){
        return orderRepository.findAll();
    }

    public Order getOrder(String orderId){
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent()){
            return order.get();
        }
        throw new OrderNotFoundException("Order with id : " + orderId + " not found");
    }

    public void placePurchaseOrder(List<OrderWrapper> orders) {
        for (OrderWrapper orderWrapper : orders) {
            Product purchaseProduct = orderWrapper.getProduct();
            String productName = purchaseProduct.getName();
            boolean productIsExist = productRepository.existsByName(productName);
            if (productIsExist) {
                Product product = productRepository.findByName(productName);
                orderWrapper.setProduct(product);
                boolean inventoryIsExist = inventoryRepository.existsByProduct(product);
                Inventory inventory;
                if (inventoryIsExist) {
                    inventory = inventoryRepository.findByProduct(product);
                    inventory.setQty(inventory.getQty() + orderWrapper.getQty());
                } else {
                    inventory = new Inventory(product, orderWrapper.getQty());
                }
                inventoryRepository.save(inventory);
            } else {
                productRepository.save(purchaseProduct);
                inventoryRepository.save(new Inventory(purchaseProduct, orderWrapper.getQty()));
            }
        }
        orderRepository.save(new Order(orders,'p'));
    }

    public void placeSellOrder(List<OrderWrapper> orders) {
        for (OrderWrapper orderWrapper : orders) {
            Product sellProduct = orderWrapper.getProduct();
            String productName = sellProduct.getName();
            boolean productIsExist = productRepository.existsByName(productName);
            if (productIsExist) {
                Product product = productRepository.findByName(productName);
                orderWrapper.setProduct(product);
                boolean inventoryIsExist = inventoryRepository.existsByProduct(product);
                if (inventoryIsExist) {
                    Inventory inventory = inventoryRepository.findByProduct(product);
                    if (inventory.getQty() >= orderWrapper.getQty()) {
                        inventory.setQty(inventory.getQty() - orderWrapper.getQty());
                        inventoryRepository.save(inventory);
                    } else {
                        throw new InsufficientStockException("Insufficient stock for product: " + productName +
                                ". Available stock: " + inventory.getQty() + ", Requested quantity: " + orderWrapper.getQty());
                    }
                } else {
                    throw new InventoryNotFoundException("Product: " + productName + " is not available in inventory.");
                }
            } else {
                throw new ProductNotFoundException("Product: " + productName + " not found.");
            }
        }
        orderRepository.save(new Order(orders, 's'));
    }


}
