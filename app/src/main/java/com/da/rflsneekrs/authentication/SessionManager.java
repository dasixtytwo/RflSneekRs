package com.da.rflsneekrs.authentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.da.rflsneekrs.models.User;

import java.util.HashMap;

public class SessionManager {
  // Initialize variable
  SharedPreferences sharedPreferences;
  SharedPreferences.Editor editor;

  public static final String SESSION_KEY = "UserSession";
  public static final String KEY_LOGIN = "IdLogin";
  public static final String KEY_FIRST_NAME = "FirstName";
  public static final String KEY_LAST_NAME = "LastName";
  public static final String KEY_EMAIL = "Email";
  public static final String KEY_PREFERENCES = "Preferences";
  public static final String KEY_COUNTRY = "Country";
  public static final String KEY_NOTIFICATION = "Notification";

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

  // Create get Notification method
  public boolean getNotification(){
    return sharedPreferences.getBoolean(KEY_NOTIFICATION, false);
  }

  // clear user session
  public void setLogout(){
    editor.clear();
    editor.commit();
  }

}
