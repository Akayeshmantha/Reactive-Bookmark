package com.go.to.shoplist.ProductList.exception;

public class ProductListNotFoundException extends RuntimeException {
    public ProductListNotFoundException(String message) {
        super("ProductList id " + message + " Not found");
    }
}
