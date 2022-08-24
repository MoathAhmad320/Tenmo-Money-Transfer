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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public UserService(String baseUrl) {
        this.baseUrl = baseUrl;
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


    public User retrieveUserById(long id) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        User newUser = new User();
        try {
            ResponseEntity<User> response =
                    restTemplate.exchange(baseUrl + "users/" + id, HttpMethod.GET, entity, User.class);
           newUser = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return newUser;
    }

    public void displayUsers(AuthenticatedUser user, List<User> users){
        System.out.printf("%n,%n","**************************************************");
        System.out.printf("%15s, %20s,%n", "User Id", "Name");
        System.out.println("**************************************************");
        for(User x:users){
            System.out.format("%15s, %20s, %n", x.getId(),x.getUsername());
        }
    }
}
