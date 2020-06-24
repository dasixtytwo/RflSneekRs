package com.da.rflsneekrs.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.jar.Attributes;

public class ScrollNavigationBar extends CoordinatorLayout.Behavior<BottomNavigationView> {
  public ScrollNavigationBar() {
    super();
  }

  public ScrollNavigationBar(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull BottomNavigationView child, @NonNull View dependency) {
    return dependency instanceof FrameLayout;
  }

  @Override
  public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child, @NonNull View directTargetChild, @NonNull View target, int nestedScrollAxes) {
    return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
  }

  @Override
  public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child, @NonNull View target, int dx, int dy, @NonNull int[] consumed) {
    if (dy < 0) {
      showBottomNavigationView(child);
    } else if (dy > 0) {
      hideBottomNavigationView(child);
    }
  }

  private void hideBottomNavigationView(BottomNavigationView view) {
    view.animate().translationY(view.getHeight());
  }

  private void showBottomNavigationView(BottomNavigationView view) {
    view.animate().translationY(0);
  }
}
