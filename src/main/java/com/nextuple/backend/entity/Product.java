package com.nextuple.backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product")
public class Product {

//    Declaring all attributes of the product
    @Id
    private String pid;
    private String name;
    private int price;
    private String category;

//    Declaring constructors
    public Product(){}
    public Product(String pid,String name,int price,String category){
        this.pid = pid;
        this.name = name;
        this.price = price;
        this.category = category;
    }

//    Getters
    public String getPid(){
        return this.pid;
    }
    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
    public String getCategory() {
        return category;
    }

//    Setters

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

//    To String method
    @Override
    public String toString() {
        return "Product{" +
                "pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                '}';
    }
}
