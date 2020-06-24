package com.da.rflsneekrs.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.da.rflsneekrs.activities.MainUnlogActivity;
import com.da.rflsneekrs.R;
import com.da.rflsneekrs.adapters.ViewPagerAdapter;
import com.da.rflsneekrs.settings.SessionManager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("FieldCanBeLocal")
public class InboxFragment extends Fragment {

  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";


  private String mParam1;
  private String mParam2;

  private FirebaseAuth auth;
  private SessionManager userSession;

  private TabLayout inboxTab;
  private ViewPager inboxVP;

  private NotificationsFragment notificationsFragment;
  private OrdersFragment ordersFragment;

  public InboxFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment InboxFragment.
   */
  public static InboxFragment newInstance(String param1, String param2) {
    InboxFragment fragment = new InboxFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();

    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // instantiate authorization
    auth = FirebaseAuth.getInstance();
    // Initialize user session
    userSession = new SessionManager(requireActivity().getApplicationContext());
    // Inflate the layout for this fragment
    View fragmentView = inflater.inflate(R.layout.fragment_inbox, container, false);
    // Check if the user is logged In, if not redirect the main view activity
    if(userSession.getLogin() == null){
      Intent intent = new Intent(getActivity(), MainUnlogActivity.class);
      startActivity(intent);
    } else {
      // Initialize layouts
      inboxTab = fragmentView.findViewById(R.id.in_box_tab_layout);
      inboxVP = fragmentView.findViewById(R.id.in_box_view_pager);
      // Initialize Fragments
      notificationsFragment = new NotificationsFragment();
      ordersFragment = new OrdersFragment();

      inboxTab.setupWithViewPager(inboxVP);
      ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);
      viewPagerAdapter.addFragment(notificationsFragment, "NOTIFICATIONS");
      viewPagerAdapter.addFragment(ordersFragment, "ORDERS");
      inboxVP.setAdapter(viewPagerAdapter);
    }
    // return the fragment
    return fragmentView;
  }
}