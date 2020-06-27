package com.da.rflsneekrs.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.da.rflsneekrs.models.User;

import java.util.HashMap;
import static com.da.rflsneekrs.adapters.ListGridAdapter.SPAN_COUNT_ONE;
import static com.da.rflsneekrs.adapters.ListGridAdapter.SPAN_COUNT_TWO;

public class SessionManager {
  // Initialize variable
  SharedPreferences sharedPreferences;
  SharedPreferences.Editor editor;

  // declare name of session
  public static final String SESSION_KEY = "UserSession";
  // declare login session name variable
  public static final String KEY_LOGIN = "IdLogin";
  // declare variable for setting
  public static final String KEY_FIRST_NAME = "FirstName";
  public static final String KEY_LAST_NAME = "LastName";
  public static final String KEY_EMAIL = "Email";
  public static final String KEY_PREFERENCES = "Preferences";
  public static final String KEY_COUNTRY = "Country";
  public static final String KEY_NOTIFICATION = "Notification";
  public static final String KEY_GENDER = "Gender";
  public static final String KEY_SHOES_SIZE = "ShoesSize";
  // declare variable for list/grid preferences
  public static final String KEY_LIST_GRID = "ListGrid";
  public static final String KEY_LIST_GRID_STOCK = "InStockListGrid";
  public static final String KEY_ICON = "IconListGrid";
  public static final String KEY_ICON_STOCK = "IconListGridStock";
  // declare variable for favourite
  public static final String KEY_FAV_STATUS = "FavouriteStatus";

  // create a constructor
  @SuppressLint("CommitPrefEdits")
  public SessionManager(Context context) {
    sharedPreferences = context.getSharedPreferences(SESSION_KEY, Context.MODE_PRIVATE);
    editor = sharedPreferences.edit();
  }

  // Create set login method
  public void setLogin(String login){
    editor.putString(KEY_LOGIN, login);
    editor.commit();
  }
  // Create get login method
  public String getLogin(){
    return sharedPreferences.getString(KEY_LOGIN, null);
  }

  // Set user session
  public void setUserDetails(User user) {
    // Initialize user
    String firstName = user.getFirstName();
    String lastName = user.getLastName();
    String email = user.getEmail();
    String preferences = user.getProductPreference();
    String country = user.getNationality();

    // save user detail into user session
    editor.putString(KEY_FIRST_NAME, firstName);
    editor.putString(KEY_LAST_NAME, lastName);
    editor.putString(KEY_EMAIL, email);
    editor.putString(KEY_PREFERENCES, preferences);
    editor.putString(KEY_COUNTRY, country);

    editor.commit();
  }
  // Get user data from user session
  public HashMap<String, String> getUserDetails() {
    HashMap<String, String> userdata = new HashMap<String, String>();

    userdata.put(KEY_FIRST_NAME, sharedPreferences.getString(KEY_FIRST_NAME, null));
    userdata.put(KEY_LAST_NAME, sharedPreferences.getString(KEY_LAST_NAME, null));
    userdata.put(KEY_EMAIL, sharedPreferences.getString(KEY_EMAIL, null));
    userdata.put(KEY_PREFERENCES, sharedPreferences.getString(KEY_PREFERENCES, null));
    userdata.put(KEY_COUNTRY, sharedPreferences.getString(KEY_COUNTRY, null));

    return userdata;
  }

  // Create set Notification method
  public void setNotification(boolean notification){
    editor.putBoolean(KEY_NOTIFICATION, notification);
    editor.commit();
  }
  // Get Notification method
  public boolean getNotification(){
    return sharedPreferences.getBoolean(KEY_NOTIFICATION, false);
  }

  // Set gender
  public void setGender(int gender){
    editor.putInt(KEY_GENDER, gender);
    editor.commit();
  }
  // Get Gender session
  public int getGender() {
    return sharedPreferences.getInt(KEY_GENDER, 0);
  }

  // Set preference shoes
  public void setShoesSize(int size){
    editor.putInt(KEY_SHOES_SIZE, size);
    editor.commit();
  }
  // Get Shoes preferences
  public int getShoesSize() {
    return sharedPreferences.getInt(KEY_SHOES_SIZE, 0);
  }

  // Set view, list or grid
  public void setListGrid(int grid){
    editor.putInt(KEY_LIST_GRID, grid);
    editor.commit();
  }
  // set view in stock  list/grid default
  public int getListGrid() {
    return sharedPreferences.getInt(KEY_LIST_GRID, SPAN_COUNT_ONE);
  }

  // Set view, list or grid
  public void setListGridStock(int grid){
    editor.putInt(KEY_LIST_GRID_STOCK, grid);
    editor.commit();
  }
  // set view in stock list/grid default
  public int getListGridStock() {
    return sharedPreferences.getInt(KEY_LIST_GRID_STOCK, SPAN_COUNT_TWO);
  }

  // Set button icon for list/grid
  public void setIcon(boolean icon){
    editor.putBoolean(KEY_ICON, icon);
    editor.commit();
  }
  // return the value to the icon button
  public boolean getIcon() {
    return sharedPreferences.getBoolean(KEY_ICON, true);
  }

  // Set button icon for list/grid
  public void setIconStock(boolean icon){
    editor.putBoolean(KEY_ICON_STOCK, icon);
    editor.commit();
  }
  // return the value to the icon button
  public boolean getIconStock() {
    return sharedPreferences.getBoolean(KEY_ICON_STOCK, false);
  }

  // Set icon button favourite color
  public void setIconFav(boolean icon){
    editor.putBoolean(KEY_FAV_STATUS, icon);
    editor.commit();
  }
  // return the value to the icon button
  public boolean getIconFav() {
    return sharedPreferences.getBoolean(KEY_FAV_STATUS, false);
  }

  // clear user session
  public void setLogout(){
    editor.clear();
    editor.commit();
  }

}
