package com.da.rflsneekrs.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.da.rflsneekrs.R;
import com.da.rflsneekrs.authentication.LoginActivity;
import com.da.rflsneekrs.authentication.RegistrationActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

  Button registerBtn, loginBtn;
  TextView guestBtn;
  FirebaseAuth auth;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportActionBar().hide(); //hide title bar

    auth = FirebaseAuth.getInstance();

    initializeViews();

    if (auth.getCurrentUser() != null){
      Intent intent = new Intent(MainActivity.this, MainViewActivity.class);
      startActivity(intent);
    }

    registerBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
        startActivity(intent);
      }
    });

    loginBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
      }
    });

    guestBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, MainViewActivity.class);
        startActivity(intent);
      }
    });
  }

  // Block the user to come back at the previous activity
  @Override
  public void onBackPressed() {
    //super.onBackPressed();
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_HOME);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
    finishAffinity();
  }

  private void initializeViews() {
    registerBtn = findViewById(R.id.register);
    loginBtn = findViewById(R.id.login);
    guestBtn = findViewById(R.id.tvGuest);
  }
}