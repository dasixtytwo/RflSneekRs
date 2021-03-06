package com.da.rflsneekrs.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.da.rflsneekrs.R;
import com.da.rflsneekrs.activities.MainViewActivity;
import com.da.rflsneekrs.models.User;
import com.da.rflsneekrs.settings.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
@SuppressWarnings("FieldCanBeLocal")
public class LoginActivity extends AppCompatActivity {

  private FirebaseAuth auth;
  private FirebaseDatabase fbDatabase;
  private DatabaseReference dbReference;
  private SessionManager userSession;

  private EditText emailEt, passwordEt;
  private Button loginBtn;
  private TextView resetPasswordTv, signUpTv;
  private ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    // Hide the actionbar
    if (getSupportActionBar() != null) {
      getSupportActionBar().hide(); //hide title bar
    }
    // instantiate authorization
    auth = FirebaseAuth.getInstance();
    fbDatabase = FirebaseDatabase.getInstance();
    dbReference = fbDatabase.getReference("Users");
    FirebaseUser firebaseUser = auth.getCurrentUser();
    // Initialize session
    userSession = new SessionManager(LoginActivity.this);

    // Initialize all component
    initializeViews();

    // Handle method for login button
    loginBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        loginUserAccount();
      }
    });

    setupHyperLink();

    signUpTv.setOnClickListener(new View.OnClickListener() {
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
  }

  // setting the link on the activity
  private void setupHyperLink() {
    TextView hlTextView = findViewById(R.id.activity_main_link);
    hlTextView.setMovementMethod(LinkMovementMethod.getInstance());
  }

  // Method for login
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
              // call getUser method
              getUser();
              // display a message for successful login
              Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
              progressBar.setVisibility(View.GONE);
              // close the login activity and redirect to main view activity
              Intent intent = new Intent(LoginActivity.this, MainViewActivity.class);
              startActivity(intent);
              finish();
            }
            else {
              Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
              progressBar.setVisibility(View.GONE);
            }
          }
        });
  }

  // Get user method
  private void getUser() {
    final String UID = auth.getUid();
    dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot userSnapShot) {
        assert UID != null;
        User user = userSnapShot.child(UID).getValue(User.class);
        // save user into userSession
        userSession.setLogin(UID);
        assert user != null;
        userSession.setUserDetails(user);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
      }
    });
  }

  // method to initialize all activity components
  private void initializeViews() {
    emailEt = findViewById(R.id.email_edt_text);
    passwordEt = findViewById(R.id.pass_edt_text);
    loginBtn = findViewById(R.id.login_btn);
    signUpTv = findViewById(R.id.signup_Tv);
    resetPasswordTv = findViewById(R.id.reset_pass_tv);
    progressBar = findViewById(R.id.progressBar);
  }
}