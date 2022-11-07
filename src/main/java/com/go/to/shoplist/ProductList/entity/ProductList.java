package com.go.to.shoplist.ProductList.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "product_list")
public class ProductList {
    @Id
    @Column("id")
    @Generated
    private UUID id;

    @Column("customer_id")
    private String customerId;

    @Column("product_entry")
    private Set<ProductEntry> productEntrySet;

    @Column("created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column("updated_at")
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
