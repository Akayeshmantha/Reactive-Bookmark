package com.go.to.shoplist.ProductList.dao;

import com.go.to.shoplist.ProductList.entity.ProductList;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ProductListRepository extends R2dbcRepository<ProductList, UUID> {
    Flux<ProductList> findByCustomerId(String customerId);
}
