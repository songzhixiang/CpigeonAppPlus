package com.cpigeon.app.modular.footsearch.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.adapter.ContentFragmentAdapter;
import com.cpigeon.app.modular.order.model.bean.CpigeonServicesInfo;
import com.cpigeon.app.modular.footsearch.model.bean.FootQueryResult;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonUserServiceInfo;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.customview.MarqueeTextView;
import com.cpigeon.app.utils.customview.SaActionSheetDialog;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/8.
 */

public class FootSearchResultCardFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mtv_foot_search_key)
    MarqueeTextView mtvFootSearchKey;
    @BindView(R.id.tv_foot_search_result)
    TextView tvFootSearchResult;
    @BindView(R.id.tv_prompt_open_service)
    TextView tvPromptOpenService;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.layout_result_pager)
    LinearLayout layoutResultPager;
    @BindView(R.id.sv_result)
    ScrollView svResult;
    @BindView(R.id.tv_curr_item)
    TextView tvCurrItem;
    @BindView(R.id.iv_last_item)
    ImageView ivLastItem;
    @BindView(R.id.iv_next_item)
    ImageView ivNextItem;
    @BindView(R.id.layout_op)
    RelativeLayout layoutOp;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    private List<FootQueryResult> mData;
    private List<CpigeonServicesInfo> allServicesInfo;
    private String mSkey = "";

    private View mView;
    private ContentFragmentAdapter mContentFragmentAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private SaActionSheetDialog mSaActionSheetDialog = null;
    private int mResultAllCount = 0, mCurrResultCount = 0, mMaxShowCount = 100;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_footsearchresult_card, container, false);
        ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    private void initView() {
        setData(mData);
        mtvFootSearchKey.setText(mSkey);
        checkShowOpenSerivcePrompt();
        mContentFragmentAdapter = new ContentFragmentAdapter(getFragmentManager(), mFragments);

        //设置limit
        mViewPager.setOffscreenPageLimit(4);
        //设置transformer
        //mOrientedViewPager.setPageTransformer(true, new VerticalStackTransformer(mContext));
        mViewPager.setAdapter(mContentFragmentAdapter);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Logger.d("onPageScrolled滑动中" + position + ";" + positionOffset + ";" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Logger.d("onPageSelected选中了" + position);
                tvCurrItem.setText(String.valueOf(position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    //正在滑动   pager处于正在拖拽中

                    Logger.d("onPageScrollStateChanged=======正在滑动" + "SCROLL_STATE_DRAGGING");

                } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                    //pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
                    Logger.d("onPageScrollStateChanged=======自动沉降" + "SCROLL_STATE_SETTLING");

                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                    //空闲状态  pager处于空闲状态
                    Logger.d("onPageScrollStateChanged=======空闲状态" + "SCROLL_STATE_IDLE");
                }
            }
        });
    }

    private void checkNextPager() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
    }

    private void checkLastPager() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
    }

    public void setData(List<FootQueryResult> data) {
        this.mData = data;
        if (mData == null)
            mData = new ArrayList<>();
        setSearchResultCount(mData.size());
        mFragments.clear();
        //制造数据
        for (FootQueryResult footQueryResult : mData) {
            mFragments.add(FootSearchResultFragment.newInstance(footQueryResult));
        }
        if (mContentFragmentAdapter != null)
            mContentFragmentAdapter.setData(mFragments);

        mSaActionSheetDialog = null;
        refreshTipsStatus();
    }

    public void setSearchResultCount(int count) {
        mCurrResultCount = count;
        if (tvFootSearchResult != null) {
            SpannableStringBuilder builder = new SpannableStringBuilder(String.format("共查找到%d条记录", Math.min(mMaxShowCount, mResultAllCount)));
            ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.light_red2));
            builder.setSpan(redSpan, 4,
                    4 + String.valueOf(Math.min(mMaxShowCount, mResultAllCount)).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvFootSearchResult.setText(builder);
        }
    }

    public void refreshTipsStatus() {
        if (mData.size() == 0) {
            if (svResult != null)
                svResult.setVisibility(View.GONE);
            if (layoutOp != null)
                layoutOp.setVisibility(View.GONE);
            if (tvTips != null) {
                SpannableStringBuilder builder = new SpannableStringBuilder(String.format("没有搜索到“%s”\n\n可能原因：\n足环记录库中没有您要找的足环号码", mSkey));
                ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.light_red2));
                builder.setSpan(redSpan, 6, 6 + mSkey.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvTips.setText(builder);
                tvTips.setVisibility(View.VISIBLE);
            }
        } else {
            if (svResult != null)
                svResult.setVisibility(View.VISIBLE);
            if (layoutOp != null)
                layoutOp.setVisibility(View.VISIBLE);
            if (tvTips != null)
                tvTips.setVisibility(View.GONE);
        }
    }

    /**
     * 设置所有结果数，不会在界面上显示
     *
     * @param allCount
     */
    public void setResultAllCount(int allCount) {
        this.mResultAllCount = allCount;
        checkShowOpenSerivcePrompt();
    }

    public void setSearchKey(String key) {
        Logger.i(key);
        this.mSkey = key;
        if (mtvFootSearchKey != null)
            mtvFootSearchKey.setText(key);
    }

    /**
     * 设置最大显示数
     *
     * @param count
     */
    public void setMaxShowCount(int count) {
        this.mMaxShowCount = count;
        checkShowOpenSerivcePrompt();
    }

    public void setAllServicesInfo(List<CpigeonServicesInfo> allServicesInfo) {
        this.allServicesInfo = allServicesInfo;
        checkShowOpenSerivcePrompt();
    }

    private void checkShowOpenSerivcePrompt() {
        Logger.d("mResultAllCount=" + mResultAllCount + ";mData.size()=" + (mData == null ? -1 : mData.size()));
        if (tvPromptOpenService == null) return;
        CpigeonUserServiceInfo userPackageData = CpigeonData.getInstance().getUserFootSearchServiceInfo();
        if (userPackageData == null || userPackageData.getNumbers() <= 0) {
            //未购买套餐，或套餐已用完
            Logger.d("未购买套餐，或套餐已用完");
            //当前只能查看2条，开通套餐，查看更多
            tvPromptOpenService.setText(String.format("当前最多能查看%d条，开通套餐，查看更多", mData.size()));
            tvPromptOpenService.setVisibility(View.VISIBLE);
        }
        //判断当前所有记录是否显示完
        else if ((mData.size() < mMaxShowCount && mResultAllCount > mData.size())) {
            //未显示完，提示升级套餐
            Logger.d("未显示完，提示升级套餐");
            //当前只能查看5条，升级套餐，查看更多
            tvPromptOpenService.setText(String.format("当前最多能查看%d条，升级套餐，查看更多", mData.size()));
        } else if ((mData.size() != 0 && mData.get(0).getName() == null)) {
            //提示显示姓名需要升级套餐
            Logger.d("升级套餐，查看更多信息");
            //当前只能查看5条，升级套餐，查看更多
            tvPromptOpenService.setText(String.format("升级套餐，查看更多相关信息"));
        } else {
            //显示完
            Logger.d("显示完");
            //tv_prompt_open_service.setText("开通查询套餐，查看更多记录");
            tvPromptOpenService.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_prompt_open_service, R.id.iv_last_item, R.id.iv_next_item, R.id.tv_curr_item})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_prompt_open_service:
//                Intent intent = new Intent(mContext, OpenSeriveActivity.class);
//                intent.putExtra(OpenSeriveActivity.INTENT_DATA_KEY_SERVICENAME, "足环查询服务");
//                startActivity(intent);
                break;
            case R.id.iv_last_item:
                checkLastPager();
                break;
            case R.id.iv_next_item:
                checkNextPager();
                break;
            case R.id.tv_curr_item:
                if (mSaActionSheetDialog == null) {
                    mSaActionSheetDialog = new SaActionSheetDialog(getActivity()).builder();
                    for (int i = 0; i < mData.size(); i++) {
                        mSaActionSheetDialog.addSheetItem(String.valueOf(i + 1), SaActionSheetDialog.SheetItemColor.Blue, new SaActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Logger.i(String.valueOf(which));
                                mViewPager.setCurrentItem(which - 1);
                            }
                        });
                    }
                    mSaActionSheetDialog.setCanceledOnTouchOutside(false);
                }
                mSaActionSheetDialog.show();
                break;
        }
    }
}
