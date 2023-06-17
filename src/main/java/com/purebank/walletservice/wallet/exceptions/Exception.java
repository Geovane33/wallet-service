package com.purebank.walletservice.wallet.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class Exception extends RuntimeException {

    @Getter
    @Setter
    private HttpStatus status;

    public static class NotFound extends Exception {
        public NotFound(Long id) {
            super(String.format("User with id %d not found.", id), HttpStatus.NOT_FOUND);
        }
    }

    public static class NullPointerException extends Exception {
        public NullPointerException(String payload) {
            super(payload);
        }
    }


    public static class EmailAlreadyExists extends Exception {
        public EmailAlreadyExists() {
            super("E-mail already exists");
        }
    }

    public static class InvalidEmail extends Exception {
        public InvalidEmail(String email) {
            super(String.format("Invalid Email %s", email));
        }
    }

    public static class ErrorValidatingEmail extends Exception {
        public ErrorValidatingEmail() {
            super("Error validating email");
        }
    }

    public static class CPFAlreadyExists extends Exception {
        public CPFAlreadyExists() {
            super("CPF already exists");
        }
    }

    public static class JwtAuthenticationExpiredException extends Exception {
        public JwtAuthenticationExpiredException() {
            super("jwt authentication expired", HttpStatus.FORBIDDEN);
        }
    }

    public static class InvalidJwtAuthenticationException extends Exception {
        public InvalidJwtAuthenticationException() {
            super("invalid jwt authentication", HttpStatus.FORBIDDEN);
        }
    }

    public static class IncorretCombinationCPFAndBirthdate extends Exception {
        public IncorretCombinationCPFAndBirthdate() {
            super("Incorrect CPF and date of birth combination", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InvalidCPF extends Exception {
        public InvalidCPF() {
            super("Invalid cpf", HttpStatus.BAD_REQUEST);
        }
    }

    public static class ErrorSendingValidationEmail extends Exception {
        public ErrorSendingValidationEmail(String msg) {
            super("Error sending validation email", HttpStatus.OK);
        }
    }

    public Exception(String message, HttpStatus... status) {
        super(message);
        this.status = HttpStatus.OK;
        if (status.length > 0)
            this.status = status[0];
    }

    public Exception(HttpStatus status) {
        super(status.getReasonPhrase());
        this.status = status;
    }
}
