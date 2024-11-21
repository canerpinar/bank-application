package com.oredata.bank.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public class UserDTO extends BaseDTO {
    @NotEmpty(message = "Username not be null")
    private String username;
    @NotEmpty(message = "Password not be null")
    private String password;
    @Email(message = "Email not be null")
    private String email;
    private Set<AccountDTO> accountDTOS;

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

    public Set<AccountDTO> getAccountDTOS() {
        return accountDTOS;
    }

    public void setAccountDTOS(Set<AccountDTO> accountDTOS) {
        this.accountDTOS = accountDTOS;
    }

    public UserDTO(String username, String password, String email, Set<AccountDTO> accountDTOS) {
        super();
        this.username = username;
        this.password = password;
        this.email = email;
        this.accountDTOS = accountDTOS;
    }

    public UserDTO() {
    }
}
