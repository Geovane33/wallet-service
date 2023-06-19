package com.purebank.walletservice.wallet.resource;

public enum MovimentStatus {
    PENDING("Pending"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    CANCELED("Canceled");

    private String label;

    MovimentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
