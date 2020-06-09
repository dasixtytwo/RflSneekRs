package com.da.rflsneekrs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.da.rflsneekrs.authentication.LoginActivity;
import com.da.rflsneekrs.authentication.RegistrationActivity;
import com.da.rflsneekrs.mainview.MainViewActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainUnlogActivity extends AppCompatActivity {
  Button registerBtn, loginBtn;
  TextView guestBtn;
  FirebaseAuth auth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_unlog);
    getSupportActionBar().hide(); //hide title bar

    initialize();

    registerBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainUnlogActivity.this, RegistrationActivity.class);
        startActivity(intent);
      }
    });

    loginBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainUnlogActivity.this, LoginActivity.class);
        startActivity(intent);
      }
    });

    guestBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainUnlogActivity.this, MainViewActivity.class);
        startActivity(intent);
      }
    });
  }

  private void initialize() {
    registerBtn = findViewById(R.id.register);
    loginBtn = findViewById(R.id.login);
    guestBtn = findViewById(R.id.tvGuest);
  }
}