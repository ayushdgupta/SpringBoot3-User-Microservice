package com.guptaji.microservice.UserMicroservice.controller;

import com.guptaji.microservice.UserMicroservice.entities.User;
import com.guptaji.microservice.UserMicroservice.service.UserServiceImpl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

  Logger LOG = LogManager.getLogger(UserController.class);

  @Autowired private UserServiceImpl userService;

  @PostMapping
  public ResponseEntity<?> createUser(@RequestBody User user) {
    LOG.info("Hit createUser API");
    User savedUser = userService.saveUser(user);
    return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getUserById(@PathVariable("id") int userId) {
    LOG.info("Hit getUserById API");
    User user = userService.getUserById(userId);
    return new ResponseEntity<>(user, HttpStatus.FOUND);
  }

  @GetMapping
  public ResponseEntity<?> getAllUsers() {
    LOG.info("Hit getAllUsers API");
    List<User> userList = userService.getAllUser();
    return new ResponseEntity<>(userList, HttpStatus.OK);
  }
}
