package com.oredata.bank.user.service;

import com.oredata.bank.dto.AccountDTO;
import com.oredata.bank.dto.AccountSearch;
import com.oredata.bank.dto.UserDTO;
import com.oredata.bank.mapper.AccountMapper;
import com.oredata.bank.user.model.AccountModel;
import com.oredata.bank.user.model.TransactionModel;
import com.oredata.bank.user.model.UserModel;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    /**
     * canerpinar
     * entity manager not final because of that every request it must create
     */
    private EntityManager entityManager;

    private final UserService userService;


    private final AccountMapper accountMapper;
    public AccountService(EntityManager entityManager, UserService userService, AccountMapper accountMapper) {
        this.entityManager = entityManager;
        this.userService = userService;
        this.accountMapper = accountMapper;
    }

    @Transactional
    public List<AccountDTO> getAccountsByUserId(String accountNumber) throws NoResultException {
        TypedQuery<AccountModel> query = entityManager.createQuery("SELECT account from accountModel account where account.user.id = :number", AccountModel.class);
        query.setParameter("number",accountNumber);
        return accountMapper.toAccountDtoList(query.getResultList());
    }

    public AccountDTO getAccountByAccountNo(String accountNumber) throws NoResultException{
        TypedQuery<AccountModel> query = entityManager.createQuery("SELECT account from accountModel account where account.number = :number", AccountModel.class);
        query.setParameter("number",accountNumber);
        return accountMapper.toAccount(query.getSingleResult());
    }

    public List<AccountDTO> getAllAccount(String email) throws NoResultException {
        TypedQuery<AccountModel> query = entityManager.createQuery("SELECT account from accountModel account where account.user.email <> :email", AccountModel.class);
        query.setParameter("email",email);
        List<AccountModel> accountModels = query.getResultList();
        return accountMapper.toAccountDtoList(accountModels);
    }

    /**
     *optimistic locking because of that not occur dirty read
     * @param id
     * @param account
     */
    @Transactional
    public void updateAccount(String id, AccountDTO account) {
        AccountModel accountModel = entityManager.find(AccountModel.class,id, LockModeType.PESSIMISTIC_WRITE);
        accountModel.setBalance(account.getBalance());
        accountModel.setNumber(account.getNumber());
        accountModel.setName(account.getName());
        entityManager.merge(accountModel);
    }

    @Transactional
    public void delete(String id) {
        AccountModel accountModel = entityManager.find(AccountModel.class,id);
        entityManager.remove(accountModel);
    }

    public List<TransactionModel> getAllTransactionByFromId(UserDTO userDTO){
        List<String> accountIds = userDTO.getAccountDTOS().stream().map(s->s.getId()).collect(Collectors.toList());
        TypedQuery<TransactionModel> query = entityManager.createQuery("SELECT t from transaction t where t.from IN :accountId_1 OR t.to IN :accountId_2", TransactionModel.class);
        query.setParameter("accountId_1",accountIds);
        query.setParameter("accountId_2",accountIds);
        List<TransactionModel> transactionModels = query.getResultList();
        return transactionModels;
    }

    public List<AccountDTO> getAccountBySearchKey(AccountSearch accountSearch) {

        TypedQuery<AccountModel> query = entityManager.createQuery("SELECT account from accountModel account where account.number LIKE  CONCAT('%',:search_1 ,'%') OR account.name LIKE CONCAT('%',:search_2 ,'%')", AccountModel.class);
        query.setParameter("search_1",accountSearch.getValue());
        query.setParameter("search_2",accountSearch.getValue());
        List<AccountModel> accountModels = query.getResultList();
        return accountMapper.toAccountDtoList(accountModels);

    }

    @Transactional
    public AccountDTO saveAccount(AccountModel accountModel, String email) {
        TypedQuery<UserModel> query = entityManager.createQuery("SELECT u from user u where u.email = :email", UserModel.class);
        query.setParameter("email",email);
        UserModel userModel = query.getSingleResult();
        accountModel.setUser(userModel);
        userModel.getAccounts().add(accountModel);
        entityManager.merge(userModel);
        return accountMapper.toAccount(accountModel);
    }
}
