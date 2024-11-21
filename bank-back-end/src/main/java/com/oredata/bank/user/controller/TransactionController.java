package com.oredata.bank.user.controller;

import com.oredata.bank.dto.AccountDTO;
import com.oredata.bank.dto.GenericResponse;
import com.oredata.bank.dto.TransferDTO;
import com.oredata.bank.dto.UserDTO;
import com.oredata.bank.user.model.TransactionModel;
import com.oredata.bank.user.model.UserModel;
import com.oredata.bank.user.service.AccountService;
import com.oredata.bank.user.service.JwtService;
import com.oredata.bank.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final UserService userService;
    private final AccountService accountService;
    private final JwtService jwtService;

    public TransactionController(UserService userService, AccountService accountService, JwtService jwtService) {
        this.userService = userService;
        this.accountService = accountService;
        this.jwtService = jwtService;
    }

    @GetMapping("/transaction/{number}")
    public ResponseEntity controlAccountNumber(@PathVariable("number") String accountNumber){
        return ResponseEntity.ok(userService.getAccountByNumber(accountNumber));
    }

    @PostMapping("/transfer")
    public GenericResponse transfer(@RequestBody TransferDTO transferDTO){
        AccountDTO accountDTO = userService.transfer(transferDTO);
       return new GenericResponse("Success",accountDTO,null,200);
    }

    @GetMapping
    public List<TransactionModel> transactionModelList(@CookieValue(value = "cookieValue",required = false) String cookie){
        String token = jwtService.extractEmail(cookie);
        UserDTO userModel = userService.getUserByEmail(token);
        return accountService.getAllTransactionByFromId(userModel);
    }


}
