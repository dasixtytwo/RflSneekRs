package com.da.rflsneekrs.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.da.rflsneekrs.activities.MainUnlogActivity;
import com.da.rflsneekrs.R;
import com.da.rflsneekrs.activities.SettingsActivity;
import com.da.rflsneekrs.adapters.ViewPagerAdapter;
import com.da.rflsneekrs.models.User;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  private String mParam1;
  private String mParam2;

  FirebaseAuth auth;
  FirebaseDatabase fbDatabase;
  DatabaseReference dbReference;

  private TabLayout tabLayout;
  private TextView profileTv;
  private ImageView profileImg;
  private ViewPager viewPager;

  private FavouritesFragment favouritesFragment;
  private PurchasesFragment purchasesFragment;

  public String firstName, lastName, email;

  public ProfileFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment ProfileFragment.
   */
  public static ProfileFragment newInstance(String param1, String param2) {
    ProfileFragment fragment = new ProfileFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("PROFILE");
    // Setting the menu item
    setHasOptionsMenu(true);

    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu,inflater);
    inflater.inflate(R.menu.profile_setting, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId())
    {
      case R.id.setting_button:
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    auth = FirebaseAuth.getInstance();
    fbDatabase = FirebaseDatabase.getInstance();
    dbReference = fbDatabase.getReference("Users");
    FirebaseUser firebaseUser = auth.getCurrentUser();

    // Inflate the layout for this fragment
    View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
    viewPager = fragmentView.findViewById(R.id.profile_view_pager);
    profileTv = fragmentView.findViewById(R.id.profileName);
    tabLayout = fragmentView.findViewById(R.id.profile_tabs);
    profileImg = fragmentView.findViewById(R.id.profileImg);

    favouritesFragment = new FavouritesFragment();
    purchasesFragment = new PurchasesFragment();

    tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    tabLayout.setupWithViewPager(viewPager);

    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);
    viewPagerAdapter.addFragment(favouritesFragment, "FAVOURITES");
    viewPagerAdapter.addFragment(purchasesFragment, "PURCHASES");
    viewPager.setAdapter(viewPagerAdapter);

    if(auth.getCurrentUser() == null){
      Intent intent = new Intent(getActivity(), MainUnlogActivity.class);
      startActivity(intent);
    } else {
      // Load an image if exist on database otherwise load default one using Picasso library
      assert firebaseUser != null;
      Picasso.get()
          .load(firebaseUser.getPhotoUrl())
          .error(R.drawable.avatar)
          .into(profileImg);

      getUser();
    }

    return fragmentView;
  }

  private void getUser() {
    final String UID = auth.getUid();
    dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot userSnapShot) {
        assert UID != null;
        User user = userSnapShot.child(UID).getValue(User.class);
        assert user != null;
        String fullName = user.getFirstName() + " " + user.getLastName();
        profileTv.setText(fullName);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
      }
    });
  }
}