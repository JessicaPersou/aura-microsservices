package com.postech.auramsorder.config.exception;

public class OrderFailSerealizationItems extends RuntimeException {
    public OrderFailSerealizationItems(String message, String details) {
        super(message);
    }
}
