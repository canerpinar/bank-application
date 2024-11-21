package com.oredata.bank.appContext;

import com.oredata.bank.dto.GenericResponse;
import com.oredata.bank.exceptions.NotEnoughException;
import jakarta.persistence.NoResultException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResultException.class)
    public GenericResponse sendNoResultException(NoResultException noResultException){
        return new GenericResponse(null,null,null,404);
    }
    @ExceptionHandler(NotEnoughException.class)
    public GenericResponse sendNoResultException(NotEnoughException notEnoughException){
        return new GenericResponse("You dont have enough balance",null,null,403);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public GenericResponse sendNoUserFoundException(UsernameNotFoundException usernameNotFoundException){
        return new GenericResponse("Username , password may wrong",null,null,400);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public GenericResponse sendNoUserFoundException(BadCredentialsException usernameNotFoundException){
        return new GenericResponse("Username , password may wrong",null,null,400);
    }
}
