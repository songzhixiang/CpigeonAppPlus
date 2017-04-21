package com.cpigeon.app.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout.LayoutParams;

/**
 * Created by Administrator on 2016/11/15.
 */

public class ViewExpandAnimation extends Animation {

    private View mAnimationView = null;
    private LayoutParams mViewLayoutParams = null;
    private int mStart = 0;
    private int mEnd = 0;

    public ViewExpandAnimation(View view) {
        animationSettings(view, 500);
    }

    public ViewExpandAnimation(View view, int duration) {
        animationSettings(view, duration);
    }

    private void animationSettings(View view, int duration) {
        setDuration(duration);
        mAnimationView = view;
        mViewLayoutParams = (LayoutParams) view.getLayoutParams();
        mStart = mViewLayoutParams.bottomMargin;
        mEnd = -view.getHeight();
        mEnd = mStart == 0 ? (0 - view.getHeight()) : 0;
        view.setVisibility(View.VISIBLE);
    }


    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        if (interpolatedTime < 1.0f) {
            mViewLayoutParams.bottomMargin = mStart + (int) ((mEnd - mStart) * interpolatedTime);
            // invalidate
            mAnimationView.requestLayout();
        } else {
            mViewLayoutParams.bottomMargin = mEnd;
            mAnimationView.requestLayout();
            if (mEnd != 0) {
                mAnimationView.setVisibility(View.GONE);
            }
        }
    }

}