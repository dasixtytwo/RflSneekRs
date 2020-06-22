package com.da.rflsneekrs.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.da.rflsneekrs.R;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Objects;

@SuppressWarnings("FieldCanBeLocal")
public class ProductDetailActivity extends AppCompatActivity {

  private FirebaseDatabase fbDatabase;
  private ImageView imgProduct;
  private TextView txtViewName, txtViewBrand, txtViewSecondaryName, txtViewSecondaryBrand, txtViewPrice, txtViewDescription;
  private ImageButton imgShare, imgFavorite;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_detail);
    // Initialize components
    initialize();
    // Instantiate the Firebase
    fbDatabase = FirebaseDatabase.getInstance();

    // now we need to bind all data into those views
    // first we need to get product data
    // we need to send product detail data to this activity first ...
    // now we can get product data
    // ImageView on activity product detail
    String productImage = Objects.requireNonNull(getIntent().getExtras()).getString("image");
    Picasso.get().load(productImage).into(imgProduct);
    // TextView on activity product detail
    final String productName = getIntent().getExtras().getString("name");
    txtViewName.setText(productName);
    String productBrand = getIntent().getExtras().getString("brand");
    txtViewBrand.setText(productBrand);
    String productSecName = getIntent().getExtras().getString("name");
    txtViewSecondaryName.setText(productSecName);
    String productSecBrand = getIntent().getExtras().getString("brand");
    txtViewSecondaryBrand.setText(productSecBrand);
    String productPrice = getIntent().getExtras().getString("price");
    txtViewPrice.setText(String.valueOf(productPrice));
    String productDescription = getIntent().getExtras().getString("description");
    txtViewDescription.setText(productDescription);
    // Handle the share button
    imgShare.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, productName);
        sharingIntent.setType("text/*");
        Intent shareIntent = Intent.createChooser(sharingIntent, "Share via");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
      }
    });

    // Check if the action bar is displayed if so, set the back arrow and the title.
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(productName);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }
  // Initialize all components
  private void initialize() {
    imgProduct = findViewById(R.id.product_detail_img);
    txtViewName = findViewById(R.id.product_detail_name);
    txtViewBrand = findViewById(R.id.product_detail_brand);
    txtViewSecondaryName = findViewById(R.id.product_detail_name_bottom);
    txtViewSecondaryBrand = findViewById(R.id.product_detail_brand_bottom);
    txtViewPrice = findViewById(R.id.product_detail_price);
    txtViewDescription = findViewById(R.id.product_detail_description);
    imgShare = findViewById(R.id.share_btn_detail);
    imgFavorite = findViewById(R.id.favorite_btn_detail);
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