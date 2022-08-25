package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDAO {
    List<Transfer> listTransfers();
    Transfer retrieveTransferById(long id);
    void updateTransfer (Transfer transfer, long id);
    boolean create(long toAccount, long fromAccount, int type, int status, BigDecimal amount);
}
