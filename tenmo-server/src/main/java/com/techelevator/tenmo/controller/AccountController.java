package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.RegisterUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
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

    @GetMapping(path ="/user/{id}")
    public Account retrieveAccounyByUserId(@PathVariable long id){
        return accountDao.retrieveAccountByUserId(id);
    }

    @GetMapping(path ="/{id}")
    public Account retrieveAccounyById(@PathVariable long id){
        return accountDao.retrieveAccountById(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Account> listAccounts(){
        return accountDao.listAccounts();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@Valid @PathVariable long id, @RequestBody Account account) {
        accountDao.updateAccount(account, id);
//        if (!userDao.create(newUser.getUsername(), newUser.getPassword())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User registration failed.");
        }

}
