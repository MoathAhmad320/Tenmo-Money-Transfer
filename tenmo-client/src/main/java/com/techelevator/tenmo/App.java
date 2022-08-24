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
        accountService.displayBalance(accountService.retrieveAccountByUserId(currentUser.getUser().getId()));
	}

	private void viewTransferHistory() {
        transferService.displayTransferHistory(accountService.retrieveAccountByUserId(currentUser.getUser().getId()));
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
        for(Transfer x: transferService.transfersByUser(currentUser.getUser().getId(),transferService.listTransfers())){
            System.out.println(x.getToAccount()+" "+x.getFromAccount());
        }
	}

	private void sendBucks() {
//        System.out.println(1);
//        System.out.println(accountService.retrieveAccountById(2002).getAccountId());
//        System.out.println(2);
//        System.out.println(accountService.retrieveAccountByUserId(currentUser.getUser().getId()).getAccountId());
//        System.out.println(3);
//        System.out.println(accountService.retrieveAccountById(2002).getAuthenticatedUser().getUser().getUsername());
//        System.out.println(4);
//        System.out.println(accountService.retrieveAccountByUserId(currentUser.getUser().getId()).getAuthenticatedUser().getUser().getUsername());
//        System.out.println(5);
        System.out.println("here"+accountService.retrieveAccountByUserId(1003).getAccountId());
        System.out.println("then"+userService.retrieveUserById(1003).getUsername());
//        System.out.println(userService.retrieveUserById(1003).getUser().getUsername());

    }

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

}
