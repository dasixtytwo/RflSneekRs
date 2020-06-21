package com.da.rflsneekrs.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.da.rflsneekrs.R;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class SettingsActivity extends AppCompatActivity {
  private static final String TAG = "SettingsActivity";

  Button orderHistoryBtn, paymentBtn, deliveryBtn, countryRegionBtn, notificationPrefBtn, helpBtn,  logout;
  EditText firstNameTv, lastNameTv, emailTv;
  Spinner genderSp, shoeSp;
  ImageView imageProfile;
  ImageButton uploadImg;
  private FirebaseAuth auth;
  private StorageReference storageReference;

  private final int TAKE_IMAGE_CODE = 1000;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    auth = FirebaseAuth.getInstance();
    storageReference = FirebaseStorage.getInstance().getReference();

    initialize();

    uploadImg.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // take picture from camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
          startActivityForResult(intent, TAKE_IMAGE_CODE);
        }

      }
    });

    if (auth.getCurrentUser().getPhotoUrl() != null) {
      Picasso.get()
          .load(auth.getCurrentUser().getPhotoUrl())
          .into(imageProfile);
    } else {
      String imgUri = "http://davideagosti.co.uk/wp-content/uploads/2020/06/avatar.png";
      Picasso.get().load(imgUri).error(R.drawable.avatar).into(imageProfile);
    }

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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == TAKE_IMAGE_CODE){
      if(resultCode == Activity.RESULT_OK){
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        imageProfile.setImageBitmap(bitmap);
        uploadImageToFirebase(bitmap);
      }
    }
  }

  private void uploadImageToFirebase(Bitmap bitmap) {
    // upload image to firebase storage
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    String uid = auth.getUid();
    final StorageReference reference = storageReference
        .child("profileImages")
        .child(uid + ".jpeg");

    reference.putBytes(baos.toByteArray())
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
          @Override
          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            getDownloadUrl(reference);
          }
        }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        //Toast.makeText(SettingsActivity.this, "Something Wrong!", Toast.LENGTH_LONG).show();
        Log.e(TAG, "onFailure: ", e.getCause());
      }
    });
  }

  private void getDownloadUrl(StorageReference reference){
    reference.getDownloadUrl()
        .addOnSuccessListener(new OnSuccessListener<Uri>() {
          @Override
          public void onSuccess(Uri uri) {
            Toast.makeText(SettingsActivity.this, "Profile image Uploaded on: " + uri, Toast.LENGTH_LONG).show();
            Log.d(TAG, "onSuccess: "+ uri);
            setUserProfileUrl(uri);
          }
        });
  }

  private void setUserProfileUrl(Uri uri) {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
        .setPhotoUri(uri)
        .build();

    assert user != null;
    user.updateProfile(request)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            Toast.makeText(SettingsActivity.this, "Updated succesfully", Toast.LENGTH_SHORT).show();
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Toast.makeText(SettingsActivity.this, "Profile image failed...", Toast.LENGTH_SHORT).show();
          }
        });
  }

  private void initialize() {
    firstNameTv = findViewById(R.id.firstname_edt_text);
    lastNameTv = findViewById(R.id.lastname_edt_text);
    emailTv = findViewById(R.id.email_edt_text);
    genderSp = findViewById(R.id.gender_spinner);
    shoeSp = findViewById(R.id.shoe_size_spinner);
    orderHistoryBtn = findViewById(R.id.order_history_btn);
    paymentBtn = findViewById(R.id.payment_info_btn);
    deliveryBtn = findViewById(R.id.delivery_info_btn);
    countryRegionBtn = findViewById(R.id.country_region_btn);
    notificationPrefBtn = findViewById(R.id.notification_pref_btn);
    helpBtn = findViewById(R.id.help_btn);
    uploadImg = findViewById(R.id.upload_pictures_iv);
    imageProfile = findViewById(R.id.settingProfileImg);
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