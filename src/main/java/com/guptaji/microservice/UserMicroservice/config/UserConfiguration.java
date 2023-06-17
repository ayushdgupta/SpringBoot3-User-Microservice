package com.guptaji.microservice.UserMicroservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UserConfiguration {

  //  The loadBalancer takes the logical service-name (as registered with the discovery-server) and
  // converts it to the actual hostname of the chosen microservice.
  @Bean
  @LoadBalanced
  public RestTemplate getRestTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate;
  }
}
