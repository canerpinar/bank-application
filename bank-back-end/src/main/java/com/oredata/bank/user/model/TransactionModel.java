package com.oredata.bank.user.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;



@Entity(name = "transaction")
@Table(name = "transactions")
public class TransactionModel {
   public enum TransactionStatus{
        SUCCESS,
        FAILED,
       ;

   }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_id")
    private String from;
    @Column(name="to_id")
    private String to;

    @Column(name = "amount")
    private BigDecimal amount;

    @CreationTimestamp
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "transactionStatus")
    private TransactionStatus transactionStatus;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public TransactionModel(String from, String to, BigDecimal amount, TransactionStatus transactionStatus) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.transactionStatus = transactionStatus;
    }

    public TransactionModel() {
    }
}
