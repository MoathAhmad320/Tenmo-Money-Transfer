package com.techelevator.tenmo.services;

import com.techelevator.tenmo.App;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransferService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private Transfer transfer;

    public TransferService(String url) {
        this.baseUrl = url;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    public static List<Transfer> listTransfers () {
        RestTemplate restTemplate1 = new RestTemplate();
        List<Transfer> transfers= new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate1.exchange("http://localhost:8080/transfers", HttpMethod.GET, entity, Transfer[].class);
            transfers = Arrays.asList(response.getBody());
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public static Transfer retrieveTransferById(long id) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate1 = new RestTemplate();
        Transfer transferByID = new Transfer();
        try {
            ResponseEntity<Transfer> response =
                    restTemplate1.exchange("http://localhost:8080/transfers/" + id, HttpMethod.GET, entity, Transfer.class);
            transferByID = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferByID;
    }


    public static boolean create(Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer,headers);
        boolean success = false;
        RestTemplate restTemplate1 = new RestTemplate();
        try {
            restTemplate1.exchange("http://localhost:8080/transfers", HttpMethod.POST, entity, Transfer.class);
            success = true;
        }catch(RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }
public List<Transfer> transfersByUser(long id, List<Transfer> transferList){
        List<Transfer> transfers = new ArrayList<>();
        for(Transfer x: transferList){
            if(id==x.getToAccount()||id==x.getFromAccount()){
                transfers.add(x);
            }
        }
return transfers;
}

    public static Transfer updateTransfer(Transfer transfer, long id) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Transfer> entity = new HttpEntity<>( transfer,headers);
        RestTemplate restTemplate1 = new RestTemplate();
        try {
            ResponseEntity<Transfer> response =
                    restTemplate1.exchange("http://localhost:8080/transfers/" +id, HttpMethod.PUT, entity, Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }
public void sendTransfer(Account toAccount, Account fromAccount, BigDecimal amount){
        AccountService accountService = new AccountService("http://localhost:8080/");
       if ((amount.doubleValue()>0) && (amount.doubleValue()<=fromAccount.getBalance().doubleValue())){
            Transfer newTransfer = new Transfer(1, fromAccount.getAccountId(), toAccount.getAccountId(),2,2,amount);
            create(newTransfer);
            accountService.adjustBalance(toAccount,fromAccount,amount);
            System.out.println("TE Bucks were successfully Sent");
        } else {
           System.out.println();
            System.out.println("Not enough TE Bucks in account to perform transaction");
            accountService.displayBalance(fromAccount);
           System.out.println();
           System.out.println("Returning to main menu");
        }}

    public void requestTransfer(Account toAccount, Account fromAccount, BigDecimal amount){
        if (amount.doubleValue()>0){
            Transfer newTransfer = new Transfer(1, fromAccount.getAccountId(), toAccount.getAccountId(),1,1,amount);
            create(newTransfer);
            System.out.println("TE Bucks request successfully sent");
        } else {
            System.out.println();
            System.out.println("Please enter a valid amount");
        }}



public void displayTransferHistory(Account userAccount, List<Transfer> list){
        AccountService accountService = new AccountService("http://localhost:8080/");
    System.out.print("\n\n***********************************************************\n");
    System.out.println("Transfer History");
    System.out.println("***********************************************************");
    System.out.println();
    System.out.printf("%20s, %20s, %20s, %20s,%n","Transfer Id", "From/To", "Amount","TYPE");
    System.out.println("***********************************************************");
    for(Transfer x:list){
        if(!(x.getType()==1 && userAccount.getAccountId().equals(x.getFromAccount()))){
        System.out.printf("%20s, %20s, %20s, %20s, %n",x.getId(),(userAccount.getAccountId()==x.getFromAccount()?
                "TO: "+accountService.retrieveAccountById(x.getToAccount()).getUser().getUsername():
                "FROM: "+accountService.retrieveAccountById(x.getFromAccount()).getUser().getUsername())
                , "$"+x.getAmount(), (x.getType()==1?"REQUEST":"SENT"));
    }}}

    public void displayPending(Account userAccount, List<Transfer> list){
        AccountService accountService = new AccountService("http://localhost:8080/");
        System.out.print("\n\n***********************************************************\n");
        System.out.println("Pending Transfers");
        System.out.println("***********************************************************");
        System.out.println();
        System.out.printf("%20s, %20s, %20s,%n","Transfer Id", "To", "Amount");
        System.out.println("***********************************************************");
        for(Transfer x:list){
            if(x.getType()==1 && x.getStatus()==1 && userAccount.getAccountId().equals(x.getFromAccount())){
                System.out.printf("%20s, %20s, %20s, %n",x.getId(),
                                "TO: "+accountService.retrieveAccountById(x.getToAccount()).getUser().getUsername(),"$"+x.getAmount());
            }}}


    public void displayTransferDetails (int id){
        AccountService accountService = new AccountService("http://localhost:8080/");
        transfer = retrieveTransferById(id);
        String status ="";
        if(transfer.getStatus()==1){status="PENDING";
        } else if (transfer.getStatus()==2) {status="APPROVED";}
            else if(transfer.getStatus()==3){status="REJECTED";
        }
        System.out.println();
        System.out.printf("%20s, %n","Transfer Details");
        System.out.println("***************************************");
        System.out.println("ID: "+transfer.getId());
        System.out.println("FROM: "+accountService.retrieveAccountById(transfer.getFromAccount()).getUser().getUsername());
        System.out.println("TO: "+accountService.retrieveAccountById(transfer.getToAccount()).getUser().getUsername());
        System.out.println("TYPE: "+(transfer.getType()==1?"REQUEST":"SEND"));
        System.out.println("STATUS: "+status);
        System.out.println("AMOUNT: "+transfer.getAmount());

    }

    public boolean validateTransferId(long checkId, List<Transfer> transfers){
        boolean status = false;
        for( Transfer x: transfers)
            if(x.getId()==(checkId)){
                status = true;
            }
        return status;
    }

    public void pendingResponse (int response, Transfer pendingTransfer){
        AccountService accountService = new AccountService("http://localhost:8080/");
        Account toAccount = new Account();
        Account fromAccount = new Account();
        if (response == 1) {
            pendingTransfer.setStatus(2);
            pendingTransfer.setType(2);
            updateTransfer(pendingTransfer,pendingTransfer.getId());
            toAccount = accountService.retrieveAccountById(pendingTransfer.getToAccount());
            fromAccount = accountService.retrieveAccountById(pendingTransfer.getFromAccount());
            accountService.adjustBalance(toAccount,fromAccount,pendingTransfer.getAmount());
        } else if (response == 2) {
            pendingTransfer.setStatus(3);
            updateTransfer(pendingTransfer, pendingTransfer.getId());
            toAccount = accountService.retrieveAccountById(pendingTransfer.getToAccount());
            fromAccount = accountService.retrieveAccountById(pendingTransfer.getFromAccount());
            accountService.adjustBalance(toAccount, fromAccount, new BigDecimal(0));
        } else{
            System.out.println("Invalid Entry");}
    }

    public void displayResponse(){
        System.out.println();
        System.out.println("PENDING TRANSFER RESPONSE");
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Return to main menu");
    }

}
