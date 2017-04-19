package com.cpigeon.app.modular.guide.view;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by chenshuai on 2017/4/19.
 */

public class AppIntroFragment extends BaseFragment {

    boolean isEnd = false;
    int imageRerource = 0;
    @BindView(R.id.iv_guide_image)
    AppCompatImageView ivGuideImage;
    @BindView(R.id.btn_entry)
    AppCompatButton btnEntry;

    @Override
    protected void initView(View view) {
        btnEntry.setVisibility(isEnd ? View.VISIBLE : View.GONE);
        if (imageRerource != 0) {
            ivGuideImage.setBackgroundResource(imageRerource);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_appintro;
    }


    @OnClick(R.id.btn_entry)
    public void onViewClicked() {
        AppIntroActivity activity = (AppIntroActivity) getActivity();
        activity.entryApp();
    }

    public void setBackgroundResource(@DrawableRes int resource) {
        imageRerource = resource;
        if (ivGuideImage != null) {
            ivGuideImage.setBackgroundResource(imageRerource);
        }
    }

    public void setIsEnd(boolean isend) {
        this.isEnd = isend;
        if (btnEntry != null) {
            btnEntry.setVisibility(isEnd ? View.VISIBLE : View.GONE);
        }
    }

}
