package com.guptaji.microservice.UserMicroservice.repositories;

import com.guptaji.microservice.UserMicroservice.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {}
