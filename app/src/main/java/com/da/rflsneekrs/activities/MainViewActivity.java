package com.da.rflsneekrs.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.da.rflsneekrs.R;
import com.da.rflsneekrs.helper.ScrollNavigationBar;
import com.da.rflsneekrs.fragments.DiscoverFragment;
import com.da.rflsneekrs.fragments.HomeFragment;
import com.da.rflsneekrs.fragments.InboxFragment;
import com.da.rflsneekrs.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

@SuppressWarnings("FieldCanBeLocal")
public class MainViewActivity extends AppCompatActivity {
  private BottomNavigationView bottomNavigationView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_view);
    // Initialize components
    initializeViews();
    // attaching bottom sheet behaviour - hide / show on scroll
    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
    layoutParams.setBehavior(new ScrollNavigationBar());
    // the main fragments on main activity
    bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
    getSupportFragmentManager().beginTransaction().replace(R.id.fgContainer, new HomeFragment()).commit();
  }

  // Block the user come back to previous activity
  @Override
  public void onBackPressed() {
    //super.onBackPressed();
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_HOME);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
    finishAffinity();
  }
  // setting the menu on navigation bar
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
      assert fragment != null;
      getSupportFragmentManager().beginTransaction().replace(R.id.fgContainer, fragment).commit();

      return true;
    }
  };
  // initialize navigation bar
  private void initializeViews() {

    bottomNavigationView = findViewById(R.id.bottom_navigation);
  }
}