package com.oredata.bank.user.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity(name = "accountModel")
@Table(name = "accounts")
public class AccountModel extends BaseModel {

    @Column(name="number",unique = true)
    private String number;

    @Column(name = "accountName")
    private String name;

    @Column(name="balance")
    private BigDecimal balance;

    @ManyToOne
    private UserModel user;

    @Version
    private Long version;

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

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public AccountModel(String number, String name, BigDecimal balance) {
        this.number = number;
        this.name = name;
        this.balance = balance;
    }

    public AccountModel() {
    }
}
