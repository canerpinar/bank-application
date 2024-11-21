package com.oredata.bank.exceptions;

import com.oredata.bank.dto.TransferDTO;
import com.oredata.bank.dto.events.TransactionEvent;
import com.oredata.bank.user.model.TransactionModel;

/**
 * if transfer has not enough balance
 */
public class NotEnoughException extends RuntimeException{
    private final TransactionModel transactionModel;
    private final String message;

    public NotEnoughException(TransactionModel transactionEvent, String message) {
        this.transactionModel = transactionEvent;
        this.message = message;
    }

    public TransactionModel getTransactionEvent() {
        return transactionModel;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
