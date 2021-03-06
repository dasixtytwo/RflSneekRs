package com.da.rflsneekrs.fragments;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.da.rflsneekrs.activities.MainUnlogActivity;
import com.da.rflsneekrs.R;
import com.da.rflsneekrs.activities.SettingsActivity;
import com.da.rflsneekrs.adapters.ViewPagerAdapter;
import com.da.rflsneekrs.settings.SessionManager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("FieldCanBeLocal")
public class ProfileFragment extends Fragment {

  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  private String mParam1;
  private String mParam2;

  private FirebaseAuth auth;
  private FirebaseDatabase fbDatabase;
  private DatabaseReference dbReference;

  private SessionManager userSession;

  private TabLayout tabLayout;
  private TextView profileTv;
  private ImageView profileImg;
  private ViewPager viewPager;

  private FavouritesFragment favouritesFragment;
  private PurchasesFragment purchasesFragment;

  private String firstName, lastName, email;

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
    Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("PROFILE");
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
    if (item.getItemId() == R.id.setting_button) {
      Intent intent = new Intent(getActivity(), SettingsActivity.class);
      startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Instantiate Firebase
    auth = FirebaseAuth.getInstance();
    fbDatabase = FirebaseDatabase.getInstance();
    dbReference = fbDatabase.getReference("Users");
    FirebaseUser firebaseUser = auth.getCurrentUser();
    // Initialize session
    userSession = new SessionManager(requireActivity().getApplicationContext());
    // Inflate the layout for this fragment
    View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
    // Check if the user is logged In, if not redirect the main view activity
    if(userSession.getLogin() == null) {
      Intent intent = new Intent(getActivity(), MainUnlogActivity.class);
      startActivity(intent);
    } else {
      viewPager = fragmentView.findViewById(R.id.profile_view_pager);
      profileTv = fragmentView.findViewById(R.id.profileName);
      tabLayout = fragmentView.findViewById(R.id.profile_tabs);
      profileImg = fragmentView.findViewById(R.id.profileImg);

      favouritesFragment = new FavouritesFragment();
      purchasesFragment = new PurchasesFragment();

      tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
      tabLayout.setupWithViewPager(viewPager);
      // Setting the view pager with 2 tabs
      ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);
      viewPagerAdapter.addFragment(favouritesFragment, "FAVOURITES");
      viewPagerAdapter.addFragment(purchasesFragment, "PURCHASES");
      viewPager.setAdapter(viewPagerAdapter);
      // Load an image if exist on database otherwise load default one using Picasso library
      assert firebaseUser != null;
      if (firebaseUser.getPhotoUrl() != null){
        Picasso.get()
            .load(firebaseUser.getPhotoUrl())
            .error(R.drawable.avatar)
            .into(profileImg);
      } else {
        String imgUri = "http://davideagosti.co.uk/wp-content/uploads/2020/06/avatar.png";
        Picasso.get()
            .load(imgUri)
            .error(R.drawable.avatar)
            .into(profileImg);
      }
      // call getUser method
      getUser();
    }
    // return the fragment
    return fragmentView;
  }
  // Get user method
  @SuppressLint("SetTextI18n")
  private void getUser() {
    HashMap<String, String> userDetails = userSession.getUserDetails();
    String firstName = userDetails.get(SessionManager.KEY_FIRST_NAME);
    String lastName = userDetails.get(SessionManager.KEY_LAST_NAME);
    profileTv.setText(firstName + " " + lastName);
  }
}