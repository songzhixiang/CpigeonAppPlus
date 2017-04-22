package com.cpigeon.app.modular.footsearch.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.modular.footsearch.model.bean.FootQueryResult;
import com.cpigeon.app.utils.customview.MarqueeTextView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/8.
 */

public class FootSearchResultCardFragment extends BaseFragment {
    private static final String BUNDLE_FOOT_QUERY_RESULT = "result_data";
    @BindView(R.id.tv_card_title_foot)
    TextView tvCardTitleFoot;
    @BindView(R.id.tv_card_content_foot)
    MarqueeTextView tvCardContentFoot;
    @BindView(R.id.tv_card_title_name)
    TextView tvCardTitleName;
    @BindView(R.id.tv_card_content_name)
    MarqueeTextView tvCardContentName;
    @BindView(R.id.layout_name)
    LinearLayout layoutName;
    @BindView(R.id.tv_card_title_xm)
    TextView tvCardTitleXm;
    @BindView(R.id.tv_card_content_xm)
    MarqueeTextView tvCardContentXm;
    @BindView(R.id.tv_card_title_org)
    TextView tvCardTitleOrg;
    @BindView(R.id.tv_card_content_org)
    MarqueeTextView tvCardContentOrg;
    @BindView(R.id.tv_card_title_bskj)
    TextView tvCardTitleBskj;
    @BindView(R.id.tv_card_content_bskj)
    MarqueeTextView tvCardContentBskj;
    @BindView(R.id.tv_card_title_time)
    TextView tvCardTitleTime;
    @BindView(R.id.tv_card_content_time)
    MarqueeTextView tvCardContentTime;
    @BindView(R.id.tv_card_title_csys)
    TextView tvCardTitleCsys;
    @BindView(R.id.tv_card_content_csys)
    MarqueeTextView tvCardContentCsys;
    @BindView(R.id.tv_card_title_mc)
    TextView tvCardTitleMc;
    @BindView(R.id.tv_card_content_mc)
    MarqueeTextView tvCardContentMc;
    @BindView(R.id.tv_card_title_speed)
    TextView tvCardTitleSpeed;
    @BindView(R.id.tv_card_content_speed)
    MarqueeTextView tvCardContentSpeed;

    private FootQueryResult mFootQueryResult;


    @Override
    protected void initView(View view) {
        if (mFootQueryResult != null) {
            tvCardContentFoot.setText(mFootQueryResult.getFoot());
            tvCardContentXm.setText(mFootQueryResult.getXmmc());
            tvCardContentOrg.setText(mFootQueryResult.getOrgname());
            tvCardContentBskj.setText(mFootQueryResult.getBskj() + "KM");
            tvCardContentTime.setText(mFootQueryResult.getSt());
            tvCardContentCsys.setText(mFootQueryResult.getCsys() + "羽");
            tvCardContentMc.setText(String.format("%d", mFootQueryResult.getMc()));
            tvCardContentSpeed.setText(mFootQueryResult.getSpeed() + "米/分");
            if (mFootQueryResult.getName() != null) {
                tvCardContentName.setText(mFootQueryResult.getName());
            } else {
                layoutName.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_footsearch_result_card;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mFootQueryResult = (FootQueryResult) bundle.getSerializable(BUNDLE_FOOT_QUERY_RESULT);
        }
    }

    public static FootSearchResultCardFragment newInstance(FootQueryResult footQueryResult) {
        FootSearchResultCardFragment fragment = new FootSearchResultCardFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_FOOT_QUERY_RESULT, footQueryResult);
        fragment.setArguments(bundle);
        return fragment;
    }

}
