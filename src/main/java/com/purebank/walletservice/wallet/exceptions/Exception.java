package com.purebank.walletservice.wallet.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class Exception extends RuntimeException {

    @Getter
    @Setter
    private HttpStatus status;

    public static class NotFound extends Exception {
        public NotFound(String message) {
            super(message, HttpStatus.NOT_FOUND);
        }
    }

    public static class BadRequest extends Exception {
        public BadRequest(String message) {
            super(message, HttpStatus.NOT_FOUND);
        }
    }

    public static class NullPointerException extends Exception {
        public NullPointerException(String message) {
            super(message);
        }
    }

    public static class FailedToDeposit extends Exception {
        public FailedToDeposit(String message) {
            super(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static class FailedToWithdraw extends Exception {
        public FailedToWithdraw(String message) {
            super(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static class InvalidAmount extends Exception {
        public InvalidAmount(String message) {
            super(message, HttpStatus.BAD_REQUEST);
        }
    }

    public Exception(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public Exception(String message) {
        super(message);
        this.status = HttpStatus.OK;
    }

    public Exception(HttpStatus status) {
        super(status.getReasonPhrase());
        this.status = status;
    }
}
