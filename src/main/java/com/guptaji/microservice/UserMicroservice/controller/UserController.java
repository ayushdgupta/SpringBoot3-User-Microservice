package com.guptaji.microservice.UserMicroservice.controller;

import com.guptaji.microservice.UserMicroservice.FeignInterfaces.HotelClient;
import com.guptaji.microservice.UserMicroservice.FeignInterfaces.RatingClient;
import com.guptaji.microservice.UserMicroservice.entities.Hotel;
import com.guptaji.microservice.UserMicroservice.entities.Rating;
import com.guptaji.microservice.UserMicroservice.entities.User;
import com.guptaji.microservice.UserMicroservice.service.UserServiceImpl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

// '@RefreshScope' annotation is used to refresh / update all the environment properties that
// UserController is reading from Config-files using Config-server whenever '/refresh' endpoint of
// Spring boot actuator will be hit.
@RestController
@RefreshScope
@RequestMapping("/user")
public class UserController {

  Logger LOG = LogManager.getLogger(UserController.class);

  @Value("${server.port}")
  private int portNo;

  @Autowired private UserServiceImpl userService;

  @Autowired private RestTemplate restTemplate;

  @Autowired private HotelClient hotelClient;

  @Autowired private RatingClient ratingClient;

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

  // In this API we are using RestTemplate to call other microservices.
  @GetMapping("/userWithRatings/{id}")
  public ResponseEntity<?> getUserWithRatingsById(@PathVariable("id") int userId) {
    LOG.info("Hit getUserWithRatingsById API");
    User user = userService.getUserById(userId);

    // Now we will call our Rating and Hotel Microservice using RestTemplate

    // Right now the URLs are constant i.e. hardcoded, our URLs are depend on the Host - localhost
    // and Port but when our microservices will deploy on any server then both the properties will
    // change, so we need to remove the dependency of these two things. Below we are using array
    // because ArrayList was giving some class cast exception
    //    List<Rating> ratings =
    //            restTemplate.getForObject(
    //                "http://localhost:9091/rating/ratingByUserId/" + userId, ArrayList.class);

    // Using Array
    //    Rating[] ratings =
    //        restTemplate.getForObject(
    //            "http://localhost:9091/rating/ratingByUserId/" + userId, Rating[].class);
    //
    //    List<Rating> ratingListFromArray = Arrays.stream(ratings).toList();
    //
    //    List<Rating> ratingList =
    //        ratingListFromArray.stream()
    //            .map(
    //                rating -> {
    //                  Hotel hotel =
    //                      restTemplate.getForObject(
    //                          "http://localhost:9090/hotel/" + rating.getHotelId(), Hotel.class);
    //                  rating.setHotel(hotel);
    //                  return rating;
    //                })
    //            .collect(Collectors.toList());

    // Above we were using hardcoded URLs, so now we will use the application names which are
    // registered with the eureka server, and we also need to add '@LoadBalanced' annotation with
    // our RestTemplate bean then only our application will be able to call other microservices.
    Rating[] ratings =
        restTemplate.getForObject(
            "http://Rating-Service/rating/ratingByUserId/" + userId, Rating[].class);

    List<Rating> ratingListFromArray = Arrays.stream(ratings).toList();

    List<Rating> ratingList =
        ratingListFromArray.stream()
            .map(
                rating -> {
                  Hotel hotel =
                      restTemplate.getForObject(
                          "http://Hotel-Service/hotel/" + rating.getHotelId(), Hotel.class);
                  rating.setHotel(hotel);
                  return rating;
                })
            .collect(Collectors.toList());

    user.setRatings(ratingList);
    LOG.info("Fetch the data from the Rating API");
    return new ResponseEntity<>(user, HttpStatus.FOUND);
  }

  // In this API we will use Feign Client (like RegisterRestClient in Quarkus)
  // Retry and CircuitBreaker does not work simultaneously so we need to do some changes in
  // application.yaml file (changing aspect order and removing fallback of retry).

  int retryCount = 1;

  @GetMapping("/userWithRatingsUsingFeign/{id}")
  //  @Retry(name = "userRatingHotelRetry", fallbackMethod = "userRatingHotelFallback")
  @Retry(name = "userRatingHotelRetry")
  @CircuitBreaker(name = "userRatingHotelBreaker", fallbackMethod = "userRatingHotelFallback")
  @RateLimiter(name = "userRatingHotelRateLimiter", fallbackMethod = "userRatingHotelFallback")
  public ResponseEntity<?> getUserWithRatingsByIdUsingFeign(@PathVariable("id") int userId) {
    LOG.info("Retry count {}", retryCount++);
    LOG.info("Hit getUserWithRatingsByIdUsingFeign API");
    User user = userService.getUserById(userId);

    List<Rating> ratings = ratingClient.getRatingList(userId);

    List<Rating> ratingList =
        ratings.stream()
            .map(
                rating -> {
                  Hotel hotel = hotelClient.getHotel(rating.getHotelId());
                  rating.setHotel(hotel);
                  return rating;
                })
            .collect(Collectors.toList());

    user.setRatings(ratingList);
    LOG.info("Fetch the data from the Rating API");
    return new ResponseEntity<>(user, HttpStatus.FOUND);
  }

  // fallback method
  public ResponseEntity<?> userRatingHotelFallback(int userId, Exception e) {
    LOG.warn("Fallback method is called please take care of your microservice");
    return new ResponseEntity<>(
        "some error came in microservice calling " + e.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // We are creating this api to check load-balancing implemented or not
  @GetMapping("/checkLoadBalancing")
  public ResponseEntity<?> checkLoadBalancing() throws InterruptedException {
    LOG.info("Hit CheckLoadBalancing API with portNo {}", portNo);
    List<User> userList = userService.getAllUser();
    Thread.sleep(120000);
    return new ResponseEntity<>(portNo, HttpStatus.OK);
  }
}
