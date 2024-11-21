package com.oredata.bank.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "user")
@Table(name = "users")
public class UserModel extends BaseModel {

    @Column(name = "username",nullable = false)
    private String username;
    @Column(name = "password",nullable = false)
    private String password;
    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user",orphanRemoval = true)
    private Set<AccountModel> accounts = new HashSet<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<AccountModel> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<AccountModel> accounts) {
        if(accounts != null) accounts.forEach(s->s.setUser(this));
        this.accounts = accounts;
    }

    public void addAccount(AccountModel accountModel){
        accountModel.setUser(this);
        accounts.add(accountModel);
    }




}
