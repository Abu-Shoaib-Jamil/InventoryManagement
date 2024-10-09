package com.nextuple.backend.entity;

import com.nextuple.backend.dto.OrderWrapper;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "order")
public class Order {
    @Id
    private String id;
    private char type;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    private List<OrderWrapper> orders;


//    Constructors
    public Order(){}
    public Order(List<OrderWrapper> orders,char type){
        this.orders = orders;
        this.type = type;
    }

//    Getters
    public String getId(){
        return this.id;
    }
    public char getType() {
        return this.type;
    }
    public List<OrderWrapper> getOrders() {
        return this.orders;
    }
    public LocalDateTime getCreatedDate(){
        return this.createdDate;
    }
    public LocalDateTime getModifiedDate(){
        return this.modifiedDate;
    }

//    Setters
    public void setId(String id){
        this.id = id;
    }
    public void setOrders(List<OrderWrapper> orders) {
        this.orders = orders;
    }
    public void setType(char type) {
        this.type = type;
    }

//    To String Method
    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", orders=" + orders +
                '}';
    }
}
