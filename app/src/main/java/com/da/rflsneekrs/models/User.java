package com.da.rflsneekrs.models;

public class User {
  public String email, firstName, lastName, nationality, productPreference;
  public Boolean notification;

  // empty constructor
  public User(){}

  public User(String email, String firstName, String lastName, String nationality, String productPreference, Boolean notification){
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.nationality = nationality;
    this.productPreference = productPreference;
    this.notification = notification;
  }

  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }

  public String getNationality() {
    return nationality;
  }
  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  public String getProductPreference() {
    return productPreference;
  }
  public void setProductPreference(String productPreference) {
    this.productPreference = productPreference;
  }

  public Boolean getNotification() {
    return notification;
  }
  public void setNotification(Boolean notification) {
    this.notification = notification;
  }
}
