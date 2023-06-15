package com.guptaji.microservice.UserMicroservice.service;

import com.guptaji.microservice.UserMicroservice.entities.User;

import java.util.List;

public interface UserService {

  User saveUser(User user);

  List<User> getAllUser();

  User getUserById(int id);
}
