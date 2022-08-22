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

    public List<Account> listAccounts () {
        List<Account> accounts= new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Account[]> response =
                    restTemplate.exchange(baseUrl + "accounts", HttpMethod.GET, entity, Account[].class);
            accounts = Arrays.asList(response.getBody());
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return accounts;
    }

    public List<User> listUsers (){
        List<User> users = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(baseUrl + "users", HttpMethod.GET, entity, User[].class);
            users = Arrays.asList(response.getBody());
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;
    }

    public Account retrieveAccountById(AuthenticatedUser user, long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(baseUrl + "accounts/" + id, HttpMethod.GET, entity, Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
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
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    public void displayBalance(Account account){
        System.out.println("Your current amount balance is : $" + account.getBalance());
    }

    public void adjustBalance (Account toAccount, Account fromAccount, BigDecimal balance){
        toAccount.increaseBalance(balance);
        fromAccount.decreaseBalance(balance);
    }
}
