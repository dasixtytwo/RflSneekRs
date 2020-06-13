package com.da.rflsneekrs.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.da.rflsneekrs.activities.MainUnlogActivity;
import com.da.rflsneekrs.R;
import com.da.rflsneekrs.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InboxFragment extends Fragment {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  FirebaseAuth auth;

  TabLayout inboxTab;
  ViewPager inboxVP;

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
  // TODO: Rename and change types and number of parameters
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
    ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    auth = FirebaseAuth.getInstance();

    // Inflate the layout for this fragment
    View fragmentView = inflater.inflate(R.layout.fragment_inbox, container, false);

    // Initialize layouts
    inboxTab = fragmentView.findViewById(R.id.in_box_tab_layout);
    inboxVP = fragmentView.findViewById(R.id.in_box_view_pager);

    // Initialize Fragments
    notificationsFragment = new NotificationsFragment();
    ordersFragment = new OrdersFragment();

    inboxTab.setupWithViewPager(inboxVP);

    if(auth.getCurrentUser() == null){
      Intent intent = new Intent(getActivity(), MainUnlogActivity.class);
      startActivity(intent);
    } else {
      ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);
      viewPagerAdapter.addFragment(notificationsFragment, "NOTIFICATIONS");
      viewPagerAdapter.addFragment(ordersFragment, "ORDERS");
      inboxVP.setAdapter(viewPagerAdapter);
    }

    return fragmentView;
  }
}