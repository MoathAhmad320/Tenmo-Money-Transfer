package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private long id;
    private Account fromAccount;
    private Account  toAccount;
    private int type;
    private int status;
    private BigDecimal amount;
}
