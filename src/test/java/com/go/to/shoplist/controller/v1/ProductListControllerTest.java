package com.go.to.shoplist.controller.v1;

import com.go.to.shoplist.ProductList.controller.v1.ProductListController;
import com.go.to.shoplist.ProductList.dao.ProductListRepository;
import com.go.to.shoplist.ProductList.dto.CreateDataListDTO;
import com.go.to.shoplist.ProductList.entity.ProductEntry;
import com.go.to.shoplist.ProductList.entity.ProductList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ProductListController.class)
public class ProductListControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private ProductListRepository productListRepository;

    private CreateDataListDTO createDataListDTO;
    private ProductEntry productEntry;
    private ProductList productList;
    private ProductList productList2;

    private final static String SAMPLE_UUID = "35520fe1-883b-4b9f-ae69-3be3fc54910d";
    private final static String SAMPLE_UUID2 = "ba923852-55ca-45a2-8241-353e7a0bd9c8";
    private final static String CUSTOMER_ID = "12345";

    @BeforeEach
    public void setUp() {
        productEntry = ProductEntry.builder()
                .product_number("product_1")
                .quantity("1")
                .build();
        createDataListDTO  = CreateDataListDTO.builder()
                .productEntryList(List.of(productEntry))
                .customerId(CUSTOMER_ID)
                .build();
        productList = ProductList.builder()
                .id(UUID.fromString(SAMPLE_UUID))
                .productEntrySet(Set.of(productEntry))
                .customerId(CUSTOMER_ID)
                .modifiedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        productList2 = ProductList.builder()
                .id(UUID.fromString(SAMPLE_UUID2))
                .productEntrySet(Set.of(productEntry))
                .customerId(CUSTOMER_ID)
                .modifiedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void shouldGetProductList() {
        when(productListRepository.findById(UUID.fromString(SAMPLE_UUID))).thenReturn(Mono.just(productList));
        webClient
                .get().uri("/product-list/"+ SAMPLE_UUID )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ProductList.class);
    }

    @Test
    public void getProductListWithNotExisting_ShouldThrowNotFoundException() {
        when(productListRepository.findById(UUID.fromString(SAMPLE_UUID))).thenReturn(Mono.empty());
        webClient
                .get().uri("/product-list/"+ SAMPLE_UUID )
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("error_status").isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void shouldGetAllProductListsForUser() {
        when(productListRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Flux.just(productList, productList2));
        webClient
                .get().uri("/product-list?customer-id="+CUSTOMER_ID)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(SAMPLE_UUID);
    }

    @Test
    public void getAllProductListsForUser_ShouldThrowNotFoundException() {
        when(productListRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Flux.empty());
        webClient
                .get().uri("/product-list?customer-id="+CUSTOMER_ID)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("error_status").isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void shouldPatchTheCurrentProductList() {
        when(productListRepository.findById(UUID.fromString(SAMPLE_UUID))).thenReturn(Mono.just(productList));
        when(productListRepository.save(productList)).thenReturn(Mono.just(productList));
        webClient
                .patch().uri("/product-list/"+ SAMPLE_UUID)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(createDataListDTO))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ProductList.class);
    }

    @Test
    public void patchTheCurrentProductList_ShouldThrowNotFound() {
        when(productListRepository.findById(UUID.fromString(SAMPLE_UUID))).thenReturn(Mono.empty());
        webClient
                .patch().uri("/product-list/"+ SAMPLE_UUID)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(createDataListDTO))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("error_status").isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void shouldDeleteProductListProductList() {
        when(productListRepository.deleteById(UUID.fromString(SAMPLE_UUID))).thenReturn(Mono.empty());
        webClient
                .delete().uri("/product-list/"+ SAMPLE_UUID)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void deleteNotExistingProductList_ShouldThrowNotFound() {
        when(productListRepository.deleteById(UUID.fromString(SAMPLE_UUID))).thenReturn(Mono.error(new RuntimeException(SAMPLE_UUID)));
        webClient
                .delete().uri("/product-list/"+ SAMPLE_UUID)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("error_status").isEqualTo(HttpStatus.NOT_FOUND.value());
    }


    @Test
    public void shouldCreateTheProductList() {
        when(productListRepository.save(any())).thenReturn(Mono.just(productList));
        webClient
                .post().uri("/product-list")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(createDataListDTO))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ProductList.class);
    }
}
