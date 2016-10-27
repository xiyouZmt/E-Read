package com.zmt.e_read.Animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by MintaoZhu on 2016/10/27.
 */
public class FABAnimator {

    public static void showFAB(View view, float to){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "TranslationY", 0, to);
        animator.setDuration(1000);
        animator.setInterpolator(new OvershootInterpolator());
        animator.start();
    }

    public static void hideFAB(final View view, float from){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "TranslationY", from, 0);
        animator.setDuration(1000);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

}
