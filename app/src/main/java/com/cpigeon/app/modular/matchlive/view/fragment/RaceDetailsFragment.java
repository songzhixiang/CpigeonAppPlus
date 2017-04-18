package com.cpigeon.app.modular.matchlive.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.cpigeon.app.R;

/**
 * Created by Administrator on 2017/4/18.
 */

public class RaceDetailsFragment extends DialogFragment {



    public interface DialogFragmentDataImpl{
        void showMessage(String msg);
    }

    public static RaceDetailsFragment newInstance(String message){
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

        return new AlertDialog.Builder(getActivity()).setView(customView)
                .create();

    }
}
