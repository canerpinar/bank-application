package com.oredata.bank.user.controller;

import com.oredata.bank.dto.GenericResponse;
import com.oredata.bank.dto.LoginDTO;
import com.oredata.bank.dto.UserDTO;
import com.oredata.bank.user.service.JwtService;
import com.oredata.bank.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/user")
public class IndexController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public IndexController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> saveUser(@Valid @RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.save(userDTO));
    }

    /**
     * we must create cookie, actually it must encrypted in prod environment, this work can be not necessary
     * @param loginDTO
     * @param cookieValue
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO, @CookieValue(value = "cookieValue",required = false) String cookieValue){
        System.out.println(cookieValue);
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword()));
        String jwt = jwtService.generateToken(authentication.getName());
        UserDTO userDTO = userService.getUserByEmail(loginDTO.getEmail());
        HttpHeaders httpHeaders = new HttpHeaders();
        //Token is store in cookie and make http only for that cant read in javascript
        ResponseCookie cookie = ResponseCookie.from("cookieValue", jwt)
                .maxAge(Duration.ofSeconds(360L))
                .httpOnly(true)
                .path("/")
                .build();
        //may be we dont need authorization header
        httpHeaders.add("Authorization","Bearer "+jwt);
        httpHeaders.add(HttpHeaders.SET_COOKIE,cookie.toString());
        return new ResponseEntity(userDTO,httpHeaders,200);
    }


    /**
     * if page is refresh, we can use this method
     * @param value
     * @return
     */
    @GetMapping("/tokenControl")
    public GenericResponse tokenControl(@CookieValue(value = "cookieValue",required = false) String value){
        System.out.println(value);
        if(value != null){
            if(jwtService.validateToken(value)){
                String user = jwtService.extractEmail(value);
                return new GenericResponse("Success",userService.getUserByUsername(user),null,200);
            }else return new GenericResponse("Failed",null,null,401);

        }else return new GenericResponse("Failed",null,null,401);

    }
}
