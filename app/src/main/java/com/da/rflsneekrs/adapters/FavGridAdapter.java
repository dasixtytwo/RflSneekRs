package com.da.rflsneekrs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.da.rflsneekrs.R;
import com.da.rflsneekrs.models.FavItem;
import com.da.rflsneekrs.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("FieldCanBeLocal")
public class FavGridAdapter extends RecyclerView.Adapter<FavGridAdapter.ViewHolder> {
  public static final int SPAN_COUNT_TWO = 2;

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
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.column_product_item,
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
    ImageButton imgFavBtn;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      imgProduct = itemView.findViewById(R.id.col_product_image);
      imgFavBtn= itemView.findViewById(R.id.favorite_btn);

      // instantiate database
      fbDatabase = FirebaseDatabase.getInstance();
      dbReference = fbDatabase.getReference("FavouriteItem");
      //remove from fav after click
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //initialize position
          int position = getAdapterPosition();
          final FavItem favItem = favItemList.get(position);
          final DatabaseReference upFavRefLike = dbReference.child(favItemList.get(position).getKeyId());

          Map<String, Object> userUpdates = new HashMap<>();
          userUpdates.put(favItem.getKeyId(), new FavItem(null, null,0));
          dbReference.updateChildren(userUpdates);
          removeItem(position);

          upFavRefLike.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
              try {
                Integer currentValue = mutableData.getValue(Integer.class);
                if (currentValue == null) {
                  mutableData.setValue(1);
                } else {
                  mutableData.setValue(currentValue - 1);
                }
              } catch (Exception e) {
                throw e;
              }
              return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
              Toast.makeText(context.getApplicationContext(), "Removed from favourite", Toast.LENGTH_LONG).show();
            }
          });
        }
      });
    }

    private void removeItem(int position) {
      favItemList.remove(position);
      notifyItemRemoved(position);
      notifyItemRangeChanged(position,favItemList.size());
    }
  }
}
