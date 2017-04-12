package com.cpigeon.app.modular.usercenter.view.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.cpigeon.app.R;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/3/7.
 */

public class MyDialogFragment extends DialogFragment {
    public static final int DIALOG_TYPE_DATE = 2;

    public static MyDialogFragment getInstance(int type) {
        MyDialogFragment myDialogFragment = new MyDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("Dialog_Type", type);
        myDialogFragment.setArguments(bundle);
        return myDialogFragment;
    }

    /**
     * 创建一个Dialog并返回
     *
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = null;
        int dialog_type = getArguments().getInt("Dialog_Type");
        switch (dialog_type) {
            case DIALOG_TYPE_DATE:
                Calendar c1 = Calendar.getInstance();
                int year = c1.get(Calendar.YEAR);
                int month = c1.get(Calendar.MONTH);
                int day = c1.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectYear, int selectMonthOfYear, int selectDayOfMonth) {
//                        Toast.makeText(getActivity(), selectYear + "-" + (selectMonthOfYear + 1) + "-" + selectDayOfMonth , Toast.LENGTH_SHORT).show();
                        TextView mTextView = (TextView) getActivity().findViewById(R.id.tv_user_brithday);
                        mTextView.setText(selectYear + "-" + (selectMonthOfYear + 1) + "-" + selectDayOfMonth);

                    }
                }, year, month, day);
        }
        return dialog;
    }
}


