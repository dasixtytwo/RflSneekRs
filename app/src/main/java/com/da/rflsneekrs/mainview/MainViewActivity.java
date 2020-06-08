package com.da.rflsneekrs.mainview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import android.widget.Button;

import com.da.rflsneekrs.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class MainViewActivity extends AppCompatActivity {
  private FirebaseAuth auth;

  private BottomNavigationView bottomNavigationView;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_view);
    auth = FirebaseAuth.getInstance();

    initializeViews();

    bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
    getSupportFragmentManager().beginTransaction().replace(R.id.fgContainer, new HomeFragment()).commit();
  }

  private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
      Fragment fragment = null;

      switch (menuItem.getItemId())
      {
        case R.id.home:
          fragment = new HomeFragment();
          break;

        case R.id.discover:
          fragment = new DiscoverFragment();
          break;

        case R.id.inbox:
          fragment = new InboxFragment();
          break;

        case R.id.profile:
          fragment = new ProfileFragment();
          break;
      }
      getSupportFragmentManager().beginTransaction().replace(R.id.fgContainer, fragment).commit();

      return true;
    }
  };

  private void initializeViews() {
    bottomNavigationView = findViewById(R.id.bottom_navigation);
  }
}