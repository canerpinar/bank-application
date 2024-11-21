package com.oredata.bank.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TransferDTO {
    @NotEmpty(message = "From not be null")
    private String fromAccount;
    @NotEmpty(message = "To not be null")
    private String toAccount;

    @NotEmpty(message = "Amount not be null")
    @Positive(message = "Amount be positive")
    private BigDecimal amount;

    public TransferDTO(String fromAccount, String toAccount, BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public TransferDTO() {
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }
}
