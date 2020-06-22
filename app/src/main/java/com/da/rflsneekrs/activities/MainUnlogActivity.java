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

public class MainUnlogActivity extends AppCompatActivity {
  private Button registerBtn, loginBtn, closeBtn;
  private TextView guestBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_unlog);
    // Hide the actionbar
    if (getSupportActionBar() != null) {
      getSupportActionBar().hide(); //hide title bar
    }
    // Initialize components
    initialize();
    // handle the close button, close activity when close button is clicked
    closeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainUnlogActivity.this, MainViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
      }
    });
    // redirect to register activity when register button is clicked
    registerBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainUnlogActivity.this, RegistrationActivity.class);
        startActivity(intent);
      }
    });
    // redirect to login activity when login button is clicked
    loginBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainUnlogActivity.this, LoginActivity.class);
        startActivity(intent);
      }
    });
    // redirect to main activity when guest button is clicked
    guestBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainUnlogActivity.this, MainViewActivity.class);
        startActivity(intent);
      }
    });
  }

  // Block the user to come back at the previous activity
  @Override
  public void onBackPressed() {
    //super.onBackPressed();
    Intent intent = new Intent(getApplicationContext(), MainViewActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    finish();
  }
  // Initialize all components for this activity
  private void initialize() {
    registerBtn = findViewById(R.id.register);
    loginBtn = findViewById(R.id.login);
    guestBtn = findViewById(R.id.tvGuest);
    closeBtn = findViewById(R.id.closeBtn);
  }
}