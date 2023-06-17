package com.guptaji.microservice.UserMicroservice.FeignInterfaces;

import com.guptaji.microservice.UserMicroservice.entities.Hotel;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Hotel-Service")
public interface HotelClient {

  @GetMapping("/hotel/{hotelId}")
  Hotel getHotel(@PathVariable("hotelId") int hotelId);
}
