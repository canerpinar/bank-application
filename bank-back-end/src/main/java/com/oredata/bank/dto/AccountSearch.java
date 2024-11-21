package com.oredata.bank.dto;

public class AccountSearch {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AccountSearch(String value) {
        this.value = value;
    }

    public AccountSearch() {
    }
}
