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
        currentAccount = accountService.retrieveAccountByUserId(currentUser.getUser().getId());}
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
        transferService.displayTransferHistory(currentAccount);
        System.out.println("****************************************************");
        int transferId = consoleService.promptForInt("Enter transfer id to view transfer details or enter 0 to return to main menu:");
        if(transferId==0){
            mainMenu();
        } else {
            transferService.displayTransferDetails(transferId);
            consoleService.pause();
            viewTransferHistory();
        }

	}

	private void viewPendingRequests() {

        transferService.displayPendingTransfers(currentAccount);
        System.out.println("****************************************************");
        int transferId = consoleService.promptForInt("Enter transfer id to view transfer details or enter 0 to return to main menu:");
        if(transferId==0){
            mainMenu();
        } else {
            transferService.displayTransferDetails(transferId);
            consoleService.pause();
            viewPendingRequests();
        }


    }
	private void sendBucks() {
userService.displayUsers(currentUser,userService.listUsers());
int userId = consoleService.promptForInt("Please enter the User Id you wish to send TE Bucks to (Enter 0 to cancel): ");
if (userId==0){mainMenu();
} else if (!userService.validateId(currentUser.getUser().getId(),userId,userService.listUsers())) {
    System.out.println("Invalid selection, Please enter a valid User Id");
    sendBucks();
}
        Account toAccount = accountService.retrieveAccountByUserId(userId);
BigDecimal amount = consoleService.promptForBigDecimal("Please enter the amount of TE Bucks you wish to send (Enter 0 to cancel): ");
if(amount.intValue()==0){mainMenu();
} else if (amount.doubleValue()<=0){
            System.out.println("Negative numbers not allowed, Please try again with a valid amount");
sendBucks();
} else {
    transferService.sendTransfer(toAccount, currentAccount, amount);
}}

	private void requestBucks() {
        userService.displayUsers(currentUser,userService.listUsers());
        int userId = consoleService.promptForInt("Please enter the User Id you wish to request TE Bucks from (Enter 0 to cancel): ");
        if (userId==0){mainMenu();
        } else if (!userService.validateId(currentUser.getUser().getId(),userId,userService.listUsers())) {
            System.out.println("Invalid selection, Please enter a valid User Id");
            requestBucks();
        }
        Account fromAccount = accountService.retrieveAccountByUserId(userId);
        BigDecimal amount = consoleService.promptForBigDecimal("Please enter the amount of TE Bucks you wish to request (Enter 0 to cancel): ");
        if(amount.intValue()==0){mainMenu();
        } else if (amount.doubleValue()<=0){
            System.out.println("Negative numbers not allowed, Please try again with a valid amount");
            requestBucks();
        } else {
            transferService.requestTransfer(currentAccount,fromAccount,amount);
        }
	}

}
