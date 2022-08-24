package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

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


    public boolean create(Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer,headers);
        boolean success = false;
        try {
            restTemplate.exchange(baseUrl + "transfers", HttpMethod.POST, entity, Transfer.class);
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
public void displayTransferHistory(Account userAccount){
        AccountService accountService = new AccountService("http://localhost:8080/");
    System.out.print("\n\n***********************************************************\n");
    System.out.println("Transfer History");
    System.out.println("***********************************************************");
    System.out.println();
    System.out.printf("%20s, %30s, %20s,%n","Transfer Id", "From/To", "Amount");
    System.out.println("***********************************************************");
    for(Transfer x:listTransfers()){
        System.out.printf("%20s, %30s, %20s,%n",x.getId(),(userAccount.getAccountId()==x.getFromAccount()?
                "TO: "+accountService.retrieveAccountById(x.getToAccount()).getUser().getUsername():
                "FROM: "+accountService.retrieveAccountById(x.getFromAccount()).getUser().getUsername())
                , "$"+x.getAmount());
    }}

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

}
