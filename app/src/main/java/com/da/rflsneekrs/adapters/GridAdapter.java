package com.da.rflsneekrs.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.da.rflsneekrs.R;
import com.da.rflsneekrs.activities.ProductDetailActivity;
import com.da.rflsneekrs.models.Product;
import com.squareup.picasso.Picasso;

import java.util.List;
@SuppressWarnings("FieldCanBeLocal")
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {
  public static final int SPAN_COUNT_TWO = 2;

  Context context;
  private List<Product> productItems;
  private GridLayoutManager gridLayoutManager;

  public GridAdapter(Context cx, List<Product> product, GridLayoutManager layoutManager) {
    this.context = cx;
    this.productItems = product;
    this.gridLayoutManager = layoutManager;
  }

  @NonNull
  @Override
  public GridAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.column_product_item, parent, false);
    return new GridViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull GridAdapter.GridViewHolder holder, int position) {
    int size = productItems.size();
    Product product = productItems.get(position % size);
    String imageUri = product.getImage();
    Picasso.get().load(imageUri).placeholder(R.drawable.ic_broken_image).into(holder.imgProduct);
  }

  @Override
  public int getItemCount() {
    return productItems.size();
  }

  class GridViewHolder extends RecyclerView.ViewHolder {
    ImageView imgProduct;

    GridViewHolder(View ItemView){
      super(ItemView);
      imgProduct = ItemView.findViewById(R.id.col_product_image);

      // setting Items to that display on product details
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent postDetailActivity = new Intent(context, ProductDetailActivity.class);
          int position = getAdapterPosition();

          // Save the double data Type from database in a variable, ready to be converted
          Double d = productItems.get(position).getPrice();
          // convert the variable above in a string
          String price = "£." + d;

          postDetailActivity.putExtra("name",productItems.get(position).getName());
          postDetailActivity.putExtra("brand",productItems.get(position).getBrand());
          postDetailActivity.putExtra("image",productItems.get(position).getImage());
          postDetailActivity.putExtra("description",productItems.get(position).getDescription());
          postDetailActivity.putExtra("price", price);
          // will fix this later i forgot to add user name to post object
          //long timestamp  = (long) productData.get(position).getTimeStamp();
          //postDetailActivity.putExtra("postDate",timestamp) ;
          context.startActivity(postDetailActivity);
        }
      });
    }
  }

}
