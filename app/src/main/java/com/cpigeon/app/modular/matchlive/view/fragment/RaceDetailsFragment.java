package com.cpigeon.app.modular.matchlive.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    Unbinder unbinder;
    private MatchInfo matchInfo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.matchInfo = ((RaceReportActivity) context).getMatchInfo();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                R.layout.list_header_race_detial_xh, null);
        unbinder = ButterKnife.bind(this, customView);
        initView();
        return new AlertDialog.Builder(getActivity()).setView(customView)
                .create();

    }

    private void initView() {
        if (!"".equals(matchInfo.getMc()))
        {
            raceDetialInfoTextviewRacename.setText(matchInfo.getBsmc());
        }
        if (!"".equals(matchInfo.getArea()))
        {
            raceDetialMatchInfoContentArea.setText(matchInfo.getArea());
        }

        raceDetialMatchInfoContentKj.setText(matchInfo.getBskj()+"KM");

        raceDetialMatchInfoContentSlys.setText(matchInfo.getCsys()+"羽");

        if (!"".equals(matchInfo.getTq()))
        {
            raceDetialMatchInfoContentTq.setText(matchInfo.getTq());
        }
        if (!"".equals(matchInfo.getMc()))
        {
            raceDetialMatchInfoContentSt.setText(matchInfo.getSt());
        }
        if (!"".equals(matchInfo.getMc()))
        {
            raceDetialMatchInfoContentJwd.setText(matchInfo.computerSFZB());
        }

    }
}
