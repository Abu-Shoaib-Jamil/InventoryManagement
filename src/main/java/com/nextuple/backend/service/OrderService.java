package com.nextuple.backend.service;

import com.nextuple.backend.dto.OrderWrapper;
import com.nextuple.backend.entity.Inventory;
import com.nextuple.backend.entity.Order;
import com.nextuple.backend.entity.Product;
import com.nextuple.backend.exception.*;
import com.nextuple.backend.repository.InventoryRepository;
import com.nextuple.backend.repository.OrderRepository;
import com.nextuple.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void placeOrder(List<OrderWrapper> orders) {
        for (OrderWrapper orderWrapper : orders) {
            String orderType = orderWrapper.getOrderType();
            String productName = orderWrapper.getProduct().getName();
            int orderQty = orderWrapper.getQty();

            // Validate the order type early
            validateOrderType(orderType, productName);

            // Fetch or create the product (for purchase orders)
            Product product = getProductForOrder(orderType, productName);
            orderWrapper.setProduct(product);

            // Process the order by type (purchase or sale)
            processOrder(orderType, product, orderQty);
        }

        // Save the order after processing all items
        orderRepository.save(new Order(orders));
    }

    // Validate the order type (purchase or sale)
    private void validateOrderType(String orderType, String productName) {
        if (!orderType.equals("purchase") && !orderType.equals("sale")) {
            throw new InvalidOrderTypeException(
                    String.format("Invalid order type '%s' for product '%s'. Supported types are 'purchase' and 'sale'.",
                            orderType, productName)
            );
        }
    }

    // Fetch or create the product for purchase orders
    private Product getProductForOrder(String orderType, String productName) {
        boolean productIsExist = productRepository.existsByName(productName);
        Product product;
        if(productIsExist){
            product =  productRepository.findByName(productName);
        }else{
            if(orderType.equals("purchase")){
                product = new Product(productName);
                productRepository.save(product);
            }else{
                throw new ProductNotFoundException("Product : "+ productName +" not found in the available product list.");
            }
        }
        return product;
    }

    // Process the order (purchase or sale)
    private void processOrder(String orderType, Product product, int orderQty) {
        Inventory inventory = inventoryRepository.findByProduct(product);

        if (orderType.equals("purchase")) {
            processPurchaseOrder(product, inventory, orderQty);
        } else {
            processSaleOrder(product, inventory, orderQty);
        }
    }

    // Handle purchase orders
    private void processPurchaseOrder(Product product, Inventory inventory, int orderQty) {
        if (inventory == null) {
            // Create new inventory if none exists
            inventory = new Inventory(product, orderQty);
        } else {
            // Add to existing inventory
            inventory.setQty(inventory.getQty() + orderQty);
        }

        inventoryRepository.save(inventory); // Save updated inventory
    }

    // Handle sale orders
    private void processSaleOrder(Product product, Inventory inventory, int orderQty) {
        if (inventory == null) {
            throw new InventoryNotFoundException("No inventory found for product: " + product.getName());
        }

        if (inventory.getQty() < orderQty) {
            throw new InsufficientStockException(
                    String.format("Insufficient stock for product '%s'. Available: %d, Requested: %d",
                            product.getName(), inventory.getQty(), orderQty)
            );
        }

        // Deduct from inventory
        inventory.setQty(inventory.getQty() - orderQty);
        inventoryRepository.save(inventory); // Save updated inventory
    }



//    @Transactional
//    public void placeOrder(List<OrderWrapper> orders) {
//        for (OrderWrapper orderWrapper : orders) {
//            Product purchaseProduct = orderWrapper.getProduct();
//            int orderQty = orderWrapper.getQty();
//            String orderType = orderWrapper.getOrderType();
//            String productName = purchaseProduct.getName();
//
//            //Check for the valid order type
//            if(!orderType.equals("purchase") && !orderType.equals("sale")){
//                throw new InvalidOrderTypeException("Invalid Order type '" + orderType
//                        + "' for product '" + productName
//                        + "'. Order supports only 'purchase' and 'sale' order");
//            }
//
//            // Check if product exists, else throw exception
//            if (!productRepository.existsByName(productName)) {
//                throw new ProductNotFoundException("Product: " + productName + " not found in the product list.");
//            }
//
//            // Fetch the product
//            Product product = productRepository.findByName(productName);
//            orderWrapper.setProduct(product);
//
//            // Check if inventory exists for the product
//            Inventory inventory = inventoryRepository.existsByProduct(product)
//                    ? inventoryRepository.findByProduct(product)
//                    : null;
//
//            if (inventory == null && orderType.equals("sale")) {
//                // No inventory for sale orders
//                throw new InventoryNotFoundException("Product: " + productName + " is not available in inventory.");
//            }
//
//            // Process order based on order type
//            if (orderType.equals("purchase")) {
//                if (inventory == null) {
//                    // Create new inventory if not exists
//                    inventory = new Inventory(product, orderQty);
//                } else {
//                    // Add to existing inventory
//                    inventory.setQty(inventory.getQty() + orderQty);
//                }
//            } else {
//                // Check stock availability
//                if (inventory.getQty() < orderQty) {
//                    throw new InsufficientStockException(
//                            "Insufficient stock for product: " + productName +
//                                    ". Available: " + inventory.getQty() + ", Requested: " + orderQty
//                    );
//                }
//                // Deduct from inventory
//                inventory.setQty(inventory.getQty() - orderQty);
//            }
//
//            // Save inventory changes
//            inventoryRepository.save(inventory);
//        }
//
//        // Save the order after processing all items
//        orderRepository.save(new Order(orders));
//    }


