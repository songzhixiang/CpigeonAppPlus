package com.cpigeon.app.modular.matchlive.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.view.activity.RaceReportActivity;
import com.cpigeon.app.modular.matchlive.view.activity.RaceXunFangActivity;
import com.cpigeon.app.utils.customview.MarqueeTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/4/28.
 */

public class RaceDetailsXunFangFragment extends DialogFragment {
    Unbinder unbinder;
    @BindView(R.id.race_detial_info_textview_racename)
    MarqueeTextView raceDetialInfoTextviewRacename;
    @BindView(R.id.race_detial_match_info_title_area)
    TextView raceDetialMatchInfoTitleArea;
    @BindView(R.id.race_detial_match_info_content_area)
    TextView raceDetialMatchInfoContentArea;
    @BindView(R.id.layout_area)
    LinearLayout layoutArea;
    @BindView(R.id.race_detial_match_info_title_kj)
    TextView raceDetialMatchInfoTitleKj;
    @BindView(R.id.race_detial_match_info_content_kj)
    TextView raceDetialMatchInfoContentKj;
    @BindView(R.id.layout_kj)
    LinearLayout layoutKj;
    @BindView(R.id.race_detial_match_info_title_st)
    TextView raceDetialMatchInfoTitleSt;
    @BindView(R.id.race_detial_match_info_content_st)
    TextView raceDetialMatchInfoContentSt;
    @BindView(R.id.layout_report_info_detial)
    LinearLayout layoutReportInfoDetial;
    @BindView(R.id.race_detial_info_close)
    AppCompatImageView raceDetialInfoClose;
    private MatchInfo matchInfo;

    @OnClick(R.id.race_detial_info_close)
    public void onViewClicked() {
        getDialog().dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View customView = LayoutInflater.from(getActivity()).inflate(
                R.layout.list_header_xunfang_detial, null);
        unbinder = ButterKnife.bind(this, customView);
        initView();
        return new AlertDialog.Builder(getActivity()).setView(customView)
                .create();
    }

    private void initView() {
        raceDetialInfoTextviewRacename.setText(matchInfo.computerBSMC());
        raceDetialMatchInfoContentArea.setText(!TextUtils.isEmpty(matchInfo.getArea()) ? matchInfo.getArea() : "无");
        raceDetialMatchInfoContentSt.setText(matchInfo.getSt());
        if (matchInfo.getBskj() == 0)//家飞
        {
            layoutKj.setVisibility(View.GONE);
        } else {
            layoutKj.setVisibility(View.VISIBLE);
            raceDetialMatchInfoContentKj.setText(matchInfo.getBskj() + "Km");
        }
    }


    public interface DialogFragmentDataImpl {
        void showMessage(String msg);
    }

    public static RaceDetailsXunFangFragment newInstance(String message) {
        //创建一个带有参数的Fragment实例
        RaceDetailsXunFangFragment fragment = new RaceDetailsXunFangFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        fragment.setArguments(bundle);//把参数传递给该DialogFragment
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.matchInfo = ((RaceXunFangActivity) context).getMatchInfo();

    }
}
