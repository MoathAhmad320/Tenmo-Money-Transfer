package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final UserService userService = new UserService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private Account currentAccount;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();

        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser!=null){
        currentAccount = accountService.retrieveAccountByUserId(currentUser,currentUser.getUser().getId());}
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        accountService.displayBalance(currentAccount);
	}

	private void viewTransferHistory() {
        transferService.displayTransferHistory(currentUser,currentAccount,transferService.transfersByUser(currentAccount.getAccountId(),TransferService.listTransfers(currentUser)));
        System.out.println("****************************************************");
        int transferId = consoleService.promptForInt("Enter transfer id to view transfer details or enter 0 to return to main menu:");
        if(transferId==0){
            mainMenu();
        } else if (!transferService.validateTransferId(transferId,transferService.transfersByUser(currentAccount.getAccountId(),TransferService.listTransfers(currentUser)))) {
            System.out.println("Invalid selection, Please enter a valid Transfer Id");
            viewTransferHistory();
        } else {
            transferService.displayTransferDetails(currentUser,transferId);
            consoleService.pause();
            viewTransferHistory();
        }

	}

	private void viewPendingRequests() {
        transferService.displayPending(currentUser, currentAccount,transferService.transfersByUser(currentAccount.getAccountId(),TransferService.listTransfers(currentUser)));
        System.out.println("****************************************************");
        int transferId = consoleService.promptForInt("Enter transfer id to respond to pending transfer or enter 0 to return to main menu:");
        if(transferId==0){
            mainMenu();
        } else if (!transferService.validateTransferId(transferId,transferService.transfersByUser(currentAccount.getAccountId(),TransferService.listTransfers(currentUser)))) {
            System.out.println("Invalid selection, Please enter a valid Transfer Id");
            viewPendingRequests();
        } else {
            transferService.displayTransferDetails(currentUser, transferId);
            transferService.displayResponse();
            int response = consoleService.promptForInt("Please choose an option:");
            if (response == 0) {
                mainMenu();
            } else {

                Transfer transfer = TransferService.retrieveTransferById(currentUser,transferId);
                transferService.pendingResponse(currentUser, response, transfer);
                System.out.println("Pending Transfer successfully responded to");
            }
        }
    }
	private void sendBucks() {
userService.displayUsers(currentUser,userService.listUsers(currentUser));
int userId = consoleService.promptForInt("Please enter the User Id you wish to send TE Bucks to (Enter 0 to cancel): ");
if (userId==0){mainMenu();
} else if (!userService.validateId(currentUser.getUser().getId(),userId,userService.listUsers(currentUser))) {
    System.out.println("Invalid selection, Please enter a valid User Id");
    sendBucks();
}
        Account toAccount = accountService.retrieveAccountByUserId(currentUser,userId);
BigDecimal amount = consoleService.promptForBigDecimal("Please enter the amount of TE Bucks you wish to send (Enter 0 to cancel): ");
if(amount.intValue()==0){mainMenu();
} else if (amount.doubleValue()<=0){
            System.out.println("Negative numbers not allowed, Please try again with a valid amount");
sendBucks();
} else {
    transferService.sendTransfer(currentUser, toAccount, currentAccount, amount);
}}

	private void requestBucks() {
        userService.displayUsers(currentUser,userService.listUsers(currentUser));
        int userId = consoleService.promptForInt("Please enter the User Id you wish to request TE Bucks from (Enter 0 to cancel): ");
        if (userId==0){mainMenu();
        } else if (!userService.validateId(currentUser.getUser().getId(),userId,userService.listUsers(currentUser))) {
            System.out.println("Invalid selection, Please enter a valid User Id");
            requestBucks();
        }
        Account fromAccount = accountService.retrieveAccountByUserId(currentUser,userId);
        BigDecimal amount = consoleService.promptForBigDecimal("Please enter the amount of TE Bucks you wish to request (Enter 0 to cancel): ");
        if(amount.intValue()==0){mainMenu();
        } else if (amount.doubleValue()<=0){
            System.out.println("Negative numbers not allowed, Please try again with a valid amount");
            requestBucks();
        } else {
            transferService.requestTransfer(currentUser, currentAccount,fromAccount,amount);
        }
	}

}
