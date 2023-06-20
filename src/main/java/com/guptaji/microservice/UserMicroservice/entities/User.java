package com.guptaji.microservice.UserMicroservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.util.List;

@Entity
public class User {

  @Id private int userId;
  private String name;
  private String email;
  private String about;
  private String aadhar;

  // Because we don't want to store this field into our DB so that's why we provided annotation
  // '@Transient'.
  @Transient private List<Rating> ratings;

  public User() {
    // Default constructor
  }

  public User(
      int userId, String name, String email, String about, String aadhar, List<Rating> ratings) {
    this.userId = userId;
    this.name = name;
    this.email = email;
    this.about = about;
    this.aadhar = aadhar;
    this.ratings = ratings;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAbout() {
    return about;
  }

  public void setAbout(String about) {
    this.about = about;
  }

  public String getAadhar() {
    return aadhar;
  }

  public void setAadhar(String aadhar) {
    this.aadhar = aadhar;
  }

  public List<Rating> getRatings() {
    return ratings;
  }

  public void setRatings(List<Rating> ratings) {
    this.ratings = ratings;
  }
}
