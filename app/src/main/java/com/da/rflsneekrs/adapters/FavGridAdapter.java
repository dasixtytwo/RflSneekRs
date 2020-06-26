package com.da.rflsneekrs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.da.rflsneekrs.R;
import com.da.rflsneekrs.models.FavItem;
import com.da.rflsneekrs.models.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class FavGridAdapter extends RecyclerView.Adapter<FavGridAdapter.ViewHolder> {

  private Context context;
  private List<FavItem> favItemList;
  private GridLayoutManager gridLayoutManager;

  private FirebaseDatabase fbDatabase;
  private DatabaseReference dbReference;

  public FavGridAdapter(Context cx, List<FavItem> favItem, GridLayoutManager layoutManager) {
    this.context = cx;
    this.favItemList = favItem;
    this.gridLayoutManager = layoutManager;
  }

  @NonNull
  @Override
  public FavGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_favourites,
        parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull FavGridAdapter.ViewHolder holder, int position) {
    int size = favItemList.size();
    FavItem favItem = favItemList.get(position % size);
    String imageUri = favItem.getImageUri();
    Picasso.get().load(imageUri).placeholder(R.drawable.ic_broken_image).into(holder.imgProduct);
  }

  @Override
  public int getItemCount() {
    return favItemList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    ImageView imgProduct;
    public ViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}
