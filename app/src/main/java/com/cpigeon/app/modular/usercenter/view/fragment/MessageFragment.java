package com.cpigeon.app.modular.usercenter.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/2/16.
 */

public class MessageFragment extends BaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.viewstub)
    ViewStub viewstub;
    View mEmptyTip;
    TextView mEmptyTipTextView;
    AppCompatImageView mEmptyTipImage;

    @Override
    protected void initView(View view) {
        showTips("暂无通知", TipType.View);
        mEmptyTipImage.setVisibility(View.GONE);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_message;
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        if (tipType == TipType.View) {
            if (mEmptyTip == null) mEmptyTip = viewstub.inflate();
            mEmptyTip.setVisibility(View.VISIBLE);
            if (mEmptyTipTextView == null)
                mEmptyTipTextView = (TextView) mEmptyTip.findViewById(R.id.tv_empty_tips);
            if (mEmptyTipImage == null)
                mEmptyTipImage = (AppCompatImageView) mEmptyTip.findViewById(R.id.tv_empty_img);
            mEmptyTipTextView.setText(TextUtils.isEmpty(tip) ? "非常抱歉，发生了未知错误" : tip);
            return true;
        }
        return super.showTips(tip, tipType);
    }
}
