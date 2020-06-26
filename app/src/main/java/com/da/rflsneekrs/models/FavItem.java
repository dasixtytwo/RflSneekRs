package com.da.rflsneekrs.models;

public class FavItem {
  private String keyId;
  private String imageUrl;
  private int favStatus;

  public FavItem() {
  }

  public FavItem(String imageUrl, String keyId, int favStatus ) {
    this.keyId = keyId;
    this.imageUrl = imageUrl;
    this.favStatus = favStatus;
  }

  public String getImageUri() {
    return imageUrl;
  }

  public void setImageUri(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getKeyId() {
    return keyId;
  }

  public void setKeyId(String keyId) {
    this.keyId = keyId;
  }

  public int getFavStatus() {
    return favStatus;
  }

  public void setFavStatus(int favStatus) {
    this.favStatus = favStatus;
  }
}
