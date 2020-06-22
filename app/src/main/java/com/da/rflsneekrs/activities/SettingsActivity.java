package com.da.rflsneekrs.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.da.rflsneekrs.R;

import com.da.rflsneekrs.authentication.SessionManager;
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
import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings("FieldCanBeLocal")
public class SettingsActivity extends AppCompatActivity {
  private static final String TAG = "SettingsActivity";

  private Button orderHistoryBtn, paymentBtn, deliveryBtn, countryRegionBtn, notificationPrefBtn, helpBtn,  logout;
  private EditText firstNameEt, lastNameEt, emailEt;
  private Spinner genderSp, shoeSp;
  private ImageView imageProfile;
  private ImageButton uploadImg;
  private FirebaseAuth auth;
  private StorageReference storageReference;
  private SessionManager userSession;

  private final int TAKE_IMAGE_CODE = 1000;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    // initialize Firebase authorization
    auth = FirebaseAuth.getInstance();
    storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseUser firebaseUser = auth.getCurrentUser();
    // Initialize user session
    userSession = new SessionManager(SettingsActivity.this);
  // Initialize components for this activity
    initialize();
    // open camera when button upload image is clicked and capture the picture
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
    // get the user picture if exist on firebase, otherwise load a default picture
    assert firebaseUser != null;
    if (firebaseUser.getPhotoUrl() != null) {
    Picasso.get()
        .load(Objects.requireNonNull(auth.getCurrentUser()).getPhotoUrl())
        .error(R.drawable.avatar)
        .into(imageProfile);
    } else {
      String imgUri = "http://davideagosti.co.uk/wp-content/uploads/2020/06/avatar.png";
      Picasso.get().load(imgUri).error(R.drawable.avatar).into(imageProfile);
    }

    // Handle the gender when selected
    genderSp.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userSession.setGender(position);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) { }
    });
    // Handle the shoes size when selected
    shoeSp.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userSession.setShoesSize(position);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) { }
    });

    // Map user details on the session
    HashMap<String, String> userDetails = userSession.getUserDetails();
    // Fill editor text with user data
    firstNameEt.setText(userDetails.get(SessionManager.KEY_FIRST_NAME));
    lastNameEt.setText(userDetails.get(SessionManager.KEY_LAST_NAME));
    emailEt.setText(userDetails.get(SessionManager.KEY_EMAIL));
    // disable email text input, for not change
    emailEt.setEnabled(false);
    // Create an ArrayAdapter using the string array and a default spinner layout to fill gender spinner
    ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
        R.array.gender_spinner, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
    genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
    genderSp.setAdapter(genderAdapter);
    genderSp.setSelection(userSession.getGender());
    // Create an ArrayAdapter using the string array and a default spinner layout to fill gender spinner
    ArrayAdapter<CharSequence> shoesAdapter = ArrayAdapter.createFromResource(this,
        R.array.shoes_spinner, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
    shoesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
    shoeSp.setAdapter(shoesAdapter);
    shoeSp.setSelection(userSession.getShoesSize());
    // Initialize logout button
    logout = findViewById(R.id.logout_btn);
    // Handle logout button, the user logout when click the logout button
    logout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        logout(v);
      }
    });
    //}

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
        assert data != null;
        Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
        imageProfile.setImageBitmap(bitmap);
        // call method to upload image
        assert bitmap != null;
        uploadImageToFirebase(bitmap);
      }
    }
  }

  // Method used to Upload image in database
  private void uploadImageToFirebase(Bitmap bitmap) {
    // upload image to firebase storage
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    String uid = auth.getUid();
    // save the image in profileImage folder with userId at the file name
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

  // download the image from database
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

  // set the image user profile
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
            Toast.makeText(SettingsActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Toast.makeText(SettingsActivity.this, "Profile image failed...", Toast.LENGTH_SHORT).show();
          }
        });
  }

  // Initialize all components
  private void initialize() {
    firstNameEt = findViewById(R.id.firstname_edt_text);
    lastNameEt = findViewById(R.id.lastname_edt_text);
    emailEt = findViewById(R.id.email_edt_text);
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

  // logout method
  private void logout(View v) {
    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
    //set title
    builder.setTitle("ARE YOU SURE?");
    // set message
    builder.setMessage("Are you sure you want to log out of SneekRs?");
    // set cancel button
    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
      }
    });
    // set positive button
    builder.setPositiveButton("LOG OUT", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        // Clear userSession
        userSession.setLogout();
        // redirect activity
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK));
        startActivity(intent);
      }
    });
    // initialize alert dialog
    AlertDialog alertLogoOut = builder.create();
    // show alert dialog
    alertLogoOut.show();
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