package com.guptaji.microservice.UserMicroservice.controller;

import com.guptaji.microservice.UserMicroservice.entities.Hotel;
import com.guptaji.microservice.UserMicroservice.entities.Rating;
import com.guptaji.microservice.UserMicroservice.entities.User;
import com.guptaji.microservice.UserMicroservice.service.UserServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/user")
public class UserController {

  Logger LOG = LogManager.getLogger(UserController.class);

  @Autowired private UserServiceImpl userService;

  @Autowired private RestTemplate restTemplate;

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

  @GetMapping("/userWithRatings/{id}")
  public ResponseEntity<?> getUserWithRatingsById(@PathVariable("id") int userId) {
    LOG.info("Hit getUserWithRatingsById API");
    User user = userService.getUserById(userId);

    // Now we will call our Rating and Hotel Microservice using RestTemplate

    // Right now the URLs are constant hardcoded
    // Below we are using array because ArrayList was giving some class cast exception
    //    List<Rating> ratings =
    //            restTemplate.getForObject(
    //                "http://localhost:9091/rating/ratingByUserId/" + userId, ArrayList.class);

    // Using Array
    Rating[] ratings =
        restTemplate.getForObject(
            "http://localhost:9091/rating/ratingByUserId/" + userId, Rating[].class);

    List<Rating> ratingListFromArray = Arrays.stream(ratings).toList();

    List<Rating> ratingList =
        ratingListFromArray.stream()
            .map(
                rating -> {
                  Hotel hotel =
                      restTemplate.getForObject(
                          "http://localhost:9090/hotel/" + rating.getHotelId(), Hotel.class);
                  rating.setHotel(hotel);
                  return rating;
                })
            .collect(Collectors.toList());

    user.setRatings(ratingList);
    LOG.info("Fetch the data from the Rating API");
    return new ResponseEntity<>(user, HttpStatus.FOUND);
  }
}
