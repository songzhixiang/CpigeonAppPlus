package com.cpigeon.app.utils.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.app.R;


/**
 * Created by Administrator on 2017/2/8.
 */

public class CommonTitleBar extends CustomTitleBar {
    private LinearLayout titleBarLeftLayout;
    private RelativeLayout titleBarRightLayout;
    private TextView titleBarLeftText;
    private TextView titleBarRightText;
    private ImageView titleBarRightImage;
    private ImageView titleBarLeftImage;
    private TextView titleBarTitle;

    public CommonTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected  void initAttrs(AttributeSet attrs) {
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTitleBar);
        if (attributes != null) {
            mBackgroundColor = attributes.getColor(R.styleable.CustomTitleBar_title_background_color, Color.parseColor("#3e9cfc"));
            mLeftVisible = attributes.getBoolean(R.styleable.CustomTitleBar_left_visible, true);
            mRightVisible = attributes.getBoolean(R.styleable.CustomTitleBar_right_visible, false);
            mLeftShowContentMode = attributes.getInt(R.styleable.CustomTitleBar_left_show_mode, 0);
            mRightShowContentMode = attributes.getInt(R.styleable.CustomTitleBar_right_show_mode, 0);
            mLeftText = attributes.getString(R.styleable.CustomTitleBar_left_text);
            mRightText = attributes.getString(R.styleable.CustomTitleBar_right_text);
            mTitleText = attributes.getString(R.styleable.CustomTitleBar_title_text);
            mLeftTextColor = attributes.getColor(R.styleable.CustomTitleBar_left_text_color, Color.WHITE);
            mRightTextColor = attributes.getColor(R.styleable.CustomTitleBar_right_text_color, Color.WHITE);
            mTitleTextColor = attributes.getColor(R.styleable.CustomTitleBar_title_text_color, Color.WHITE);
            mLeftImageResourceId = attributes.getResourceId(R.styleable.CustomTitleBar_left_drawable, R.drawable.svg_ic_left_stroke);
            mRightImageResourceId = attributes.getResourceId(R.styleable.CustomTitleBar_right_drawable, -1);
            attributes.recycle();
        }
    }

    @Override
    protected void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_layout_custom_title_bar, this, true);
        titleBarLeftLayout = (LinearLayout) findViewById(R.id.widget_title_bar_left);
        titleBarRightLayout = (RelativeLayout) findViewById(R.id.widget_title_bar_right);
        titleBarLeftText = (TextView) findViewById(R.id.widget_title_bar_left_text);
        titleBarRightText = (TextView) findViewById(R.id.widget_title_bar_right_text);
        titleBarLeftImage = (ImageView) findViewById(R.id.widget_title_bar_left_image);
        titleBarRightImage = (ImageView) findViewById(R.id.widget_title_bar_right_image);
        titleBarTitle = (TextView) findViewById(R.id.widget_title_bar_title);
        titleBarLeftLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTitleBarClickListener != null) onTitleBarClickListener.onLeftClick();
            }
        });
        titleBarRightLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTitleBarClickListener != null && onTitleBarClickListener instanceof OnTitleBarClickListener)
                    ((OnTitleBarClickListener) onTitleBarClickListener).onRightClick();
            }
        });
        titleBarTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTitleBarClickListener != null && onTitleBarClickListener instanceof OnTitleBarClickListener)
                    ((OnTitleBarClickListener) onTitleBarClickListener).onTitleClick();
            }
        });
    }

    @Override
    protected void bindView() {
        //处理titleBar背景色
        setBackgroundColor(mBackgroundColor);
        //先处理左边
        //获取是否要显示左边
        titleBarLeftLayout.setVisibility(mLeftVisible ? View.VISIBLE : GONE);
        if (mLeftVisible) {
            //设置左边的文字
            titleBarLeftText.setText(mLeftText);
            //设置左边文字颜色
            titleBarLeftText.setTextColor(mLeftTextColor);
            if (mLeftImageResourceId != -1) {
                titleBarLeftImage.setImageResource(mLeftImageResourceId);
            }
            //设置左边显示方式
            if (mLeftShowContentMode == ShowContentMode.ALL.getVal()) {
                titleBarLeftImage.setVisibility(VISIBLE);
                titleBarLeftText.setVisibility(VISIBLE);
            } else if (mLeftShowContentMode == ShowContentMode.IMAGE.getVal()) {
                titleBarLeftImage.setVisibility(VISIBLE);
                titleBarLeftText.setVisibility(GONE);
            } else if (mLeftShowContentMode == ShowContentMode.TEXT.getVal()) {
                titleBarLeftImage.setVisibility(GONE);
                titleBarLeftText.setVisibility(VISIBLE);
            }
        }

        //处理标题
        if (!TextUtils.isEmpty(mTitleText)) {
            titleBarTitle.setText(mTitleText);
            //标题显示颜色
            titleBarTitle.setTextColor(mTitleTextColor);
        }

        //先处理右边
        //是否要显示右边
        titleBarRightLayout.setVisibility(mRightVisible ? View.VISIBLE : GONE);

        if (mRightVisible) {
            //设置右边的文字
            titleBarRightText.setText(mRightText);
            //设置右边文字颜色
            titleBarRightText.setTextColor(mRightTextColor);
            if (mRightImageResourceId != -1) {
                titleBarRightImage.setImageResource(mRightImageResourceId);
            }
            //设置右边显示方式
            if (mRightShowContentMode == ShowContentMode.ALL.getVal()) {
                titleBarRightImage.setVisibility(VISIBLE);
                titleBarRightText.setVisibility(VISIBLE);
            } else if (mRightShowContentMode == ShowContentMode.IMAGE.getVal()) {
                titleBarRightImage.setVisibility(VISIBLE);
                titleBarRightText.setVisibility(GONE);
            } else if (mRightShowContentMode == ShowContentMode.TEXT.getVal()) {
                titleBarRightImage.setVisibility(GONE);
                titleBarRightText.setVisibility(VISIBLE);
            }
        }

    }
    public  void setLeftShowContentMode(int showContentMode) {
        this.mLeftShowContentMode = showContentMode;
        bindView();invalidate();
    }

    public void setRightShowContentMode(int showContentMode) {
        this.mRightShowContentMode = showContentMode;
        bindView();invalidate();
    }

    public void setLeftTextColor(int color) {
        this.mLeftTextColor = color;
        bindView();invalidate();
    }

    public void setRightTextColor(int color) {
        this.mRightTextColor = color;
        bindView();invalidate();
    }

    public void setTitleTextColor(int color) {
        this.mTitleTextColor = color;
        bindView();invalidate();
    }

    public void setLeftImageResourceId(int resourceId) {
        this.mLeftImageResourceId = resourceId;
        bindView();invalidate();
    }

    public void setRightImageResourceId(int resourceId) {
        this.mRightImageResourceId = resourceId;
        bindView();invalidate();
    }

    public void setRightText(String text) {
        this.mRightText = text;
        bindView();invalidate();
    }

    public void setLeftText(String text) {
        this.mLeftText = text;
        bindView();invalidate();
    }

    public void setTitleText(String text) {
        this.mTitleText = text;
        bindView();invalidate();
    }

    public void setLeftVisible(boolean visible) {
        this.mLeftVisible = visible;
        bindView();invalidate();
    }

    public void setRightVisible(boolean visible) {
        this.mRightVisible = visible;
        bindView();invalidate();
    }

    public LinearLayout getTitleBarLeftLayout() {
        return titleBarLeftLayout;
    }

    public RelativeLayout getTitleBarRightLayout() {
        return titleBarRightLayout;
    }

    public TextView getTitleBarLeftText() {
        return titleBarLeftText;
    }

    public TextView getTitleBarRightText() {
        return titleBarRightText;
    }

    public ImageView getTitleBarRightImage() {
        return titleBarRightImage;
    }

    public ImageView getTitleBarLeftImage() {
        return titleBarLeftImage;
    }

    public TextView getTitleBarTitle() {
        return titleBarTitle;
    }

    public void setOnTitleBarClickListener(OnTitleBarClickListener onTitleBarClickListener) {
        this.onTitleBarClickListener = onTitleBarClickListener;
    }

    public interface OnTitleBarClickListener extends CustomTitleBar.OnTitleBarClickListener {

        void onRightClick();

        void onTitleClick();
    }
}