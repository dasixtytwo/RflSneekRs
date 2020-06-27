package com.da.rflsneekrs.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.da.rflsneekrs.R;
import com.da.rflsneekrs.activities.ProductDetailActivity;
import com.da.rflsneekrs.models.FavItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.squareup.picasso.Picasso;

import java.util.List;


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
      /*fbDatabase = FirebaseDatabase.getInstance();
      dbReference = fbDatabase.getReference("FavouriteItem");*/
      // show item details on product detail activity when click on item
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent productDetailActivity = new Intent(context, ProductDetailActivity.class);
          int position = getAdapterPosition();

          // Save the double data Type from database in a variable, ready to be converted
          Double d = favItemList.get(position).getPrice();
          // convert the variable above in a string
          String price = "£." + d;

          productDetailActivity.putExtra("name",favItemList.get(position).getName());
          productDetailActivity.putExtra("brand",favItemList.get(position).getBrand());
          productDetailActivity.putExtra("image",favItemList.get(position).getImageUri());
          productDetailActivity.putExtra("description",favItemList.get(position).getDescription());
          productDetailActivity.putExtra("price", price);
          // will fix this later
          //long timestamp  = (long) productItems.get(position).getTimeStamp();
          //productDetailActivity.putExtra("postDate",timestamp) ;
          context.startActivity(productDetailActivity);
        }
      });
    }
  }
}
