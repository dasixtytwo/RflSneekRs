package com.da.rflsneekrs.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.da.rflsneekrs.R;
import com.da.rflsneekrs.activities.ProductDetailActivity;
import com.da.rflsneekrs.models.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProdViewHolder> {
  Context context;
  List<Product> productData;

  public ProductAdapter(Context cx, List<Product> pd){
    this.context = cx;
    this.productData = pd;
  }

  @NonNull
  @Override
  public ProdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View row = LayoutInflater.from(context).inflate(R.layout.row_product_item,parent,false);
    return new ProdViewHolder(row);
  }

  @Override
  public void onBindViewHolder(@NonNull ProdViewHolder holder, int position) {
    holder.tvName.setText(productData.get(position).getName());
    holder.tvBrand.setText(productData.get(position).getBrand());
    Glide.with(context).load(productData.get(position).getImage()).into(holder.imgProduct);
  }

  @Override
  public int getItemCount() {
    return productData.size();
  }

  public class ProdViewHolder extends RecyclerView.ViewHolder {
    TextView tvName, tvBrand;
    ImageView imgProduct;

    public ProdViewHolder(View ItemView){
      super(ItemView);

      tvName = ItemView.findViewById(R.id.row_product_name);
      tvBrand = ItemView.findViewById(R.id.row_product_brand);
      imgProduct = ItemView.findViewById(R.id.row_product_image);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent postDetailActivity = new Intent(context, ProductDetailActivity.class);
          int position = getAdapterPosition();

          postDetailActivity.putExtra("title",productData.get(position).getName());
          postDetailActivity.putExtra("postImage",productData.get(position).getImage());
          postDetailActivity.putExtra("description",productData.get(position).getDescription());
          postDetailActivity.putExtra("userPhoto",productData.get(position).getPrice());
          // will fix this later i forgot to add user name to post object
          //long timestamp  = (long) productData.get(position).getTimeStamp();
          //postDetailActivity.putExtra("postDate",timestamp) ;
          context.startActivity(postDetailActivity);
        }
      });
    }
  }
}
