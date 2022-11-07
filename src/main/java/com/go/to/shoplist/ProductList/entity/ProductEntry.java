package com.go.to.shoplist.ProductList.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntry {
    @JsonProperty("product_number")
    private String product_number;

    @JsonProperty("quantity")
    private String quantity;
}
