package com.purebank.walletservice.wallet.enums;

public enum ActivityType {
    DEPOSIT("Deposit"),
    WITHDRAW("withdraw"),
    TRANSFER("Transfer"),
    PAYMENT("Payment");

    private final String displayName;

    ActivityType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
