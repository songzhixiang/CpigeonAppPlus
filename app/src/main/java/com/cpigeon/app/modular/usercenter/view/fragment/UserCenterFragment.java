package com.cpigeon.app.modular.usercenter.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.modular.usercenter.view.fragment.viewdao.IUserCenterView;
import com.cpigeon.app.modular.usercenter.model.bean.UserInfo;
import com.cpigeon.app.modular.usercenter.presenter.UserCenterPre;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.customview.MarqueeTextView;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.cpigeon.app.MyApp.mCpigeonData;

/**
 * Created by Administrator on 2017/4/6.
 */

public class UserCenterFragment extends BaseFragment implements IUserCenterView {

    @BindView(R.id.fragment_user_center_userLogo)
    CircleImageView fragmentUserCenterUserLogo;
    @BindView(R.id.fragment_user_center_userName)
    TextView fragmentUserCenterUserName;
    @BindView(R.id.fragment_user_center_details)
    TextView fragmentUserCenterDetails;
    @BindView(R.id.tv_sign_status)
    TextView tvSignStatus;
    @BindView(R.id.cv_sign)
    CardView cvSign;
    @BindView(R.id.fragment_user_center_userInfo_layout)
    RelativeLayout fragmentUserCenterUserInfoLayout;
    @BindView(R.id.ll_user_center_msg)
    LinearLayout llUserCenterMsg;
    @BindView(R.id.marqueeTextView)
    MarqueeTextView marqueeTextView;
    @BindView(R.id.ll_user_center_feelback)
    LinearLayout llUserCenterFeelback;
    @BindView(R.id.ll_user_center_focus)
    LinearLayout llUserCenterFocus;
    @BindView(R.id.ll_user_center_order)
    LinearLayout llUserCenterOrder;
    @BindView(R.id.tv_user_money)
    TextView tvUserMoney;
    @BindView(R.id.ll_user_money)
    LinearLayout llUserMoney;
    @BindView(R.id.tv_user_jifen)
    TextView tvUserJifen;
    @BindView(R.id.ll_user_jifen)
    LinearLayout llUserJifen;
    @BindView(R.id.ll_user_center_setting)
    LinearLayout llUserCenterSetting;
    @BindView(R.id.ll_user_center_aboutus)
    LinearLayout llUserCenterAboutus;
    @BindView(R.id.ll_user_center_help)
    LinearLayout llUserCenterHelp;
    private View mView;
    private UserCenterPre userCenterPre = new UserCenterPre(this);
    CpigeonData.OnDataChangedListener onDataChangedListener = new CpigeonData.OnDataChangedListener() {
        @Override
        public void OnDataChanged(CpigeonData cpigeonData) {
            refreshUserInfo();
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_usercenter, container, false);
        ButterKnife.bind(this, mView);

        initView();
        return mView;
    }

    private void initView() {

        userCenterPre.loadBalance();
        userCenterPre.loadSignStatus();
        mCpigeonData.addOnDataChangedListener(onDataChangedListener);

    }

