package com.oredata.bank.dto.events;

import java.math.BigDecimal;

public class TransactionEvent {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String from;

    private String to;

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


    public TransactionEvent() {
    }

    public TransactionEvent(String status, BigDecimal amount, String from, String to) {
        this.status = status;
        this.amount = amount;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "TransactionEvent{" +
                "status='" + status + '\'' +
                ", amount=" + amount +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
