package com.da.rflsneekrs.adapters;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

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
    String imageUri = productData.get(position).getImage();
    Picasso.get().load(imageUri).placeholder(R.drawable.ic_broken_image).into(holder.imgProduct);
  }

  @Override
  public int getItemCount() {
    return productData.size();
  }

  public class ProdViewHolder extends RecyclerView.ViewHolder {
    TextView tvName, tvBrand;
    ImageView imgProduct;
    ImageButton imgShare;

    public ProdViewHolder(View ItemView){
      super(ItemView);

      tvName = ItemView.findViewById(R.id.row_product_name);
      tvBrand = ItemView.findViewById(R.id.row_product_brand);
      imgProduct = ItemView.findViewById(R.id.row_product_image);
      imgShare = ItemView.findViewById(R.id.share_btn);

      imgShare.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
          sharingIntent.setType("text/plain");
          context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
      });

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent postDetailActivity = new Intent(context, ProductDetailActivity.class);
          int position = getAdapterPosition();

          postDetailActivity.putExtra("name",productData.get(position).getName());
          postDetailActivity.putExtra("brand",productData.get(position).getBrand());
          postDetailActivity.putExtra("image",productData.get(position).getImage());
          postDetailActivity.putExtra("description",productData.get(position).getDescription());
          postDetailActivity.putExtra("price",productData.get(position).getPrice());
          // will fix this later i forgot to add user name to post object
          //long timestamp  = (long) productData.get(position).getTimeStamp();
          //postDetailActivity.putExtra("postDate",timestamp) ;
          context.startActivity(postDetailActivity);
        }
      });
    }
  }
}
