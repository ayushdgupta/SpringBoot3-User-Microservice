package com.guptaji.microservice.UserMicroservice.service;

import com.guptaji.microservice.UserMicroservice.entities.User;
import com.guptaji.microservice.UserMicroservice.repositories.UserRepo;

import java.util.List;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  Logger LOG = LogManager.getLogger(UserServiceImpl.class);

  @Autowired private UserRepo userRepo;

  @Override
  public User saveUser(User user) {
    LOG.info("Saving the USer in DB");
    return userRepo.save(user);
  }

  @Override
  public List<User> getAllUser() {
    LOG.info("Fetching all the user from DB");
    return userRepo.findAll();
  }

  @Override
  public User getUserById(int id) {
    LOG.info("Fetching the data for the user {} from the DB", id);
    return userRepo
        .findById(id)
        .orElseThrow(() -> new NoSuchElementException("No user present in DB for the id " + id));
  }
}
