package com.jp.productcatalogservice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductData {

    private Long productId;
    private String productName;
    private String updateType;
    private String description;

    @Override
    public String toString() {
        return "ProductData{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", updateType='" + updateType + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