//    public void placeOrder(List<OrderWrapper> orders){
//        for (OrderWrapper orderWrapper : orders) {
//            Product purchaseProduct = orderWrapper.getProduct();
//            int orderQty = orderWrapper.getQty();
//            String orderType = orderWrapper.getOrderType();
//            String productName = purchaseProduct.getName();
//            boolean productIsExist = productRepository.existsByName(productName);
//            if (productIsExist) {
//                Product product = productRepository.findByName(productName);
//                orderWrapper.setProduct(product);
//                boolean inventoryIsExist = inventoryRepository.existsByProduct(product);
//                Inventory inventory;
//                if (inventoryIsExist) {
//                    inventory = inventoryRepository.findByProduct(product);
//                    if(orderType.equals("purchase")){
//                        inventory.setQty(inventory.getQty() + orderWrapper.getQty());
//                        inventoryRepository.save(inventory);
//                    }else{
//                        if (inventory.getQty() >= orderWrapper.getQty()) {
//                            inventory.setQty(inventory.getQty() - orderWrapper.getQty());
//                            inventoryRepository.save(inventory);
//                        } else {
//                            throw new InsufficientStockException("Insufficient stock for product: " + productName +
//                                    ". Available stock: " + inventory.getQty() + ", Requested quantity: " + orderWrapper.getQty());
//                        }
//                    }
//                } else {
//                    if(orderType.equals("purchase")){
//                        inventory = new Inventory(product, orderWrapper.getQty());
//                        inventoryRepository.save(inventory);
//                    }else{
//                        throw new InventoryNotFoundException("Product: " + productName + " is not available in inventory.");
//                    }
//                }
//            } else {
//                throw new ProductNotFoundException("Product: " + productName + " not found in the available product list");
//            }
//        }
//        orderRepository.save(new Order(orders));
//    }

//    public void placePurchaseOrder(List<OrderWrapper> orders) {
//        for (OrderWrapper orderWrapper : orders) {
//            Product purchaseProduct = orderWrapper.getProduct();
//            String productName = purchaseProduct.getName();
//            boolean productIsExist = productRepository.existsByName(productName);
//            if (productIsExist) {
//                Product product = productRepository.findByName(productName);
//                orderWrapper.setProduct(product);
//                boolean inventoryIsExist = inventoryRepository.existsByProduct(product);
//                Inventory inventory;
//                if (inventoryIsExist) {
//                    inventory = inventoryRepository.findByProduct(product);
//                    inventory.setQty(inventory.getQty() + orderWrapper.getQty());
//                } else {
//                    inventory = new Inventory(product, orderWrapper.getQty());
//                }
//                inventoryRepository.save(inventory);
//            } else {
//                productRepository.save(purchaseProduct);
//                inventoryRepository.save(new Inventory(purchaseProduct, orderWrapper.getQty()));
//            }
//        }
//        orderRepository.save(new Order('p', orders));
//    }
//
//    public void placeSellOrder(List<OrderWrapper> orders) {
//        for (OrderWrapper orderWrapper : orders) {
//            Product sellProduct = orderWrapper.getProduct();
//            String productName = sellProduct.getName();
//            boolean productIsExist = productRepository.existsByName(productName);
//            if (productIsExist) {
//                Product product = productRepository.findByName(productName);
//                orderWrapper.setProduct(product);
//                boolean inventoryIsExist = inventoryRepository.existsByProduct(product);
//                if (inventoryIsExist) {
//                    Inventory inventory = inventoryRepository.findByProduct(product);
//                    if (inventory.getQty() >= orderWrapper.getQty()) {
//                        inventory.setQty(inventory.getQty() - orderWrapper.getQty());
//                        inventoryRepository.save(inventory);
//                    } else {
//                        throw new InsufficientStockException("Insufficient stock for product: " + productName +
//                                ". Available stock: " + inventory.getQty() + ", Requested quantity: " + orderWrapper.getQty());
//                    }
//                } else {
//                    throw new InventoryNotFoundException("Product: " + productName + " is not available in inventory.");
//                }
//            } else {
//                throw new ProductNotFoundException("Product: " + productName + " not found.");
//            }
//        }
//        orderRepository.save(new Order('s', orders));
//    }


}
