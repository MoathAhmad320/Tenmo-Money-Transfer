package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private Long accountId;
    private Long userId;
    private BigDecimal Balance;

    public Account() {
    }

    public Account(Long accountId, Long userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        Balance = balance;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }



    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return Balance;
    }

    public void setBalance(BigDecimal balance) {
        Balance = balance;
    }



}
