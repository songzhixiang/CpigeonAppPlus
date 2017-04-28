package com.cpigeon.app.modular.matchlive.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.view.activity.RaceReportActivity;
import com.cpigeon.app.utils.customview.MarqueeTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/4/18.
 */

public class RaceDetailsFragment extends DialogFragment {
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
    @BindView(R.id.race_detial_match_info_title_caipanzhang)
    TextView raceDetialMatchInfoTitleCaipanzhang;
    @BindView(R.id.race_detial_match_info_content_caipanzhang)
    TextView raceDetialMatchInfoContentCaipanzhang;
    @BindView(R.id.layout_caipanzhang)
    LinearLayout layoutCaipanzhang;
    @BindView(R.id.race_detial_match_info_title_sifangzhang)
    TextView raceDetialMatchInfoTitleSifangzhang;
    @BindView(R.id.race_detial_match_info_content_sifangzhang)
    TextView raceDetialMatchInfoContentSifangzhang;
    @BindView(R.id.layout_sifangzhang)
    LinearLayout layoutSifangzhang;
    @BindView(R.id.layout_captain)
    LinearLayout layoutCaptain;
    @BindView(R.id.race_detial_match_info_title_caipanyuan)
    TextView raceDetialMatchInfoTitleCaipanyuan;
    @BindView(R.id.race_detial_match_info_content_caipanyuan)
    TextView raceDetialMatchInfoContentCaipanyuan;
    @BindView(R.id.layout_caipanyuan)
    LinearLayout layoutCaipanyuan;
    @BindView(R.id.race_detial_match_info_title_slys)
    TextView raceDetialMatchInfoTitleSlys;
    @BindView(R.id.race_detial_match_info_content_slys)
    TextView raceDetialMatchInfoContentSlys;
    @BindView(R.id.layout_slys)
    LinearLayout layoutSlys;
    @BindView(R.id.race_detial_match_info_title_tq)
    TextView raceDetialMatchInfoTitleTq;
    @BindView(R.id.race_detial_match_info_content_tq)
    TextView raceDetialMatchInfoContentTq;
    @BindView(R.id.layout_tq)
    LinearLayout layoutTq;
    @BindView(R.id.race_detial_match_info_title_st)
    TextView raceDetialMatchInfoTitleSt;
    @BindView(R.id.race_detial_match_info_content_st)
    TextView raceDetialMatchInfoContentSt;
    @BindView(R.id.race_detial_match_info_title_jwd)
    TextView raceDetialMatchInfoTitleJwd;
    @BindView(R.id.race_detial_match_info_content_jwd)
    TextView raceDetialMatchInfoContentJwd;
    @BindView(R.id.layout_report_info_detial)
    LinearLayout layoutReportInfoDetial;
    @BindView(R.id.layout_sfzb)
    LinearLayout layoutSfzb;
    private MatchInfo matchInfo;
    private String loadType;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.matchInfo = ((RaceReportActivity) context).getMatchInfo();
        this.loadType = ((RaceReportActivity) context).getLoadType();
    }

    public interface DialogFragmentDataImpl {
        void showMessage(String msg);
    }

    public static RaceDetailsFragment newInstance(String message) {
        //创建一个带有参数的Fragment实例
        RaceDetailsFragment fragment = new RaceDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        fragment.setArguments(bundle);//把参数传递给该DialogFragment
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View customView = LayoutInflater.from(getActivity()).inflate(
                R.layout.list_header_race_detial, null);
        unbinder = ButterKnife.bind(this, customView);
        initView();
        return new AlertDialog.Builder(getActivity()).setView(customView)
                .create();

    }

    private void initView() {
        layoutCaipanyuan.setVisibility(View.GONE);
        layoutCaptain.setVisibility(View.GONE);
        if ("jg".equals(matchInfo.getDt())) {
            raceDetialInfoTextviewRacename.setText("集鸽完毕");
            layoutCaipanyuan.setVisibility(View.GONE);
            layoutCaipanzhang.setVisibility(View.GONE);
            layoutArea.setVisibility(View.GONE);
            layoutKj.setVisibility(View.GONE);
            raceDetialMatchInfoTitleSlys.setText("共计上传:");
            raceDetialMatchInfoContentSlys.setText(matchInfo.getCsys() + "羽");
            layoutTq.setVisibility(View.GONE);
            layoutSfzb.setVisibility(View.GONE);
            raceDetialMatchInfoTitleSt.setText("上传时间:");
            raceDetialMatchInfoContentSt.setText(matchInfo.getSt());
        } else if (!TextUtils.isEmpty(matchInfo.getMc())) {
            raceDetialInfoTextviewRacename.setText(matchInfo.getBsmc());
        }
        if (!TextUtils.isEmpty(matchInfo.getArea())) {
            raceDetialMatchInfoContentArea.setText(matchInfo.getArea());
        }

        raceDetialMatchInfoContentKj.setText(matchInfo.getBskj() + "KM");

        raceDetialMatchInfoContentSlys.setText(matchInfo.getCsys() + "羽");

        if (!TextUtils.isEmpty(matchInfo.getTq())) {
            raceDetialMatchInfoContentTq.setText(matchInfo.getTq());
        }
        if (!TextUtils.isEmpty(matchInfo.getMc())) {
            raceDetialMatchInfoContentSt.setText(matchInfo.getSt());
        }
        if (!TextUtils.isEmpty(matchInfo.getMc())) {
            raceDetialMatchInfoContentJwd.setText(matchInfo.computerSFZB());
        }
        if ("gp".equals(loadType)) {
            layoutCaipanyuan.setVisibility(View.VISIBLE);
            layoutCaptain.setVisibility(View.VISIBLE);
            raceDetialMatchInfoContentCaipanyuan.setText(matchInfo.getCpy());
            raceDetialMatchInfoContentCaipanzhang.setText(matchInfo.getCpz());
            raceDetialMatchInfoContentSifangzhang.setText(matchInfo.getSfz());
        }

    }
}
