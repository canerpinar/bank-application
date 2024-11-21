package com.oredata.bank.user.service;

import com.oredata.bank.dto.AccountDTO;
import com.oredata.bank.dto.TransferDTO;
import com.oredata.bank.dto.UserDTO;
import com.oredata.bank.dto.events.TransactionEvent;
import com.oredata.bank.exceptions.NotEnoughException;
import com.oredata.bank.mapper.AccountMapper;
import com.oredata.bank.mapper.UserMapper;
import com.oredata.bank.user.model.AccountModel;
import com.oredata.bank.user.model.TransactionModel;
import com.oredata.bank.user.model.UserModel;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.hibernate.StaleObjectStateException;
import org.hibernate.tool.schema.spi.SqlScriptException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseCookie;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private EntityManager entityManager;
    private final UserMapper userMapper;
    private final AccountMapper accountMapper;

    private final ApplicationEventPublisher publisher;



    public UserService(EntityManager entityManager, UserMapper userMapper, AccountMapper accountMapper, ApplicationEventPublisher publisher) {
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.accountMapper = accountMapper;
        this.publisher = publisher;
    }

    @Transactional
    public UserDTO save(UserDTO userDTO){
        UserModel userModel = userMapper.toUserModel(userDTO);
        entityManager.persist(userModel);
        return userMapper.toUserDTO(userModel);
    }

    //StaleObjectState exception is for locking exception
    @Transactional(dontRollbackOn = NotEnoughException.class,rollbackOn = {StaleObjectStateException.class, SqlScriptException.class})
    public AccountDTO transfer(TransferDTO transferDTO) throws NotEnoughException {

        AccountModel fromUser = getAccountByNumber(transferDTO.getFromAccount());
        AccountModel toUser = getAccountByNumber(transferDTO.getToAccount());
            if(fromUser.getBalance().compareTo(transferDTO.getAmount())>=0){
                BigDecimal currentToFrom = fromUser.getBalance().subtract(transferDTO.getAmount());
                BigDecimal currentTo = toUser.getBalance().add(transferDTO.getAmount());
                fromUser.setBalance(currentToFrom);
                toUser.setBalance(currentTo);
                entityManager.merge(fromUser);
                entityManager.merge(toUser);
                publisher.publishEvent(new TransactionModel(fromUser.getId(),toUser.getId(),transferDTO.getAmount(), TransactionModel.TransactionStatus.SUCCESS));
                return accountMapper.toAccount(fromUser);
            }else {
                entityManager.setFlushMode(FlushModeType.COMMIT);
                TransactionModel transactionModel = new TransactionModel(fromUser.getId(),toUser.getId(),transferDTO.getAmount(), TransactionModel.TransactionStatus.FAILED);
                entityManager.persist(transactionModel);
                entityManager.flush();
                throw new NotEnoughException(transactionModel,"Not enough balance");
            }
    }


    public AccountModel getAccountByNumber(String accountNumber) throws NoResultException{
        TypedQuery<AccountModel> query = entityManager.createQuery("SELECT account from accountModel account where account.number = :number", AccountModel.class);
        query.setParameter("number",accountNumber);
        AccountModel model = query.getSingleResult();
        entityManager.lock(model,LockModeType.PESSIMISTIC_WRITE);//
        return query.getSingleResult();
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void saveTransaction(TransactionModel transactionEvent){
        transactionEvent.setTransactionStatus(TransactionModel.TransactionStatus.valueOf(transactionEvent.getTransactionStatus().toString()));
        System.out.println("saving transfer event");
        entityManager.persist(transactionEvent);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void saveTransactionRollback(TransactionModel transactionEvent){
        transactionEvent.setTransactionStatus(TransactionModel.TransactionStatus.valueOf("FAILED"));
        System.out.println("saving transfer event");
        entityManager.persist(transactionEvent);
    }

    public UserDTO getUserByUsername(String username) throws NoResultException{
        TypedQuery<UserModel> query = entityManager.createQuery("SELECT u from user u where u.username = :username", UserModel.class);
        query.setParameter("username",username);
        UserModel model = query.getSingleResult();
        return userMapper.toUserDTO(model);
    }

    public UserDTO getUserByEmail(String email) throws NoResultException{
        TypedQuery<UserModel> query = entityManager.createQuery("SELECT u from user u where u.email = :email", UserModel.class);
        query.setParameter("email",email);
        UserModel model = query.getSingleResult();
        return userMapper.toUserDTO(model);
    }

    public UserDTO getUserById(String id) throws NoResultException{
        return userMapper.toUserDTO(entityManager.find(UserModel.class,id));
    }
}
