package com.da.rflsneekrs.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.da.rflsneekrs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
  private FirebaseAuth auth;

  private EditText emailEt;
  private Button resetPswBtn;
  private TextView backBtn;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forgot_password);
    getSupportActionBar().hide(); //hide title bar

    auth = FirebaseAuth.getInstance();

    initializeViews();

    resetPswBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        resetNewPassword();
      }
    });

    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  private void resetNewPassword() {
    String email;
    email = emailEt.getText().toString();

    if(TextUtils.isEmpty(email)){
      Toast.makeText(getApplicationContext(),"Please fill email",Toast.LENGTH_SHORT).show();
      return;
    }

    auth.sendPasswordResetEmail(email)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
              Toast.makeText(getApplicationContext(),"Password reset link was sent your email address",Toast.LENGTH_SHORT).show();
            }
            else{
              Toast.makeText(getApplicationContext(),"Mail sending error",Toast.LENGTH_SHORT).show();
            }
          }
        });
  }

  private void initializeViews() {
    emailEt = findViewById(R.id.email_edt_text);
    resetPswBtn = findViewById(R.id.reset_pass_btn);
    backBtn = findViewById(R.id.back_btn);
  }
}