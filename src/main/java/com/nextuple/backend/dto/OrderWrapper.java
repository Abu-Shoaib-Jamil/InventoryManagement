package com.nextuple.backend.dto;

import com.nextuple.backend.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OrderWrapper {

    @NonNull
    private Product product;
    @NonNull
    private int qty;
    @NonNull
    private String orderType;

}
