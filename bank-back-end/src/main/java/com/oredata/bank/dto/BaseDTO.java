package com.oredata.bank.dto;

public class BaseDTO {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BaseDTO(String id) {
        this.id = id;
    }

    public BaseDTO() {
    }
}
