package com.da.rflsneekrs.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.da.rflsneekrs.MainActivity;
import com.da.rflsneekrs.R;
import com.da.rflsneekrs.mainview.MainViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

  private FirebaseAuth auth;

  private EditText emailEt, passwordEt;
  private Button loginBtn;
  private TextView resetPasswordTv, signupTv;
  private ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    getSupportActionBar().hide(); //hide title bar

    auth = FirebaseAuth.getInstance();

    initializeViews();

    loginBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        loginUserAccount();
      }
    });

    signupTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(),RegistrationActivity.class);
        startActivity(intent);
        finish();
      }
    });

    resetPasswordTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
        startActivity(intent);
      }
    });

    /*if(auth.getCurrentUser()!=null){
      startActivity(new Intent(getApplicationContext(),MainViewActivity.class));
    }*/
  }

  private void loginUserAccount() {
    progressBar.setVisibility(View.VISIBLE);

    String email, password;
    email = emailEt.getText().toString();
    password = passwordEt.getText().toString();

    if (TextUtils.isEmpty(email)) {
      Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
      return;
    }

    if (TextUtils.isEmpty(password)) {
      Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
      return;
    }

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
              progressBar.setVisibility(View.GONE);

              Intent intent = new Intent(LoginActivity.this, MainViewActivity.class);
              startActivity(intent);
            }
            else {
              Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
              progressBar.setVisibility(View.GONE);
            }
          }
        });
  }

  private void initializeViews() {
    emailEt = findViewById(R.id.email_edt_text);
    passwordEt = findViewById(R.id.pass_edt_text);
    loginBtn = findViewById(R.id.login_btn);
    signupTv = findViewById(R.id.signup_Tv);
    resetPasswordTv = findViewById(R.id.reset_pass_tv);
    progressBar = findViewById(R.id.progressBar);
  }
}