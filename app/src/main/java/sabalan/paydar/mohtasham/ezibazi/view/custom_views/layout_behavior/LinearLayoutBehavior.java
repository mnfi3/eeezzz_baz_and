package sabalan.paydar.mohtasham.ezibazi.view.custom_views.layout_behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

public class LinearLayoutBehavior extends CoordinatorLayout.Behavior<LinearLayout> {
  private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
  private boolean mIsAnimatingOut = false;

  public LinearLayoutBehavior(Context context, AttributeSet attrs) {
    super();
  }

  @Override
  public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final LinearLayout child,
                                     final View directTargetChild, final View target, final int nestedScrollAxes) {
    return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
      || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
  }

  @Override
  public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final LinearLayout child,
                             final View target, final int dxConsumed, final int dyConsumed,
                             final int dxUnconsumed, final int dyUnconsumed) {
    super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    if (dyConsumed > 0 && !this.mIsAnimatingOut && child.getVisibility() == View.VISIBLE) {
      // User scrolled down and the FAB is currently visible -> hide the FAB
      animateOut(child);
    } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
      // User scrolled up and the FAB is currently not visible -> show the FAB
      animateIn(child);
    }
  }

  private void animateOut(final LinearLayout linearLayout) {
    ViewCompat.animate(linearLayout).translationY(168F).alpha(0.0F).setInterpolator(INTERPOLATOR).withLayer()
      .setListener(new ViewPropertyAnimatorListener() {
        public void onAnimationStart(View view) {
          LinearLayoutBehavior.this.mIsAnimatingOut = true;
        }

        public void onAnimationCancel(View view) {
          LinearLayoutBehavior.this.mIsAnimatingOut = false;
        }

        public void onAnimationEnd(View view) {
          LinearLayoutBehavior.this.mIsAnimatingOut = false;
          view.setVisibility(View.INVISIBLE);
        }
      }).start();
  }

  private void animateIn(LinearLayout linearLayout) {
    linearLayout.setVisibility(View.VISIBLE);
    ViewCompat.animate(linearLayout).translationY(0).scaleX(1.0F).scaleY(1.0F).alpha(1.0F)
      .setInterpolator(INTERPOLATOR).withLayer().setListener(null)
      .start();
  }
}
