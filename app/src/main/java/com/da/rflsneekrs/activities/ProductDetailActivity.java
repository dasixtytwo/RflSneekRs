package com.da.rflsneekrs.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.da.rflsneekrs.R;

public class ProductDetailActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_detail);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle("Product title");
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
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