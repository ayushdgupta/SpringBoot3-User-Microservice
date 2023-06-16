package com.guptaji.microservice.UserMicroservice.entities;

public class Rating {

  private int ratingId;
  private int userId;
  private int hotelId;
  private int rating;
  private String feedback;
  private Hotel hotel;

  public Rating() {
    // Default constructor
  }

  public Rating(int ratingId, int userId, int hotelId, int rating, String feedback, Hotel hotel) {
    this.ratingId = ratingId;
    this.userId = userId;
    this.hotelId = hotelId;
    this.rating = rating;
    this.feedback = feedback;
    this.hotel = hotel;
  }

  public int getRatingId() {
    return ratingId;
  }

  public void setRatingId(int ratingId) {
    this.ratingId = ratingId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getHotelId() {
    return hotelId;
  }

  public void setHotelId(int hotelId) {
    this.hotelId = hotelId;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public String getFeedback() {
    return feedback;
  }

  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }

  public Hotel getHotel() {
    return hotel;
  }

  public void setHotel(Hotel hotel) {
    this.hotel = hotel;
  }

  @Override
  public String toString() {
    return "Rating{"
        + "ratingId="
        + ratingId
        + ", userId="
        + userId
        + ", hotelId="
        + hotelId
        + ", rating="
        + rating
        + ", feedback='"
        + feedback
        + '\''
        + ", hotel="
        + hotel
        + '}';
  }
}
