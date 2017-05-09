package com.cpigeon.app.modular.guide.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cpigeon.app.MainActivity;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.ContentFragmentAdapter;
import com.cpigeon.app.modular.usercenter.view.activity.LoginActivity;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.StatusBarTool;
import com.nineoldandroids.view.ViewHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by chenshuai on 2017/4/19.
 */

public class WelcomeActivity extends BaseActivity {
    static final int NUM_PAGES = 4;
    boolean isOpaque = true;
    private ContentFragmentAdapter mContentFragmentAdapter;
    @BindView(R.id.pager)
    ViewPager pager;
//    @BindView(R.id.skip)
//    Button skip;
    @BindView(R.id.circles)
    LinearLayout circles;
    @BindView(R.id.done)
    Button done;
    @BindView(R.id.next)
    ImageButton next;
    @BindView(R.id.button_layout)
    RelativeLayout buttonLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_tutorial;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        StatusBarTool.hideStatusBar(this);
        mContentFragmentAdapter = new ContentFragmentAdapter(getSupportFragmentManager());

        ProductTourFragment fragment;
        fragment = ProductTourFragment.newInstance(R.layout.welcome_fragment01);
        mContentFragmentAdapter.appendData(fragment);

        fragment = ProductTourFragment.newInstance(R.layout.welcome_fragment02);
        mContentFragmentAdapter.appendData(fragment);

        fragment = ProductTourFragment.newInstance(R.layout.welcome_fragment03);
        mContentFragmentAdapter.appendData(fragment);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(mContentFragmentAdapter);
        pager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        pager.setPageTransformer(true, new CrossfadePageTransformer());
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == NUM_PAGES - 2 && positionOffset > 0) {
                    if (isOpaque) {
                        pager.setBackgroundColor(Color.TRANSPARENT);
                        isOpaque = false;
                    }
                } else {
                    if (!isOpaque) {
                        pager.setBackgroundColor(getResources().getColor(R.color.primary_material_light));
                        isOpaque = true;
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                setIndicator(position);
                if (position == NUM_PAGES - 2) {
//                    skip.setVisibility(View.GONE);
                    next.setVisibility(View.GONE);
                    done.setVisibility(View.VISIBLE);
                } else if (position < NUM_PAGES - 2) {
//                    skip.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    done.setVisibility(View.GONE);
                } else if (position == NUM_PAGES - 1) {
                    endTutorial();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        buildCircles();
    }

    private void buildCircles() {
        circles = LinearLayout.class.cast(findViewById(R.id.circles));

        float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (5 * scale + 0.5f);

        for (int i = 0; i < NUM_PAGES - 1; i++) {
            ImageView circle = new ImageView(this);
            circle.setImageResource(R.mipmap.ic_swipe_indicator_white_18dp);
            circle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            circle.setAdjustViewBounds(true);
            circle.setPadding(padding, 0, padding, 0);
            circles.addView(circle);
        }

        setIndicator(0);
    }

    private void endTutorial() {
        Intent intent;
        if (checkLogin())
            intent = new Intent(this, MainActivity.class);
        else
            intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        SharedPreferencesTool.Save(mContext, "guide_version", CommonTool.getVersionName(mContext));
    }


    @OnClick({ R.id.done, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.skip:
//                endTutorial();
//                break;
            case R.id.done:
                endTutorial();
                break;
            case R.id.next:
                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    private void setIndicator(int index) {
        if (index < NUM_PAGES) {
            for (int i = 0; i < NUM_PAGES - 1; i++) {
                ImageView circle = (ImageView) circles.getChildAt(i);
                if (i == index) {
                    circle.setColorFilter(getResources().getColor(R.color.text_selected));
                } else {
                    circle.setColorFilter(getResources().getColor(android.R.color.transparent));
                }
            }
        }
    }


    public class CrossfadePageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();

            View backgroundView = page.findViewById(R.id.welcome_fragment);
            View text_head = page.findViewById(R.id.heading);
            View text_content = page.findViewById(R.id.content);
            View welcomeImage01 = page.findViewById(R.id.welcome_01);
            View welcomeImage02 = page.findViewById(R.id.welcome_02);
            View welcomeImage03 = page.findViewById(R.id.welcome_03);

            if (0 <= position && position < 1) {
                ViewHelper.setTranslationX(page, pageWidth * -position);
            }
            if (-1 < position && position < 0) {
                ViewHelper.setTranslationX(page, pageWidth * -position);
            }

            if (position <= -1.0f || position >= 1.0f) {
            } else if (position == 0.0f) {
            } else {
                if (backgroundView != null) {
                    ViewHelper.setAlpha(backgroundView, 1.0f - Math.abs(position));

                }

                if (text_head != null) {
                    ViewHelper.setTranslationX(text_head, pageWidth * position);
                    ViewHelper.setAlpha(text_head, 1.0f - Math.abs(position));
                }

                if (text_content != null) {
                    ViewHelper.setTranslationX(text_content, pageWidth * position);
                    ViewHelper.setAlpha(text_content, 1.0f - Math.abs(position));
                }

                if (welcomeImage01 != null) {
                    ViewHelper.setTranslationX(welcomeImage01, (float) (pageWidth / 2 * position));
                    ViewHelper.setAlpha(welcomeImage01, 1.0f - Math.abs(position));
                }

                if (welcomeImage02 != null) {
                    ViewHelper.setTranslationX(welcomeImage02, (float) (pageWidth / 2 * position));
                    ViewHelper.setAlpha(welcomeImage02, 1.0f - Math.abs(position));
                }

                if (welcomeImage03 != null) {
                    ViewHelper.setTranslationX(welcomeImage03, (float) (pageWidth / 2 * position));
                    ViewHelper.setAlpha(welcomeImage03, 1.0f - Math.abs(position));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pager != null) {
            pager.clearOnPageChangeListeners();
        }
    }
}
