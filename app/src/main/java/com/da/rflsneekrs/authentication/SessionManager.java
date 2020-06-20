package com.da.rflsneekrs.authentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
  // Initialize variable
  SharedPreferences sharedPreferences;
  SharedPreferences.Editor editor;

  public static final String KEY_LOGIN = "KEY_LOGIN";

  public static final String KEY_FIRST_NAME = "KEY_FIRST_NAME";
  public static final String KEY_LAST_NAME = "KEY_LAST_NAME";
  public static final String KEY_EMAIL = "KEY_EMAIL";
  public static final String KEY_PREFERENCES = "KEY_PREFERENCES";
  public static final String KEY_COUNTRY = "KEY_COUNTRY";

  // create a constructor
  public SessionManager(Context context) {
    sharedPreferences = context.getSharedPreferences("AppSessionKey", Context.MODE_PRIVATE);
    editor = sharedPreferences.edit();
    editor.apply();
  }

  // Create set login method
  public void setLogin(boolean login){
    editor.putBoolean(KEY_LOGIN, login);
    editor.commit();
  }

  // Create get login method
  public boolean getLogin(){
    return sharedPreferences.getBoolean(KEY_LOGIN, false);
  }

  public void setUserDetails(String firstName, String lastName, String email, String preferences, String country) {
    editor.putString(KEY_FIRST_NAME, firstName);
    editor.putString(KEY_LAST_NAME, lastName);
    editor.putString(KEY_EMAIL, email);
    editor.putString(KEY_PREFERENCES, preferences);
    editor.putString(KEY_COUNTRY, country);

    editor.commit();
  }

  public HashMap<String, String> getUserDetails() {
    HashMap<String, String> userdata = new HashMap<String, String>();

    userdata.put(KEY_FIRST_NAME, sharedPreferences.getString(KEY_FIRST_NAME, null));
    userdata.put(KEY_LAST_NAME, sharedPreferences.getString(KEY_LAST_NAME, null));
    userdata.put(KEY_EMAIL, sharedPreferences.getString(KEY_EMAIL, null));
    userdata.put(KEY_PREFERENCES, sharedPreferences.getString(KEY_PREFERENCES, null));
    userdata.put(KEY_COUNTRY, sharedPreferences.getString(KEY_COUNTRY, null));

    return userdata;
  }

  // Create set login method
  public void setNotification(boolean notification){
    editor.putBoolean("KEY_NOTIFICATION", notification);
    editor.commit();
  }

  // Create get login method
  public boolean getNotification(){
    return sharedPreferences.getBoolean("KEY_NOTIFICATION", false);
  }

  public void setLogout(){
    editor.clear();
    editor.commit();
  }
}
