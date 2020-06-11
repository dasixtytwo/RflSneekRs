package com.da.rflsneekrs.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.da.rflsneekrs.R;
import com.da.rflsneekrs.models.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProdViewHolder> {
  Context context;
  List<Product> productData;

  public ProductAdapter(Context cx, List<Product> pd){
    context = cx;
    productData = pd;
  }

  @NonNull
  @Override
  public ProdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return null;
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
    }
  }
}
