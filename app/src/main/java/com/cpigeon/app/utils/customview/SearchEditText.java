package com.cpigeon.app.utils.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.cpigeon.app.R;


public class SearchEditText extends EditText implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {
    private static final String TAG = "SearchEditText";
    /**
     * 图标是否默认在左边
     */
    private boolean isIconLeft = false;
    /**
     * 是否点击软键盘搜索
     */
    private boolean pressSearch = false;
    /**
     * 软键盘搜索键监听
     */
    private OnSearchClickListener listener;

    private Drawable[] drawables; // 控件的图片资源
    private Drawable drawableTempDel;// 搜索图标和删除按钮图标
    private Drawable drawableSearch, drawableDel;
    private int eventX, eventY; // 记录点击坐标
    private int mSearchgravityIndex;
    private Rect rect; // 控件区域

//    private int icon_search_paddingleft = 0;
//    private int icon_search_paddingright = 0;
//    private int icon_search_paddingtop = 0;
//    private int icon_search_paddingbottom = 0;
//
//    private int icon_delete_paddingleft = 0;
//    private int icon_delete_paddingtop = 0;
//    private int icon_delete_paddingbottom = 0;
//    private int icon_delete_paddingright = 0;

    public SearchEditText(Context context) {
        this(context, null);
        init();
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
        initParams(context, attrs);
        init();
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(context, attrs);
        init();
    }

    public void setOnSearchClickListener(OnSearchClickListener listener) {
        this.listener = listener;
    }

    private void initParams(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchEditText);
        if (typedArray != null) {
            drawableDel = typedArray.getDrawable(R.styleable.SearchEditText_drawableDel);
            mSearchgravityIndex = typedArray.getInt(R.styleable.SearchEditText_search_gravity, 1);
//            int padding = typedArray.getDimensionPixelSize(R.styleable.SearchEditText_iconSearchPadding, 0);
//            int paddingTemp = typedArray.getDimensionPixelSize(R.styleable.SearchEditText_iconSearchPaddingLeft, 0);
//            icon_search_paddingleft = paddingTemp == 0 ? padding : paddingTemp;
//            paddingTemp = typedArray.getDimensionPixelSize(R.styleable.SearchEditText_iconSearchPaddingRight, 0);
//            icon_search_paddingright = paddingTemp == 0 ? padding : paddingTemp;
//            paddingTemp = typedArray.getDimensionPixelSize(R.styleable.SearchEditText_iconSearchPaddingTop, 0);
//            icon_search_paddingtop = paddingTemp == 0 ? padding : paddingTemp;
//            paddingTemp = typedArray.getDimensionPixelSize(R.styleable.SearchEditText_iconSearchPaddingBottom, 0);
//            icon_search_paddingbottom = paddingTemp == 0 ? padding : paddingTemp;
//
//            padding = typedArray.getDimensionPixelSize(R.styleable.SearchEditText_iconDeletePadding, 0);
//            paddingTemp = typedArray.getDimensionPixelSize(R.styleable.SearchEditText_iconDeletePaddingLeft, 0);
//            icon_delete_paddingleft = paddingTemp == 0 ? padding : paddingTemp;
//            paddingTemp = typedArray.getDimensionPixelSize(R.styleable.SearchEditText_iconDeletePaddingRight, 0);
//            icon_delete_paddingright = paddingTemp == 0 ? padding : paddingTemp;
//            paddingTemp = typedArray.getDimensionPixelSize(R.styleable.SearchEditText_iconDeletePaddingTop, 0);
//            icon_delete_paddingtop = paddingTemp == 0 ? padding : paddingTemp;
//            paddingTemp = typedArray.getDimensionPixelSize(R.styleable.SearchEditText_iconDeletePaddingBottom, 0);
//            icon_delete_paddingbottom = paddingTemp == 0 ? padding : paddingTemp;
            typedArray.recycle();
        }
//        if (drawableDel != null) {
//            drawableDel.setBounds(0 + icon_delete_paddingleft, 0 + icon_delete_paddingtop, drawableDel.getIntrinsicWidth() - icon_delete_paddingright, drawableDel.getIntrinsicHeight() - icon_delete_paddingbottom);
//        }
    }

    private void init() {
        setOnFocusChangeListener(this);
        setOnKeyListener(this);
        addTextChangedListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawables == null) drawables = getCompoundDrawables();
        if (drawableSearch == null) {
            drawableSearch = drawables[0];
//            if (drawableSearch != null) {
//                drawableSearch.setBounds(0 + icon_search_paddingleft, 0 + icon_search_paddingtop, drawableSearch.getIntrinsicWidth() - icon_search_paddingright, drawableSearch.getIntrinsicHeight() - icon_search_paddingbottom);
//            }
        }

        if (Searchgravity.values()[mSearchgravityIndex] == Searchgravity.Center) {
            if (isIconLeft) { // 如果是默认样式，直接绘制
                //绘制在左侧
                if (length() < 1) {
                    drawableTempDel = null;
                }
                this.setCompoundDrawablesWithIntrinsicBounds(drawableSearch, null, drawableTempDel, null);
                super.onDraw(canvas);
            } else { // 如果不是默认样式，需要将图标绘制在中间
                //绘制在中间
                float textWidth = getPaint().measureText(getHint().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = drawableSearch == null ? 0 : drawableSearch.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                canvas.translate((getWidth() - bodyWidth - getPaddingLeft() - getPaddingRight()) / 2, 0);
                super.onDraw(canvas);
            }
        } else if (Searchgravity.values()[mSearchgravityIndex] == Searchgravity.Left) {
            if (length() < 1) {
                drawableTempDel = null;
            }
            this.setCompoundDrawablesWithIntrinsicBounds(drawableSearch, null, drawableTempDel, null);
            super.onDraw(canvas);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // 被点击时，恢复默认样式
        if (!pressSearch && TextUtils.isEmpty(getText().toString())) {
            isIconLeft = hasFocus;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        pressSearch = (keyCode == KeyEvent.KEYCODE_ENTER);
        if (pressSearch && listener != null && event.getAction() == KeyEvent.ACTION_DOWN) {
            /*隐藏软键盘*/
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            listener.onSearchClick(v, getText().toString());
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            // 清空edit内容
            if (drawableTempDel != null) {
                eventX = (int) event.getRawX();
                eventY = (int) event.getRawY();
//            Log.i(TAG, "eventX = " + eventX + "; eventY = " + eventY);
                if (rect == null) rect = new Rect();
                getGlobalVisibleRect(rect);
                rect.left = rect.right - drawableTempDel.getIntrinsicWidth() - getPaddingRight();
                if (rect.contains(eventX, eventY)) {
                    setText("");
                    return true;
                }
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void afterTextChanged(Editable arg0) {
        if (this.length() < 1) {
            drawableTempDel = null;
        } else {
            drawableTempDel = drawableDel;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                              int arg3) {
    }

    public enum Searchgravity {
        Left,
        Center
    }

    public interface OnSearchClickListener {
        void onSearchClick(View view, String keyword);
    }
}
