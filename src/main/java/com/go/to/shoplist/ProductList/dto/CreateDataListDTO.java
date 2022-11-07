package com.go.to.shoplist.ProductList.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.go.to.shoplist.ProductList.entity.ProductEntry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDataListDTO {
    @JsonProperty("customer_id")
    private String customerId;

    @JsonProperty("product_entry_list")
    private List<ProductEntry> productEntryList;

}
