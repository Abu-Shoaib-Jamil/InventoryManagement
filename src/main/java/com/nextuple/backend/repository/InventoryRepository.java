package com.nextuple.backend.repository;

import com.nextuple.backend.entity.Inventory;
import com.nextuple.backend.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends MongoRepository<Inventory,String> {
    public Inventory findByProduct(Product product);
    public boolean existsByProduct(Product product);
    public void deleteByProduct(Product product);
}
