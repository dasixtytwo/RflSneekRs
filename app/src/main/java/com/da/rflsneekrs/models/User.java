package com.da.rflsneekrs.models;

public class User {
  public String email, firstName, lastName, nationality, productPreference;
  public Boolean notification;

  public User(){}

  public User(String email, String firstName, String lastName, String nationality, String productPreference, Boolean notification){
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.nationality = nationality;
    this.productPreference = productPreference;
    this.notification = notification;
  }
}
