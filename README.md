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
11. Before regitering the microservices with eureka server, if any microservice want to contact anyone then they
    need to use their host and IP Address (e.g. localhost:8080/rating), but after registering our microservices with Eureka server
    there is no need for them to contact with each other using above props, they can simply use their application name.
    because application name is autoatically tied-up with those by eureka server (e.g. http://Config-Server) these things we
    can verify in the logs of eureka server as well when we start this server.
12. This API is configured with below API-Gateway - [API Gateway](https://github.com/ayushdgupta/SpringBoot3-APIGateway-Microservice)
13. This API is using Config-server to fetch common configuration - [Config-Server](https://github.com/ayushdgupta/SpringBoot3-ConfigServer-Microservice)
14. Configurations are present on Github - [Common-Configuration](https://github.com/ayushdgupta/SpringBoot3-ConfigFiles-ConfigServer-Microservice)
15. To test load-balacing in our microservice we were using "/checkLoadBalancing" API in our code by firing below URL via API-Gateway and  
    we also create the two instances of our API in intellij.
```
http://localhost:9093/user/checkLoadBalancing
```

### Microservice as config-client
1. To use our microservice as config client we need to add below dependency -
```groovy
implementation 'org.springframework.cloud:spring-cloud-starter-config'
```
2. Apart from that we need to add few configurations in our application.yaml file -
```yaml
spring:
  config:                             # this propes is used to connect to the config-server
    import: optional:configserver:http://localhost:9094
```
3. By Adding above two configurations our microservice will act as config-client, so now it can fetch any values
from config files whatever required internally (eureka server props) or externally (using @Value, System.getProperty()).
4. @RefreshScope

### Resilience4J Fault-tolerance
1. To use Resilience4J in our project we need to add below dependencies in build.gradle -
```groovy
implementation 'org.springframework.boot:spring-boot-starter-actuator'
implementation 'org.springframework.boot:spring-boot-starter-aop'
implementation 'org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j'
```
2. CircuitBreaker, Retry and RateLimiter will not work together because of their aspects order so for that follow below links --
    1. https://resilience4j.readme.io/docs/getting-started-3#aspect-order
    2. https://stackoverflow.com/questions/71457925/resiliency4j-circuit-breaker-with-retry-configuration-not-working

3. For resilience4j circuit-breaker props follow link -- https://resilience4j.readme.io/docs/circuitbreaker
4. Resilience4J components -
    1. [Circuit Breaker](https://resilience4j.readme.io/docs/circuitbreaker)
    2. [Retry](https://resilience4j.readme.io/docs/retry)
    3. [Rate Limiter](https://resilience4j.readme.io/docs/ratelimiter)