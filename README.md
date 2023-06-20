### Microservice

1. Here we created one microservice 'User Microservice'.
2. This microservice is using Mysql database.
3. Swagger UI URL -- http://localhost:9092/swagger-ui/index.html
4. This microservice is also using another two microservices -
    1. [Rating microservice](https://github.com/ayushdgupta/SpringBoot3-Rating-Microservice).
    2. [Hotel microservice](https://github.com/ayushdgupta/SpringBoot3-Hotel-Microservice). 
5. Now we need to register our microservice with our eureka server, for that we need to follow below steps -
   1. First we need to add below dependencies -
   ```groovy
   implementation 'org.springframework.cloud:spring-cloud-starter'
   implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
   ```
   2. Here we added two dependencies one is for Spring-cloud and another one is for Eureka-client.
   3. Apart from that we need to add few other configuration like depenedency management and Spring cloud version.
   4. We need to add few dependencies in Application.yml file -
   ```yaml
   eureka:
     instance:
       prefer-ip-address: true
     client:
       register-with-eureka: true
       fetch-registry: true
       service-url:                                    # this property will tell our microservice ki kis server ke
        defaultZone: http://localhost:8761/eureka     # paas jake register hona hai.
   ```
6. This Microservice is registerd at below Eureka Server -
   [Eureka Server](https://github.com/ayushdgupta/SpringBoot3-Eureka-Service-Microservice)
7. This Microservice is first calling 'Rating Microservice' and then 'Hotel Microservice' for below API.
```
http://localhost:9092/user/userWithRatings/1
http://localhost:9092/user/userWithRatingsUsingFeign/1
```
8. In this code one URL/API is using RestTemplate -  
```
http://localhost:9092/user/userWithRatings/1
```
9. For RestTemplate we need to simply create one method with simple '@Bean' annotation in Configuration class and then we can 'Autowired' it anywhere.
10. For Feign Client we need to follow below steps -  
    1. First we need to add feign client dependency in build.gradle -
    ```groovy
	   implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
    ```
    2. In the main class declare one annotation **'@EnableFeignClients'**.
    3. Now define the Interface or client (@FeignClient(name = "Hotel-Service")) like Rest client in Quarkus.
    4. Now we can Autowired our client -
    ```JAVA
      @Autowired private HotelClient hotelClient;
    ```
11. This API is configured with below API-Gateway --  
[API Gateway](https://github.com/ayushdgupta/SpringBoot3-APIGateway-Microservice)