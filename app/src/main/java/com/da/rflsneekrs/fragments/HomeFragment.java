package com.da.rflsneekrs.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.da.rflsneekrs.R;
import com.da.rflsneekrs.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  private String mParam1;
  private String mParam2;

  TabLayout tabLayout;
  ViewPager viewPager;
  public ImageButton spanButton;
  ImageButton filterButton;

  private FeedFragment feedFragment;
  private InStockFragment inStockFragment;
  private UpComingFragment upComingFragment;

  public HomeFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment HomeFragment.
   */
  public static HomeFragment newInstance(String param1, String param2) {
    HomeFragment fragment = new HomeFragment();
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
    // Inflate the layout for this fragment
    View fragmentView =  inflater.inflate(R.layout.fragment_home, container, false);

    tabLayout = fragmentView.findViewById(R.id.homeTabsLayout);
    viewPager = fragmentView.findViewById(R.id.view_pager);
    spanButton = fragmentView.findViewById(R.id.spanBtn);
    filterButton = fragmentView.findViewById(R.id.filterBtn);

    feedFragment = new FeedFragment();
    inStockFragment = new InStockFragment();
    upComingFragment = new UpComingFragment();

    tabLayout.setupWithViewPager(viewPager);

    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);
    viewPagerAdapter.addFragment(feedFragment, "FEED");
    viewPagerAdapter.addFragment(inStockFragment, "IN STOCK");
    viewPagerAdapter.addFragment(upComingFragment, "UPCOMING");
    viewPager.setAdapter(viewPagerAdapter);

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override
      public void onPageSelected(int position) {

      }

      @Override
      public void onPageScrollStateChanged(int state) {
      }
    });

    return fragmentView;
  }
}