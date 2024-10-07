package com.nextuple.backend.repository;

import com.nextuple.backend.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product,String> {
    public boolean existsByName(String productName);
    public Product findByName(String productName);
}
