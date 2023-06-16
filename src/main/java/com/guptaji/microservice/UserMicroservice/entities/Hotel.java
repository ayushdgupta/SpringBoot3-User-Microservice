package com.guptaji.microservice.UserMicroservice.entities;

public class Hotel {

  private int hotelId;
  private String name;
  private String location;
  private String about;
  private String owner;

  public Hotel() {
    // Default constructor
  }

  public Hotel(int hotelId, String name, String location, String about, String owner) {
    this.hotelId = hotelId;
    this.name = name;
    this.location = location;
    this.about = about;
    this.owner = owner;
  }

  public int getHotelId() {
    return hotelId;
  }

  public void setHotelId(int hotelId) {
    this.hotelId = hotelId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getAbout() {
    return about;
  }

  public void setAbout(String about) {
    this.about = about;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  @Override
  public String toString() {
    return "Hotel{"
        + "hotelId="
        + hotelId
        + ", name='"
        + name
        + '\''
        + ", location='"
        + location
        + '\''
        + ", about='"
        + about
        + '\''
        + ", owner='"
        + owner
        + '\''
        + '}';
  }
}
