package com.nextuple.backend.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
//    Declaring all attributes of the product
    @Id
    private String pid;
    private String name;
    private int price;
    private String category;

    public Product(String name) {
        this.name = name;
    }
}
