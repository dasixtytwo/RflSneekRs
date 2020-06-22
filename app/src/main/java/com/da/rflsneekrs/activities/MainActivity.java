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
@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity {

  private Button registerBtn, loginBtn;
  private TextView guestBtn;
  private FirebaseAuth auth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    // Hide the actionbar
    if (getSupportActionBar() != null) {
      getSupportActionBar().hide(); //hide title bar
    }
    // instantiate authorization
    auth = FirebaseAuth.getInstance();
    // Initialize components
    initializeViews();
    // if current user is logged in will be redirect to main view activity
    if (auth.getCurrentUser() != null){
      Intent intent = new Intent(MainActivity.this, MainViewActivity.class);
      startActivity(intent);
    }
    // set the login button to redirect at the register activity
    registerBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
        startActivity(intent);
      }
    });
    // set the login button to redirect at the login activity
    loginBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
      }
    });
    // set the guest button to redirect the right activity
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
  // method to initialize all activity components
  private void initializeViews() {
    registerBtn = findViewById(R.id.register);
    loginBtn = findViewById(R.id.login);
    guestBtn = findViewById(R.id.tvGuest);
  }
}