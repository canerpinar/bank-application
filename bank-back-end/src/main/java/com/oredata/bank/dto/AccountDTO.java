package com.oredata.bank.dto;

import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;

public class AccountDTO extends BaseDTO {

    @NotEmpty(message = "Number not be null or empty")
    private String number;
    @NotEmpty(message = "Name not be null or empty")
    private String name;

    private String username;
    private BigDecimal balance;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AccountDTO(String number, String name, String username, BigDecimal balance) {
        this.number = number;
        this.name = name;
        this.username = username;
        this.balance = balance;
    }

    public AccountDTO() {
    }
}
