package com.da.rflsneekrs.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.da.rflsneekrs.R;
import com.da.rflsneekrs.activities.MainUnlogActivity;
import com.da.rflsneekrs.activities.ProductDetailActivity;
import com.da.rflsneekrs.models.FavItem;
import com.da.rflsneekrs.models.Product;
import com.da.rflsneekrs.settings.SessionManager;

import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Objects;

@SuppressWarnings("FieldCanBeLocal")
public class ListGridAdapter extends RecyclerView.Adapter<ListGridAdapter.ListGridViewHolder> {
  public static final int SPAN_COUNT_ONE = 1;
  public static final int SPAN_COUNT_TWO = 2;

  public static final int VIEW_TYPE_LIST = 1;
  public static final int VIEW_TYPE_GRID = 2;

  private Context context;
  private List<Product> productItems;
  private GridLayoutManager gridLayoutManager;
  public SessionManager userSession;
  private FirebaseAuth auth;

  private FirebaseDatabase fbDatabase;
  private DatabaseReference dbReference;

  public ListGridAdapter(Context cx, List<Product> product, GridLayoutManager layoutManager) {
    this.context = cx;
    this.productItems = product;
    this.gridLayoutManager = layoutManager;
  }

  @Override
  public int getItemViewType(int position) {
    super.getItemViewType(position);
    int spanCount = gridLayoutManager.getSpanCount();

    if (spanCount == SPAN_COUNT_ONE) {
      return VIEW_TYPE_LIST;
    } else {
      return VIEW_TYPE_GRID;
    }
  }

  @NonNull
  @Override
  public ListGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view;
    if (viewType == VIEW_TYPE_LIST){
      view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_item, parent, false);
    } else {
      view = LayoutInflater.from(parent.getContext()).inflate(R.layout.column_product_item, parent, false);
    }

    return new ListGridViewHolder(view, viewType);
  }

  @Override
  public void onBindViewHolder(@NonNull ListGridAdapter.ListGridViewHolder holder, int position) {
    int size = productItems.size();
    Product product = productItems.get(position % size);
    String imageUri = product.getImage();
    Picasso.get().load(imageUri).placeholder(R.drawable.ic_broken_image).into(holder.imgProduct);
    if (getItemViewType(position) == VIEW_TYPE_LIST) {
      holder.tvName.setText(product.getName());
      holder.tvBrand.setText(product.getBrand());
    }
  }

  @Override
  public int getItemCount() {
    return productItems.size();
  }

  public class ListGridViewHolder extends RecyclerView.ViewHolder {
    TextView tvName, tvBrand;
    ImageView imgProduct;
    ImageButton imgShare, imgFavourite;

    public ListGridViewHolder(View ItemView, int viewType){
      super(ItemView);
      // instantiate authorization
      auth = FirebaseAuth.getInstance();
      // Initialize user session
      userSession = new SessionManager(context.getApplicationContext());
      // Check if view type is list or grid, and show appropriate layout
      if (viewType == VIEW_TYPE_LIST) {
        tvName = ItemView.findViewById(R.id.row_product_name);
        tvBrand = ItemView.findViewById(R.id.row_product_brand);
        imgProduct = ItemView.findViewById(R.id.row_product_image);
      } else {
        imgProduct = ItemView.findViewById(R.id.col_product_image);
      }
      // instantiate database
      fbDatabase = FirebaseDatabase.getInstance();
      dbReference = fbDatabase.getReference("FavouriteItem");

      // Setting share button
      imgShare = ItemView.findViewById(R.id.share_btn);
      // Setting favourites button
      imgFavourite = ItemView.findViewById(R.id.favorite_btn);

      // Show list or Grid, depend of viewType receive
      if (viewType == VIEW_TYPE_LIST) {
        imgShare.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, productItems.get(position).getName());
            intent.setType("text/*");
            Intent shareIntent = Intent.createChooser(intent, "Share via");
            context.startActivity(shareIntent);
          }
        });

        imgFavourite.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            //initialize position
            int position = getAdapterPosition();
            Product productItem = productItems.get(position);
            if (userSession.getLogin() == null ) {
              Intent intent = new Intent(context.getApplicationContext(), MainUnlogActivity.class);
              context.startActivity(intent);
            } else {
              likeClick(productItem, imgFavourite);
            }
          }
        });
      }
      // show item details on product detail activity when click on item
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent productDetailActivity = new Intent(context, ProductDetailActivity.class);
          int position = getAdapterPosition();

          // Save the double data Type from database in a variable, ready to be converted
          Double d = productItems.get(position).getPrice();
          // convert the variable above in a string
          String price = "£." + d;

          productDetailActivity.putExtra("name",productItems.get(position).getName());
          productDetailActivity.putExtra("brand",productItems.get(position).getBrand());
          productDetailActivity.putExtra("image",productItems.get(position).getImage());
          productDetailActivity.putExtra("description",productItems.get(position).getDescription());
          productDetailActivity.putExtra("price", price);
          // will fix this later
          //long timestamp  = (long) productItems.get(position).getTimeStamp();
          //productDetailActivity.putExtra("postDate",timestamp) ;
          context.startActivity(productDetailActivity);
        }
      });

    }

    private void likeClick(Product productItem, ImageButton imgFavourite) {
      final DatabaseReference upFavRefLike = dbReference.child(productItem.getKeyId());

      if (productItem.getFavStatus() == 0){
        // set the status of favorite to 1
        productItem.setFavStatus(1);
        Map<String, FavItem> favProduct = new HashMap<>();
        favProduct.put(productItem.getKeyId(), new FavItem(productItem.getImage(), productItem.getKeyId(), productItem.getFavStatus()));
        dbReference.setValue(favProduct);
        imgFavourite.setImageResource(R.drawable.ic_favorite_red);
        imgFavourite.setSelected(true);

        upFavRefLike.runTransaction(new Transaction.Handler() {
          @NonNull
          @Override
          public Transaction.Result doTransaction(@NonNull final MutableData mutableData) {
            try {
              Integer currentValue = mutableData.getValue(Integer.class);
              if (currentValue == null) {
                mutableData.setValue(1);
              } else {
                mutableData.setValue(currentValue + 1);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                  @Override
                  public void run() {
                    //textLike.setText(Objects.requireNonNull(mutableData.getValue()).toString());
                  }
                });
              }
            } catch (Exception e) {
              throw e;
            }
            return Transaction.success(mutableData);
          }

          @Override
          public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
            System.out.println("Add to favourite");
          }
        });
      } else if (productItem.getFavStatus() == 1) {
        productItem.setFavStatus(0);
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put(productItem.getKeyId(), new FavItem(null, null, 0));
        dbReference.updateChildren(userUpdates);

        imgFavourite.setImageResource(R.drawable.ic_favorite);
        imgFavourite.setSelected(false);

        upFavRefLike.runTransaction(new Transaction.Handler() {
          @NonNull
          @Override
          public Transaction.Result doTransaction(@NonNull final MutableData mutableData) {
            try {
              Integer currentValue = mutableData.getValue(Integer.class);
              if (currentValue == null) {
                mutableData.setValue(1);
              } else {
                mutableData.setValue(currentValue - 1);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                  @Override
                  public void run() {
                    //textLike.setText(mutableData.getValue().toString());
                  }
                });
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
    }
  }
}
