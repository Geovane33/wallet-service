package com.purebank.walletservice.wallet.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class ApiErrorMessageTest {

    @Test
    @DisplayName("Teste para a classe ApiErrorMessage - Testando atributos")
    void testApiErrorMessage() {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> errors = Arrays.asList("Error 1", "Error 2");
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(status, errors);

        Assertions.assertEquals(status, apiErrorMessage.getStatus());
        Assertions.assertEquals(errors, apiErrorMessage.getErrors());

        String error = "Error";
        apiErrorMessage = new ApiErrorMessage(status, error);

        Assertions.assertEquals(status, apiErrorMessage.getStatus());
        Assertions.assertEquals(Arrays.asList(error), apiErrorMessage.getErrors());

        HttpStatus newStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        List<String> newErrors = Arrays.asList("New Error 1", "New Error 2");

        apiErrorMessage.setStatus(newStatus);
        apiErrorMessage.setErrors(newErrors);

        Assertions.assertEquals(newStatus, apiErrorMessage.getStatus());
        Assertions.assertEquals(newErrors, apiErrorMessage.getErrors());
    }
}
