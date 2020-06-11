package com.da.rflsneekrs.models;

public class Product {
  public String name, brand, description, image, gender;
  public Double price;
  public Boolean inStock;

  public Product(String name, String brand, String description, Boolean inStock, String image, String gender, Double price){
    this.name = name;
    this.brand = brand;
    this.description = description;
    this.gender = gender;
    this.image = image;
    this.inStock = inStock;
    this.price = price;
  }

  // empty constructor
  public Product() {}

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public String getBrand() {
    return brand;
  }
  public void setBrand(String brand) {
    this.name = brand;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.name = description;
  }

  public String getGender() {
    return gender;
  }
  public void setGender(String gender) {
    this.name = gender;
  }

  public String getImage() {
    return image;
  }
  public void setImage(String image) {
    this.name = image;
  }

  public Boolean getInStock() {
    return inStock;
  }
  public void setInStock(String inStock) {
    this.name = inStock;
  }

  public Double getPrice() {
    return price;
  }
  public void setPrice(String price) {
    this.name = price;
  }
}
