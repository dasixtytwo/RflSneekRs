package com.da.rflsneekrs.models;

public class FavItem {
  private String keyId, name, brand, description, imageUrl;
  private Double price;
  private int favStatus;

  public FavItem() {
  }

  public FavItem(String keyId, String name, String brand, String description, String imageUrl, Double price, int favStatus){
    this.keyId = keyId;
    this.name = name;
    this.brand = brand;
    this.description = description;
    this.imageUrl = imageUrl;
    this.price = price;
    this.favStatus = favStatus;
  }

  public String getKeyId() {
    return keyId;
  }
  public void setKeyId(String keyId) {
    this.keyId = keyId;
  }

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
    this.brand = brand;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public String getImageUri() {
    return imageUrl;
  }
  public void setImageUri(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Double getPrice() {
    return price;
  }
  public void setPrice(Double price) {
    this.price = price;
  }

  public int getFavStatus() {
    return favStatus;
  }
  public void setFavStatus(int favStatus) {
    this.favStatus = favStatus;
  }
}
