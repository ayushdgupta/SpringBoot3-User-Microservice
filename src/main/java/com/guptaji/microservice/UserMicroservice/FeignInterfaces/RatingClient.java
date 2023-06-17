package com.guptaji.microservice.UserMicroservice.FeignInterfaces;

import com.guptaji.microservice.UserMicroservice.entities.Rating;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Rating-Service")
public interface RatingClient {

  @GetMapping("/rating/ratingByUserId/{userId}")
  List<Rating> getRatingList(@PathVariable("userId") int userId);
}
