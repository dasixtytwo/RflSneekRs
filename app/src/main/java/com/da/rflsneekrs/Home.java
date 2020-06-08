package com.da.rflsneekrs;

import android.app.Application;
import android.content.Intent;

import com.da.rflsneekrs.mainview.MainViewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends Application {
  @Override
  public void onCreate() {
    super.onCreate();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser fbUser = auth.getCurrentUser();

    if(fbUser != null){
      startActivity(new Intent(Home.this, MainViewActivity.class));
    }
  }
}
