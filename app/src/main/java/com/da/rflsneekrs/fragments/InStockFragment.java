package com.da.rflsneekrs.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.da.rflsneekrs.R;
import com.da.rflsneekrs.adapters.ListGridAdapter;
import com.da.rflsneekrs.decoration.SpaceGridDecoration;
import com.da.rflsneekrs.models.Product;
import com.da.rflsneekrs.settings.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.da.rflsneekrs.adapters.ListGridAdapter.SPAN_COUNT_ONE;
import static com.da.rflsneekrs.adapters.ListGridAdapter.SPAN_COUNT_TWO;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InStockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("FieldCanBeLocal")
public class InStockFragment extends Fragment {
  private static final String TAG = "MyActivity";
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

  private SessionManager userSession;

  private ImageButton imageButton;
  private RecyclerView recyclerView;
  private ListGridAdapter listGridAdapter;
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
    // instantiate user session
    userSession = new SessionManager(requireActivity().getApplicationContext());
    // instantiate the layout for display the items
    gridLayoutManager = new GridLayoutManager(getActivity(), userSession.getListGridStock());
    // Inflate the layout for this fragment
    View fragmentView = inflater.inflate(R.layout.fragment_instock, container, false);
    imageButton = fragmentView.findViewById(R.id.spanBtn);

    recyclerView = (RecyclerView) fragmentView.findViewById(R.id.in_stock_recycle_view);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(gridLayoutManager);
    fbDatabase = FirebaseDatabase.getInstance();
    dbReference = fbDatabase.getReference("Products");
    query = dbReference.orderByChild(PRODUCT_KEY).equalTo(STOCK_VALUE);

    imageButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        switchLayout();
        switchIcon();
      }
    });

    return fragmentView;
  }

  @Override
  public void onStart() {
    super.onStart();
    if(userSession.getIconStock()){
      imageButton.setImageResource(R.drawable.ic_span_grid);
    } else {
      imageButton.setImageResource(R.drawable.ic_span_list);
    }
    // Get List Products from the database
    query.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        productList = new ArrayList<>();
        for (DataSnapshot productSnap: dataSnapshot.getChildren()) {
          Product product = productSnap.getValue(Product.class);
          productList.add(product);
        }
        listGridAdapter = new ListGridAdapter(getActivity(),productList, gridLayoutManager);
        recyclerView.setAdapter(listGridAdapter);
        recyclerView.addItemDecoration(new SpaceGridDecoration(10));
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
      }
    });
  }

  private void switchLayout() {
    if (userSession.getListGridStock() == SPAN_COUNT_ONE) {
      gridLayoutManager.setSpanCount(SPAN_COUNT_TWO);
      userSession.setListGridStock(SPAN_COUNT_TWO);
      userSession.setIconStock(false);
    } else {
      gridLayoutManager.setSpanCount(SPAN_COUNT_ONE);
      userSession.setListGridStock(SPAN_COUNT_ONE);
      userSession.setIconStock(true);
    }
    listGridAdapter.notifyItemRangeChanged(0, listGridAdapter.getItemCount());
  }

  private void switchIcon() {
    if(userSession.getIconStock()){
      imageButton.setImageResource(R.drawable.ic_span_grid);
    } else {
      imageButton.setImageResource(R.drawable.ic_span_list);
    }
  }
}