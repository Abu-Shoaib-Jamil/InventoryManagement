package com.nextuple.backend.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inventory")
@Data
@RequiredArgsConstructor
public class Inventory {
//  Attributes
    @Id
    private String id;
    @DBRef
    @NonNull
    private Product product;
    @NonNull
    private int qty;

//    public Inventory(Product product,int qty){
//        this.product = product;
//        this.qty = qty;
//    }
}