    @OnClick({R.id.fragment_user_center_details, R.id.cv_sign, R.id.ll_user_center_msg, R.id.ll_user_center_focus, R.id.ll_user_center_order, R.id.ll_user_money, R.id.ll_user_jifen, R.id.ll_user_center_setting, R.id.ll_user_center_aboutus, R.id.ll_user_center_help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_user_center_details:
                if (checkLogin()) {
                }
                break;
            case R.id.cv_sign:
                break;
            case R.id.ll_user_center_msg:
                break;
            case R.id.ll_user_center_focus:
                break;
            case R.id.ll_user_center_order:
                break;
            case R.id.ll_user_money:
                break;
            case R.id.ll_user_jifen:
                break;
            case R.id.ll_user_center_setting:
                break;
            case R.id.ll_user_center_aboutus:
                break;
            case R.id.ll_user_center_help:
                break;
        }
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @Override
    public void showUserInfo(Map<String, Object> data) {
        if (checkLogin()) {
            mCpigeonData.setUserBalance((double) data.get("yue"));
            mCpigeonData.setUserScore((int) data.get("jifen"));
            refreshUserInfo();
        }
    }

    @Override
    public void isSign(Boolean data) {
        int userSignStatus = mCpigeonData.getUserSignStatus();
        if (userSignStatus == CpigeonData.USER_SIGN_STATUS_NONE) {
            if (data)
            {
                CpigeonData.getInstance().setUserSignStatus(data ? CpigeonData.USER_SIGN_STATUS_SIGNED : CpigeonData.USER_SIGN_STATUS_NOT_SIGN);
                tvSignStatus.setText(data ? "已签到" : "签到");
            }

        } else {
            tvSignStatus.setText(userSignStatus == CpigeonData.USER_SIGN_STATUS_SIGNED ? "已签到" : "签到");
        }

    }

    private void refreshUserInfo() {
        if (checkLogin()) {
            UserInfo.DataBean userInfo = mCpigeonData.getUserInfo();
            String userHeadImageURl = "", nickName = "";
            if (userInfo == null) {
                Map<String, Object> map = getLoginUserInfo();
                if (map.get("touxiang") != null && (!map.get("touxiang").equals("null") || !map.get("touxiang").equals(""))) {
                    userHeadImageURl = (String) map.get("touxiangurl");
                }
                Logger.d(String.format("%s  %s", map.get("nicheng"), map.get("username")));
                if (map.get("nicheng") != null && (!TextUtils.isEmpty(map.get("nicheng").toString()))) {
                    nickName = map.get("nicheng").toString();
                } else {
                    nickName = map.get("username").toString();
                }
            } else {
                userHeadImageURl = userInfo.getHeadimg();
                nickName = TextUtils.isEmpty(userInfo.getNickname()) ? userInfo.getUsername() : userInfo.getNickname();
//              更新用户缓存数据
                if (userHeadImageURl != null)
                    SharedPreferencesTool.Save(getActivity(), "touxiangurl", userHeadImageURl, SharedPreferencesTool.SP_FILE_LOGIN);
                if (userInfo.getNickname() != null)
                    SharedPreferencesTool.Save(getActivity(), "nicheng", userInfo.getNickname(), SharedPreferencesTool.SP_FILE_LOGIN);
            }

            if (!TextUtils.isEmpty(userHeadImageURl)) {
//                ImageOptions options = new ImageOptions.Builder()
//                        .setSize(DensityUtil.dip2px(50), DensityUtil.dip2px(50))//图片大小
//                        .setRadius(DensityUtil.dip2px(50))//ImageView圆角半径
//                        // .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
//                        .setImageScaleType(CircleImageView.ScaleType.CENTER_CROP)//缩放
//                        .setLoadingDrawableId(R.mipmap.head_image_default)//加载中默认显示图片
//                        .setUseMemCache(true)//设置使用缓存
//                        .setFailureDrawableId(R.mipmap.head_image_default)//加载失败后默认显示图片
//                        .build();
//                x.image().bind(fragmentUserCenterUserLogo, userHeadImageURl, options);
                Picasso.with(getActivity())
                        .load(userHeadImageURl)
                        .into(fragmentUserCenterUserLogo);
            }
            final String name = nickName;
            fragmentUserCenterUserName.setText(name);
        } else {
            fragmentUserCenterUserLogo.setImageResource(R.mipmap.head_image_default);
            fragmentUserCenterDetails.setText(getString(R.string.please_login));
        }
        if (fragmentUserCenterDetails != null) {
            fragmentUserCenterDetails.setVisibility(checkLogin() ? View.VISIBLE : View.GONE);
        }
        //余额

        tvUserMoney.setText(String.format("%.2f", mCpigeonData.getUserBalance()));

        tvUserJifen.setText(String.format("%d", mCpigeonData.getUserScore()));

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUserInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCpigeonData.removeOnDataChangedListener(onDataChangedListener);
    }
}
