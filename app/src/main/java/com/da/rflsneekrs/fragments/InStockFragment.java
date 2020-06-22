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
import com.da.rflsneekrs.adapters.GridAdapter;
import com.da.rflsneekrs.decoration.SpaceGridDecoration;
import com.da.rflsneekrs.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.da.rflsneekrs.adapters.GridAdapter.SPAN_COUNT_TWO;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InStockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("FieldCanBeLocal")
public class InStockFragment extends Fragment {

  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  private String mParam1;
  private String mParam2;

  private static final String PRODUCT_KEY = "inStock";
  private static final Boolean STOCK_VALUE = true;

  private FirebaseDatabase fbDatabase;
  private DatabaseReference dbReference;
  private Query query;

  private RecyclerView recyclerView;
  private GridAdapter gridAdapter;
  private GridLayoutManager gridLayoutManager;
  private List<Product> productList;

  public InStockFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment InStockFragment.
   */
  public static InStockFragment newInstance(String param1, String param2) {
    InStockFragment fragment = new InStockFragment();
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
    gridLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT_TWO);
    // Inflate the layout for this fragment
    View fragmentView = inflater.inflate(R.layout.fragment_instock, container, false);
    recyclerView = (RecyclerView) fragmentView.findViewById(R.id.in_stock_recycle_view);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(gridLayoutManager);
    fbDatabase = FirebaseDatabase.getInstance();
    dbReference = fbDatabase.getReference("Products");
    query = dbReference.orderByChild(PRODUCT_KEY).equalTo(STOCK_VALUE);

    return fragmentView;
  }

  @Override
  public void onStart() {
    super.onStart();
    // Get List Products from the database
    query.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        productList = new ArrayList<>();
        for (DataSnapshot productSnap: dataSnapshot.getChildren()) {
          Product product = productSnap.getValue(Product.class);
          productList.add(product);
        }
        gridAdapter = new GridAdapter(getActivity(),productList, gridLayoutManager);
        recyclerView.setAdapter(gridAdapter);
        recyclerView.addItemDecoration(new SpaceGridDecoration(5));
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
      }
    });
  }
}