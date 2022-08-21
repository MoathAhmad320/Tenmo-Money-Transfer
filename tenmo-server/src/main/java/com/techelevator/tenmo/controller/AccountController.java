package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/accounts")
public class AccountController {
    @Autowired
    private AccountDao accountDao;

    public AccountController() {
    }
    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @GetMapping(path ="/{id}")
    public Account retrieveAccounyByUserId(@PathVariable long id){
        return accountDao.retrieveAccountByUserId(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Account> listAccounts(){
        return accountDao.listAccounts();
    }

}
