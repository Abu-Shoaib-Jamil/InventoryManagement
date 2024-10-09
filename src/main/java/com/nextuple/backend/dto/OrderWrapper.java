package com.nextuple.backend.dto;

import com.nextuple.backend.entity.Product;

public class OrderWrapper {

    private Product product;
    private int qty;

//    Constructors
    public OrderWrapper(){}
    public OrderWrapper(Product product,int qty){
        this.product = product;
        this.qty = qty;
    }

//    Getters
    public Product getProduct(){
        return this.product;
    }
    public int getQty(){
        return this.qty;
    }

//    Setters
    public void setQty(int qty) {
        this.qty = qty;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

//    To String Method

    @Override
    public String toString() {
        return "OrderWrapper{" +
                "product=" + this.product +
                ", qty=" + this.qty +
                '}';
    }
}
