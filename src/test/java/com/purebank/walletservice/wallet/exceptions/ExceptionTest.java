package com.purebank.walletservice.wallet.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ExceptionTest {

    @Test
    @DisplayName("Teste para a exceção NotFound")
    void testNotFound() {
        String message = "Not found";
        HttpStatus status = HttpStatus.NOT_FOUND;

        Exception.NotFound exception = new Exception.NotFound(message);

        Assertions.assertEquals(message, exception.getMessage());
        Assertions.assertEquals(status, exception.getStatus());
    }

    @Test
    @DisplayName("Teste para a exceção NullPointerException")
    void testNullPointerException() {
        String message = "Null pointer exception";

        Exception.NullPointerException exception = new Exception.NullPointerException(message);

        Assertions.assertEquals(message, exception.getMessage());
        Assertions.assertEquals(HttpStatus.OK, exception.getStatus());
    }

    @Test
    @DisplayName("Teste para a exceção FailedToDeposit")
    void testFailedToDeposit() {
        String message = "Failed to deposit";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        Exception.FailedToDeposit exception = new Exception.FailedToDeposit(message);

        Assertions.assertEquals(message, exception.getMessage());
        Assertions.assertEquals(status, exception.getStatus());
    }

    @Test
    @DisplayName("Teste para a exceção FailedToWithdraw")
    void testFailedToWithdraw() {
        String message = "Failed to withdraw";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        Exception.FailedToWithdraw exception = new Exception.FailedToWithdraw(message);

        Assertions.assertEquals(message, exception.getMessage());
        Assertions.assertEquals(status, exception.getStatus());
    }

    @Test
    @DisplayName("Teste para a exceção InvalidAmount")
    void testInvalidAmount() {
        String message = "Invalid amount";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Exception.InvalidAmount exception = new Exception.InvalidAmount(message);

        Assertions.assertEquals(message, exception.getMessage());
        Assertions.assertEquals(status, exception.getStatus());
    }
}
