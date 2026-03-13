package io.github.yhugorocha.orders.exception;

public class NotProcessException extends ResourceNotFoundException{
    public NotProcessException(String message) {
        super(message);
    }
}