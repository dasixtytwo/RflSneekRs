package com.da.rflsneekrs.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import com.da.rflsneekrs.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

  Button logout;
  FirebaseAuth auth;
  TabLayout tabLayout;
  ImageButton uploadImg;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    auth = FirebaseAuth.getInstance();
    uploadImg = findViewById(R.id.upload_pictures_iv);

    uploadImg.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
              }
            });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ACCEPT",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Button ACCEPT presses!", Toast.LENGTH_LONG).show();
              }
            });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DECLINE",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Button DECLINE presses!", Toast.LENGTH_LONG).show();
              }
            });
        alertDialog.show();
      }
    });

    if(auth.getCurrentUser() == null){
      Intent intent = new Intent(SettingsActivity.this, MainUnlogActivity.class);
      startActivity(intent);
    } else {
      logout = findViewById(R.id.logout_btn);
      logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          logout();
        }
      });
    }

    // Check if the action bar is displayed if so, set the back arrow and the title.
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle("SETTINGS");
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }

  private void logout() {
    if (auth.getCurrentUser() != null) {
      auth.signOut();
    }

    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.setFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK));
    startActivity(intent);
  }

  // This method is used to the back arrow on application bar
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }
}