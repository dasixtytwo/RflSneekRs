package com.da.rflsneekrs.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.da.rflsneekrs.R;
import com.da.rflsneekrs.adapters.FavGridAdapter;
import com.da.rflsneekrs.adapters.ListGridAdapter;
import com.da.rflsneekrs.decoration.SpaceGridDecoration;
import com.da.rflsneekrs.models.FavItem;
import com.da.rflsneekrs.models.Product;
import com.da.rflsneekrs.settings.SessionManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.da.rflsneekrs.adapters.ListGridAdapter.SPAN_COUNT_TWO;

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

  private FirebaseDatabase fbDatabase;
  private DatabaseReference dbReference;
  private SessionManager userSession;

  private RecyclerView favRecyclerView;
  private FavGridAdapter favGridAdapter;
  //private ListGridAdapter favGridAdapter;
  private List<FavItem> favItemList;
  //private List<Product> favItemList;
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
    gridLayoutManager = new GridLayoutManager(getActivity(), 2);
    // Inflate the layout for this fragment
    View fragmentView = inflater.inflate(R.layout.fragment_favourites, container, false);

    favRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.fav_recycler_view);
    favRecyclerView.setHasFixedSize(true);
    favRecyclerView.setLayoutManager(gridLayoutManager);
    fbDatabase = FirebaseDatabase.getInstance();
    dbReference = fbDatabase.getReference("FavouriteItem");
    //dbReference = fbDatabase.getReference("FavouriteItem");

    // add item touch helper
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
    // set swipe to recyclerview
    itemTouchHelper.attachToRecyclerView(favRecyclerView);

    //loadData();
    return fragmentView;
  }

  @Override
  public void onStart() {
    super.onStart();
    /*if (favItemList != null) {
      favItemList.clear();
    }*/
    // Get List Products from the database
    dbReference.addValueEventListener(new ValueEventListener() {
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

  private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
      return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
      final int position = viewHolder.getAdapterPosition(); // get position which is swipe
      final FavItem favItem = favItemList.get(position);
      if (direction == ItemTouchHelper.LEFT) { //if swipe left
        favGridAdapter.notifyItemRemoved(position); // item removed from recyclerview
        favItemList.remove(position); //then remove item
        //favDB.remove_fav(favItem.getKey_id()); // remove item from database
      }
    }
  };
}