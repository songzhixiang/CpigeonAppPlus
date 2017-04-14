package com.cpigeon.app.utils.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cpigeon.app.R;

/**
 * Created by Administrator on 2017/2/8.
 */

public class SearchTitleBar extends CustomTitleBar {
    private LinearLayout titleBarLeftLayout;
    private SearchEditText titleBarSearchEditText;
    private ImageView titleBarLeftImage;

    //自定义属性 start
    private String mSearchText;//文字
    private String mSearchHintText;//文字
    //自定义属性 end

    public SearchTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected  void initAttrs(AttributeSet attrs) {
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTitleBar);
        if (attributes != null) {
            mBackgroundColor = attributes.getColor(R.styleable.CustomTitleBar_title_background_color, Color.parseColor("#3e9cfc"));
            mLeftVisible = attributes.getBoolean(R.styleable.CustomTitleBar_left_visible, true);
            mLeftShowContentMode = attributes.getInt(R.styleable.CustomTitleBar_left_show_mode, 0);
            mLeftText = attributes.getString(R.styleable.CustomTitleBar_left_text);
            mLeftTextColor = attributes.getColor(R.styleable.CustomTitleBar_left_text_color, Color.WHITE);
            mSearchText = attributes.getString(R.styleable.CustomTitleBar_search_text);
            mSearchHintText = attributes.getString(R.styleable.CustomTitleBar_search_hint_text);
            mLeftImageResourceId = attributes.getResourceId(R.styleable.CustomTitleBar_left_drawable, R.drawable.svg_ic_left_stroke);
            attributes.recycle();
        }
    }

    @Override
    protected void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_layout_custom_title_bar_search, this, true);
        titleBarLeftLayout = (LinearLayout) findViewById(R.id.widget_title_bar_left);
        titleBarLeftImage = (ImageView) findViewById(R.id.widget_title_bar_left_image);
        titleBarSearchEditText = (SearchEditText) findViewById(R.id.widget_title_bar_search);
        titleBarSearchEditText.setOnSearchClickListener(new SearchEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view, String keyword) {
                if (onTitleBarClickListener != null && onTitleBarClickListener instanceof OnSearchTitleBarClickListener)
                    ((OnSearchTitleBarClickListener) onTitleBarClickListener).onSearchClick(view, keyword);
            }
        });
        titleBarLeftLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTitleBarClickListener != null) onTitleBarClickListener.onLeftClick();
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

            if (mLeftImageResourceId != -1) {
                titleBarLeftImage.setImageResource(mLeftImageResourceId);
            }

            //设置左边显示方式
            if (mLeftShowContentMode == ShowContentMode.ALL.getVal()) {
                titleBarLeftImage.setVisibility(VISIBLE);
            } else if (mLeftShowContentMode == ShowContentMode.IMAGE.getVal()) {
                titleBarLeftImage.setVisibility(VISIBLE);
            } else if (mLeftShowContentMode == ShowContentMode.TEXT.getVal()) {
                titleBarLeftImage.setVisibility(GONE);
            }
        }
        //处理搜索框
        titleBarSearchEditText.setText(mSearchText);
        titleBarSearchEditText.setHint(mSearchHintText);
        //显示颜色
        //titleBarSearchEditText.setTextColor(mTitleTextColor);
        invalidate();
    }

    public SearchEditText getTitleBarSearchEditText() {
        return this.titleBarSearchEditText;
    }

    public void setOnTitleBarClickListener(OnSearchTitleBarClickListener onSearchTitleBarClickListener) {
        this.onTitleBarClickListener = onSearchTitleBarClickListener;
    }


    public void setSearchText(String text) {
        this.mSearchText = text;
        bindView();

    }

    public void setSearchHintText(String text) {
        this.mSearchHintText = text;
        bindView();

    }

    public void setLeftVisible(boolean visible) {
        this.mLeftVisible = visible;
        bindView();
    }


    public interface OnSearchTitleBarClickListener extends CustomTitleBar.OnTitleBarClickListener {
        void onSearchClick(View view, String keyword);
    }
}