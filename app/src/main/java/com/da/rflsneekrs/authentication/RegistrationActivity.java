package com.da.rflsneekrs.authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.da.rflsneekrs.R;
import com.da.rflsneekrs.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
@SuppressWarnings("FieldCanBeLocal")
public class RegistrationActivity extends AppCompatActivity {
  private FirebaseAuth auth;
  private FirebaseDatabase fbDatabase;
  private DatabaseReference dbReference;

  private EditText emailEt, passwordEt, firstNameEt, lastNameEt, nationalityEt;
  private CheckBox notificationCb;
  private RadioButton radioManPref, radioWomanPref;
  private Button signUpBtn;
  private TextView loginBtn;
  private ProgressBar progressBar;
  private String productPreference = "";
  private Boolean notification = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_registration);
    // Hide the actionbar
    if (getSupportActionBar() != null) {
      getSupportActionBar().hide(); //hide title bar
    }

    // Initialize Firebase
    auth = FirebaseAuth.getInstance();
    fbDatabase = FirebaseDatabase.getInstance();
    dbReference = fbDatabase.getReference("Users");
    //
    initializeViews();
    // set handle sign up button
    signUpBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        registerNewUser();
      }
    });
    setupHyperLink();
    // set redirect to login activity if button is clicked
    loginBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
      }
    });
  }

  // setting the link on the activity
  private void setupHyperLink() {
    TextView hlTextView = findViewById(R.id.activity_main_link);
    hlTextView.setMovementMethod(LinkMovementMethod.getInstance());
  }
  // method to save new user on database
  private void registerNewUser() {
    progressBar.setVisibility(View.VISIBLE);

    final String email, firstName, lastName, nationality;
    email = emailEt.getText().toString();
    firstName = firstNameEt.getText().toString();
    lastName = lastNameEt.getText().toString();
    nationality = nationalityEt.getText().toString();
    String password = passwordEt.getText().toString();

    if (TextUtils.isEmpty(email)) {
      Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
      return;
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      emailEt.setError(getString(R.string.input_error_email_invalid));
      emailEt.requestFocus();
      return;
    }

    if (TextUtils.isEmpty(password)) {
      Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
      return;
    }

    if(password.length() < 6){
      Toast.makeText(getApplicationContext(),"Password must be at least 6 characters",Toast.LENGTH_SHORT).show();
    }

    if(radioManPref.isChecked()) {
      productPreference = "Man";
    }
    if(radioWomanPref.isChecked()) {
      productPreference = "Woman";
    }
    if(notificationCb.isChecked()) {
      notification = true;
    }

    auth.createUserWithEmailAndPassword(email,password)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {

            if(task.isSuccessful()){
              User user = new User(
                  email,
                  firstName,
                  lastName,
                  productPreference,
                  nationality,
                  notification
              );
              // save new user into database
              dbReference.child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                  .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                  Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_LONG).show();
                  progressBar.setVisibility(View.GONE);
                  // redirect the user at the login activity
                  Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                  startActivity(intent);
                  finish();
                }
              });
            }
            else{
              Toast.makeText(RegistrationActivity.this,"E-mail or password is wrong", Toast.LENGTH_SHORT).show();
              progressBar.setVisibility(View.GONE);
            }
          }
        });
  }
  // method to initialize all activity components
  private void initializeViews() {
    emailEt = findViewById(R.id.email_edt_text);
    passwordEt = findViewById(R.id.pass_edt_text);
    firstNameEt = findViewById(R.id.firstname_edt_text);
    lastNameEt = findViewById(R.id.lastname_edt_text);
    nationalityEt = findViewById(R.id.nationality_edt_text);
    radioManPref = findViewById(R.id.rbMens);
    radioWomanPref = findViewById(R.id.rbWomens);
    notificationCb = findViewById(R.id.notifyCB);
    signUpBtn = findViewById(R.id.signup_btn);
    loginBtn = findViewById(R.id.login_btn);
    progressBar = findViewById(R.id.progressBar);
  }
}