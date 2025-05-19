package com.postech.auramsstock.config.exception;

public class StockIsEmptyException extends RuntimeException {
    public StockIsEmptyException(String message) {
        super(message);
    }
}
