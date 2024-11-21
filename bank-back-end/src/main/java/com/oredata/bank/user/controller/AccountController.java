package com.oredata.bank.user.controller;

import com.oredata.bank.dto.AccountDTO;
import com.oredata.bank.dto.AccountSearch;
import com.oredata.bank.dto.GenericResponse;
import com.oredata.bank.mapper.AccountMapper;
import com.oredata.bank.user.model.AccountModel;
import com.oredata.bank.user.service.AccountService;
import com.oredata.bank.user.service.JwtService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {


    private final AccountService accountService;
    private final JwtService jwtService;
    private final AccountMapper accountMapper;



    public AccountController(AccountService accountService, JwtService jwtService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.jwtService = jwtService;
        this.accountMapper = accountMapper;
    }

    @GetMapping
    public List<AccountDTO> getAllAccount(@CookieValue(value = "cookieValue",required = false) String cookieValue){
        String username = jwtService.extractEmail(cookieValue);
        List<AccountDTO> accountDTOS = accountService.getAllAccount(username);
        return accountDTOS;
    }

    @GetMapping("/{id}")
    public List<AccountDTO> getAccountsById(@PathVariable("id") String id){
        List<AccountDTO> accountDTOS = accountService.getAccountsByUserId(id);
        System.out.println(accountDTOS);
        return accountDTOS;
    }

    @GetMapping("/account/{accountNo}")
    public AccountDTO getAccountsByAccountNo(@PathVariable("accountNo") String accountNo){
        return accountService.getAccountByAccountNo(accountNo);
    }

    @PutMapping("/account/{id}")
    public GenericResponse updateAccount(@PathVariable("id") String id, @RequestBody AccountDTO account){
        System.out.println("New Account" + account);
        accountService.updateAccount(id,account);
        return new GenericResponse("Success",account,null,200);
    }

    @DeleteMapping("/account/{id}")
    public GenericResponse delete(@PathVariable("id") String id){
        accountService.delete(id);
        return new GenericResponse("Success",id,null,200);
    }

    @PostMapping("/search")
    public GenericResponse search(@RequestBody AccountSearch accountSearch){
        List<AccountDTO> accountModels = accountService.getAccountBySearchKey(accountSearch);
        return new GenericResponse("SUCCESS",accountModels,null,200);
    }

    @PostMapping
    public GenericResponse saveAccount(@RequestBody AccountDTO accountDTO,@CookieValue(value = "cookieValue") String token){
        AccountModel accountModel = accountMapper.toAccountDTO(accountDTO);
        String email = jwtService.extractEmail(token);
        AccountDTO accountDTO1 =  accountService.saveAccount(accountModel,email);
        return new GenericResponse("Success",accountDTO1,null,200);
    }

}
