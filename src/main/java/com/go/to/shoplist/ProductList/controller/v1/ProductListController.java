package com.go.to.shoplist.ProductList.controller.v1;

import com.go.to.shoplist.ProductList.dao.ProductListRepository;
import com.go.to.shoplist.ProductList.entity.ProductList;
import com.go.to.shoplist.ProductList.dto.CreateDataListDTO;
import com.go.to.shoplist.ProductList.exception.ProductListNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product-list")
public class ProductListController {
    private final ProductListRepository productListRepository;

    @GetMapping("/{product-list-id}")
    public Mono<ProductList> findListById(@PathVariable("product-list-id") final String productListId) {
        log.debug("find product list by id in database...");
        return productListRepository.findById(UUID.fromString(productListId))
                .doOnSuccess(productList -> log.debug("fetched product list"+ productListId))
                .onErrorResume(e -> Mono.error(new RuntimeException(e)))
                .switchIfEmpty(Mono.error(new ProductListNotFoundException(productListId)));
    }

    @GetMapping()
    public Flux<ProductList> findListByCustomerId(@RequestParam(name = "customer-id") final String customerId) {
        Objects.requireNonNull(customerId);
        log.debug("find list by customer id in database...");
        return productListRepository.findByCustomerId(customerId)
                .doOnComplete(() -> log.debug("fetched product list"+ customerId))
                .onErrorResume(e -> Mono.error(new RuntimeException(e)))
                .switchIfEmpty(Flux.error(new ProductListNotFoundException(customerId)));
    }

    @PatchMapping("/{product-list-id}")
    public Mono<ProductList> updateProductList(@PathVariable("product-list-id") final String productListId, final @RequestBody CreateDataListDTO createDataListDTO) {
        Objects.requireNonNull(productListId);
        Objects.requireNonNull(createDataListDTO);
        return productListRepository
                .findById(UUID.fromString(productListId))
                .doOnSuccess(productList -> log.debug("fetched product list"+ productListId))
                .switchIfEmpty(Mono.error(new ProductListNotFoundException(productListId)))
                .flatMap(memberResult -> {
                    memberResult.setProductEntrySet(new HashSet<>(createDataListDTO.getProductEntryList()));
                    return productListRepository.save(memberResult);
                })
                .doOnSuccess(productList -> log.debug("Updated the product list"+ productListId))
                .onErrorResume(Mono::error);
    }

    @DeleteMapping("/{product-list-id}")
    public Mono<Void> deleteProductList(@PathVariable("product-list-id") String productListId) throws IllegalArgumentException{
        return productListRepository.deleteById(UUID.fromString(productListId))
                .doOnSuccess(productList -> log.debug("Deleted the product list"+ productListId))
                .onErrorResume(e -> Mono.error(new ProductListNotFoundException(productListId)));
    }

    @PostMapping()
    public Mono<ProductList> createProduct(final @RequestBody CreateDataListDTO createDataListDTO){
        Objects.requireNonNull(createDataListDTO);

        final ProductList productList = ProductList.builder()
                .productEntrySet(new HashSet<>(createDataListDTO.getProductEntryList()))
                .customerId(createDataListDTO.getCustomerId())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        return productListRepository.save(productList)
               .doOnSuccess(productL -> log.debug("Updated the product list"+ productL))
               .doOnError(throwable -> log.error("Creation error"))
               .onErrorResume(throwable -> Mono.error(new IllegalStateException("Cannot create product list")));
    }

}
