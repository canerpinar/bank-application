package com.oredata.bank.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderMapping {
    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderMapping(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PasswordMapping
    public String encode(String value) {
        return passwordEncoder.encode(value);
    }
}
