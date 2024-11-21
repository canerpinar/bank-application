package com.oredata.bank.user.service;

import com.oredata.bank.user.model.UserModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.http.HttpCookie;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Component
public class UserDetailService implements UserDetailsService {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        TypedQuery<UserModel> userModel = entityManager.createQuery("SELECT user from user user where user.email =:email",UserModel.class);
        userModel.setParameter("email",email);
        try {
            UserModel user = userModel.getSingleResult();
            return new User(email,user.getPassword(), Arrays.asList(new SimpleGrantedAuthority("ADMIN")));
        }catch (NoResultException noResultException){
            throw new UsernameNotFoundException("Not found user");
        }
    }


}
