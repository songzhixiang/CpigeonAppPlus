package com.cpigeon.app.utils.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/2/9.
 */

public abstract class CustomTitleBar extends RelativeLayout {
    //自定义属性 start
    boolean mRightVisible = true;//右边是否显示
    boolean mLeftVisible = true;//左边是否显示
    int mLeftShowContentMode = CommonTitleBar.ShowContentMode.ALL.getVal();
    int mRightShowContentMode = CommonTitleBar.ShowContentMode.ALL.getVal();
    int mBackgroundColor;//背景颜色值
    int mLeftTextColor;
    int mRightTextColor;
    int mTitleTextColor;
    int mLeftImageResourceId = -1;
    int mRightImageResourceId = -1;
    String mRightText;//右边文字
    String mLeftText;//左边文字
    String mTitleText;//标题文字
    //自定义属性 end

    OnTitleBarClickListener onTitleBarClickListener;

    public CustomTitleBar(Context context) {
        super(context);
        init();
    }

    public CustomTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    public CustomTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    void init() {
        initView();
        bindView();
    }

    public boolean isRightVisible() {
        return mRightVisible;
    }

    public boolean isLeftVisible() {
        return mLeftVisible;
    }

    public int getLeftShowContentMode() {
        return mLeftShowContentMode;
    }

    public int getRightShowContentMode() {
        return mRightShowContentMode;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public int getLeftTextColor() {
        return mLeftTextColor;
    }

    public int getRightTextColor() {
        return mRightTextColor;
    }

    public int getTitleTextColor() {
        return mTitleTextColor;
    }

    public int getLeftImageResourceId() {
        return mLeftImageResourceId;
    }

    public int getRightImageResourceId() {
        return mRightImageResourceId;
    }

    public String getRightText() {
        return mRightText;
    }

    public String getLeftText() {
        return mLeftText;
    }

    public String getTitleText() {
        return mTitleText;
    }

    protected abstract void initAttrs(AttributeSet attrs);

    protected abstract void initView();

    protected abstract void bindView();

    public void setOnTitleBarClickListener(OnTitleBarClickListener listener) {
        this.onTitleBarClickListener = listener;
    }

    public enum ShowContentMode {
        ALL(0), TEXT(1), IMAGE(2);
        int val;

        ShowContentMode(int v) {
            this.val = v;
        }

        public int getVal() {
            return val;
        }
    }

    public interface OnTitleBarClickListener {
        void onLeftClick();
    }
}
