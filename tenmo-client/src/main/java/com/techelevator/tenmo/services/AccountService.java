package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AccountService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserService userService = new UserService("http://localhost:8080/");
    private Account account;


    public AccountService(String url) {
        this.baseUrl = url;
    }


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Account> listAccounts (AuthenticatedUser user) {
        List<Account> accounts= new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Account[]> response =
                    restTemplate.exchange(baseUrl + "accounts", HttpMethod.GET, entity, Account[].class);
            accounts = Arrays.asList(response.getBody());}
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return accounts;
    }



    public Account retrieveAccountById(AuthenticatedUser user, long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(baseUrl + "accounts/" + id, HttpMethod.GET, entity, Account.class);
            account = response.getBody();
            account.setUser(userService.retrieveUserById(user, account.getUserId()));}
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }



    public Account retrieveAccountByUserId(AuthenticatedUser user, long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(baseUrl + "accounts/user/" + id, HttpMethod.GET, entity, Account.class);
            account = response.getBody();
            account.setUser(userService.retrieveUserById(user,id));}
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    public static Account updateAccount(AuthenticatedUser user, Account account, long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Account> entity = new HttpEntity<>( account,headers);
        RestTemplate restTemplate1 = new RestTemplate();
        try {
            ResponseEntity<Account> response =
                    restTemplate1.exchange("http://localhost:8080/accounts/" +id, HttpMethod.PUT, entity, Account.class);
            account = response.getBody();}
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }


    public void displayBalance(Account account){
        System.out.println("\n\n**************************************************"+
                            "\nYour current amount balance is : $" + account.getBalance()+
                            "\n**************************************************");
    }


    public void adjustBalance (AuthenticatedUser user, Account toAccount, Account fromAccount, BigDecimal balance){
        toAccount.increaseBalance(balance);
        fromAccount.decreaseBalance(balance);
        updateAccount(user, toAccount,toAccount.getAccountId());
        updateAccount(user, fromAccount,fromAccount.getAccountId());
    }
}
