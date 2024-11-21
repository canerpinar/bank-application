package com.oredata.bank.utils;

import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * canerpinar
 * this interface marks as password hashing coming from dto,implementation is PasswordEncoderMapping
 */
@Qualifier
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface PasswordMapping {
}
