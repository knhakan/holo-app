package com.holoride.demo.controller;


import com.holoride.demo.exception.CustomException;
import com.holoride.demo.exception.ResourceAlreadyExists;
import com.holoride.demo.exception.ResourceNotFoundException;
import com.holoride.demo.exception.UnauthorizedException;
import com.holoride.demo.model.User;
import com.holoride.demo.repository.UserRepository;
import com.holoride.demo.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserServiceImpl userDetailService;

    @Autowired
    UserRepository repository;

    @PostMapping(value = "/api/add", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addUser(@RequestBody User user) {
        User addedUser;
        if(userDetailService.doesUserExist(user.getUsername())){
            throw new ResourceAlreadyExists("Resource already exists in DB.");
        }
        else{
            if(user.getPassword().isEmpty() || user.getUsername().isEmpty() )
                throw new CustomException("Please provide username and/or password ");
            else
            addedUser = userDetailService.addUser(user);
        }
        return new ResponseEntity<>(addedUser, HttpStatus.OK);
    }


    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userDetailService.getAllUsers();
        if  (userList.isEmpty()) {
            throw new ResourceNotFoundException("There is no user");
        }else {
            return new ResponseEntity<>(userList, HttpStatus.OK);
        }
    }

    @GetMapping("/api/users/{userId}")
    public ResponseEntity<Optional<User>> findUserById(@PathVariable("userId") long userId, Principal principal) {
            Optional<User> user = userDetailService.findUserById(userId);
        if  (user.isEmpty()) {
            throw new ResourceNotFoundException("There is no user");
        }
        if (user.get().getUsername() == principal.getName()) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            throw new UnauthorizedException("User is not authorized");
        }
    }

    @PutMapping(value = "/api/users/{userId}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User userToUpdate,Principal principal) {
        if(userDetailService.isUserAllowed(userId,principal)){
        User updatedUser = userDetailService.updateUser(userId, userToUpdate);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        else{
            throw new UnauthorizedException("User is not authorized");
        }
    }

    @DeleteMapping(value = "/api/users/{userId}")
    public ResponseEntity<Long> deleteUser(@PathVariable Long userId, Principal principal) {
        boolean isRemoved;
        if(userDetailService.isUserAllowed(userId,principal)){
            isRemoved = userDetailService.deleteUser(userId);
            if (!isRemoved) {
                throw new ResourceNotFoundException("User is not authorized");
            }
            return new ResponseEntity<>(userId, HttpStatus.OK);
        }
        else{
            throw new UnauthorizedException("User is not authorized");
        }
    }

    @GetMapping(value ="/")
    public String welcomeUser() {
        return "welcome to the server";
    }
}
