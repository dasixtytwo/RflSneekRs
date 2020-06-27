package com.da.rflsneekrs.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.da.rflsneekrs.R;
import com.da.rflsneekrs.adapters.FavGridAdapter;
import com.da.rflsneekrs.decoration.SpaceGridDecoration;
import com.da.rflsneekrs.models.FavItem;
import com.da.rflsneekrs.models.Product;
import com.da.rflsneekrs.settings.SessionManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.da.rflsneekrs.adapters.FavGridAdapter.SPAN_COUNT_TWO;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("FieldCanBeLocal")
public class FavouritesFragment extends Fragment {

  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  private String mParam1;
  private String mParam2;

  private static final String PRODUCT_KEY = "favStatus";
  private static final int FAV_STATUS = 1;

  private Query query;
  private FirebaseAuth auth;
  private FirebaseDatabase fbDatabase;
  private DatabaseReference dbReference;
  private SessionManager userSession;

  private RecyclerView favRecyclerView;
  private FavGridAdapter favGridAdapter;
  private List<FavItem> favItemList;
  private GridLayoutManager gridLayoutManager;

  public FavouritesFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment FavouritesFragment.
   */
  public static FavouritesFragment newInstance(String param1, String param2) {
    FavouritesFragment fragment = new FavouritesFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // instantiate user session
    userSession = new SessionManager(requireActivity().getApplicationContext());
    // instantiate the layout for display the items
    gridLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT_TWO);
    // Inflate the layout for this fragment
    View fragmentView = inflater.inflate(R.layout.fragment_favourites, container, false);

    favRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.fav_recycler_view);
    favRecyclerView.setHasFixedSize(true);
    favRecyclerView.setLayoutManager(gridLayoutManager);
    // instantiate authorization
    auth = FirebaseAuth.getInstance();
    fbDatabase = FirebaseDatabase.getInstance();
    dbReference = fbDatabase.getReference("FavouriteItem").child(Objects.requireNonNull(auth.getUid()));
    query = dbReference.orderByChild(PRODUCT_KEY).equalTo(FAV_STATUS);

    return fragmentView;
  }

  @Override
  public void onStart() {
    super.onStart();
    if (favItemList != null) {
      favItemList.clear();
    }
    // Get List Products from the database
    query.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        favItemList = new ArrayList<>();
        for (DataSnapshot productSnap: dataSnapshot.getChildren()) {
          FavItem product = productSnap.getValue(FavItem.class);
          favItemList.add(product);
        }
        favGridAdapter = new FavGridAdapter(getActivity(), favItemList, gridLayoutManager);
        favRecyclerView.setAdapter(favGridAdapter);
        favRecyclerView.addItemDecoration(new SpaceGridDecoration(10));
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
      }
    });
  }
}