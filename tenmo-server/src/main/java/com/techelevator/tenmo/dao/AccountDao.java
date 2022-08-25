package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    Account retrieveAccountById (long id);
    Account retrieveAccountByUserId (long id);
    List<Account> listAccounts();
     void updateAccount (Account account, long id);
}
