package com.nextuple.backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inventory")
public class Inventory {

//  Attributes
    @Id
    private String id;
    @DBRef
    private Product product;
    private int qty;

//  Constructors
    public Inventory(){}
    public Inventory(String id,Product product,int qty){
        this.id = id;
        this.product = product;
        this.qty = qty;
    }
    public Inventory(Product product,int qty){
        this.product = product;
        this.qty = qty;
    }

//  Getters
    public String getId(){
        return this.id;
    }
    public Product getProduct() {
        return this.product;
    }
    public int getQty(){
        return this.qty;
    }

//  Setters
    public void setId(String id){
        this.id = id;
    }
    public void setProduct(Product product) {
        this.product= product;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }

//  To String
    @Override
    public String toString() {
        return "Inventory{" +
                "id='" + id + '\'' +
                ", product=" + product +
                ", qty=" + qty +
                '}';
    }
}
