package com.cpigeon.modular.CpigeonOrder.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.activity.WebActivity;
import com.cpigeon.modular.CommonStandard.base.BaseDialogFragment;
import com.cpigeon.modular.CpigeonUsers.activity.ResetPayPwdActivity;
import com.cpigeon.tools.CPigeonApiUrl;
import com.cpigeon.tools.CpigeonConfig;
import com.martin.widget.SaAlertDialog;

/**
 * Created by Administrator on 2017/1/7.
 * 支付密码输入Fragment
 */

public class PayPwdInputFragment extends BaseDialogFragment {

    private ImageView iv_pay_close;
    private EditText et_paypwd;
    private TextView tv_pay;
    private TextView tv_mean_for_paypwd;
    private TextView tv_forget_paypwd;
    private TextView tv_yue_prompt;
    private OnPayListener onPayListener;
    private SaAlertDialog pwdPromptDialog;
    private String promptInfo;
    private String okText="付款";

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_pay_close:
                    getDialog().dismiss();
                    break;
                case R.id.tv_pay:
                    String pwd = et_paypwd.getText().toString();
                    if (TextUtils.isEmpty(pwd)) {
                        pwdPromptDialog.setMsg("请输入密码");
                        pwdPromptDialog.show();

                    } else if (pwd.length() > 12 || pwd.length() < 6) {
                        pwdPromptDialog.setMsg("请输入6~12位的支付密码");
                        pwdPromptDialog.show();
                    } else if (onPayListener != null) {
                        onPayListener.onPay(getDialog(), pwd);
                    }
                    break;
                case R.id.tv_mean_for_paypwd:
                    Intent intent = new Intent(mContext, WebActivity.class);
                    String url = CPigeonApiUrl.getInstance().getServer() + "/APP/Help?type=help&id=172";
                    intent.putExtra(WebActivity.INTENT_DATA_KEY_URL, url);
                    intent.putExtra(WebActivity.INTENT_DATA_KEY_BACKNAME, "付款");
                    startActivity(intent);
                    break;
                case R.id.tv_forget_paypwd:
                    startActivity(new Intent(mContext, ResetPayPwdActivity.class));
                    break;
            }

        }
    };

    /**
     * 设置提示内容
     *
     * @param prompt
     */
    public void setPromptInfo(String prompt) {
        this.promptInfo = prompt;
        if (tv_yue_prompt != null)
            tv_yue_prompt.setText(promptInfo);
    }

    /**
     * 设置付款按钮文本，默认“付款”
     *
     * @param text
     */
    public void setOkText(String text) {
        this.okText = text;
        if (tv_pay != null)
            tv_pay.setText(okText);
    }

    public void setOnPayListener(OnPayListener onPayListener) {
        this.onPayListener = onPayListener;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View rootView) {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.dialogfragment_pay);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        assert window != null;
        window.setWindowAnimations(R.style.AnimBottomDialog);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 2 / 5;
        window.setAttributes(lp);
        initView(dialog);

        return dialog;
    }

    private void initView(Dialog dialog) {
        iv_pay_close = (ImageView) dialog.findViewById(R.id.iv_pay_close);
        et_paypwd = (EditText) dialog.findViewById(R.id.et_paypwd);
        tv_pay = (TextView) dialog.findViewById(R.id.tv_pay);
        tv_mean_for_paypwd = (TextView) dialog.findViewById(R.id.tv_mean_for_paypwd);
        tv_forget_paypwd = (TextView) dialog.findViewById(R.id.tv_forget_paypwd);
        tv_yue_prompt = (TextView) dialog.findViewById(R.id.tv_yue_prompt);

        tv_pay.setText(okText);
        tv_yue_prompt.setText(promptInfo);

        iv_pay_close.setOnClickListener(clickListener);
        tv_pay.setOnClickListener(clickListener);
        tv_mean_for_paypwd.setOnClickListener(clickListener);
        tv_forget_paypwd.setOnClickListener(clickListener);

        pwdPromptDialog = new SaAlertDialog(mContext)
                .builder()
                .setTitle("提示")
                .setMsg("请输入支付密码")
                .setCancelable(false)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        et_paypwd.requestFocus();
                    }
                });
    }


    public interface OnPayListener {
        void onPay(Dialog dialog, String payPwd);
    }
}
