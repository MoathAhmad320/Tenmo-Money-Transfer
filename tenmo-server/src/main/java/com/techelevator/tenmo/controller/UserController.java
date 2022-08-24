package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path ="/users")
public class UserController {

@Autowired
    private UserDao userDao;

    public UserController() {
    }

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> listUsers(){
        return userDao.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User retrieveUserById(@PathVariable long id){
        return userDao.findByUserId(id);
    }
}
