package com.cpigeon.app.utils.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2017/4/14.
 */

public class CpigeonListView extends ListView {

    private int maxHeight = -1;
    private boolean inScrollViewMode = true;

    public CpigeonListView(Context context) {
        super(context);
    }

    public CpigeonListView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public CpigeonListView(Context context, AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);

    }

    /**
     * 设置最大高度
     *
     * @param maxHeight
     */
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public boolean isInScrollViewMode() {
        return inScrollViewMode;
    }

    public void setInScrollViewMode(boolean inScrollViewMode) {
        this.inScrollViewMode = inScrollViewMode;
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = heightMeasureSpec;
        if (maxHeight > -1) {
            //最大高度
            height = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        } else if (inScrollViewMode) {
            height = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, height);
    }
}
