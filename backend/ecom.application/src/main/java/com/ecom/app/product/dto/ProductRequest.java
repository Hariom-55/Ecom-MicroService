package com.ecom.app.product.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest
{
    private String name;
    private String Description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String category;
    private String imageUrl;
}
