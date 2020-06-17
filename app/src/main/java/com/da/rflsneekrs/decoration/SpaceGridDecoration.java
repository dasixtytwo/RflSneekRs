package com.da.rflsneekrs.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpaceGridDecoration extends RecyclerView.ItemDecoration {
  private int space;

  public SpaceGridDecoration(int space) {
    this.space = space;
  }

  @Override
  public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
    // Add this code to recyclerView: recyclerView.addItemDecoration(new SpacesItemDecoration(5));
    super.getItemOffsets(outRect, view, parent, state);
    outRect.left = space;
    outRect.right = space;
    outRect.bottom = space;

    // Add top Margin only for the first item to avoid double space between items
    if (parent.getChildLayoutPosition(view) == 0) {
      outRect.top = space;
    } else {
      outRect.top = 0;
    }
  }
}
