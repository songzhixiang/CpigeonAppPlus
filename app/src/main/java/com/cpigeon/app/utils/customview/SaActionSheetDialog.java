package com.cpigeon.app.utils.customview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;


import com.cpigeon.app.R;

import java.util.ArrayList;
import java.util.List;

public class SaActionSheetDialog {
    private Context context;
    private Dialog dialog;
    private TextView txt_title;
    private TextView txt_cancel;
    private LinearLayout lLayout_content;
    private ScrollView sLayout_content;
    private boolean showTitle = false;
    private List<SheetItem> sheetItemList;
    private Display display;

    public SaActionSheetDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public SaActionSheetDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_actionsheet, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());

        // 获取自定义Dialog布局中的控件
        sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
        lLayout_content = (LinearLayout) view
                .findViewById(R.id.lLayout_content);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
        txt_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        sLayout_content.setOverScrollMode(View.OVER_SCROLL_NEVER);
        return this;
    }

    public SaActionSheetDialog setTitle(String title) {
        showTitle = true;
        txt_title.setVisibility(View.VISIBLE);
        txt_title.setText(title);
        return this;
    }

    public SaActionSheetDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public SaActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public Dialog getDialog() {
        return dialog;
    }

    /**
     * @param strItem  条目名称
     * @param color    条目字体颜色，设置null则默认蓝色
     * @param listener
     * @return
     */
    public SaActionSheetDialog addSheetItem(String strItem, SheetItemColor color,
                                            OnSheetItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<SheetItem>();
        }
        sheetItemList.add(new SheetItem(strItem,color, listener));
        return this;
    }

    /**
     * @param strItem  条目名称
     * @param listener
     * @return
     */
    public SaActionSheetDialog addSheetItem(String strItem,
                                            OnSheetItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<SheetItem>();
        }
        sheetItemList.add(new SheetItem(strItem, listener));
        return this;
    }

    /**
     * 设置条目布局
     */
    private void setSheetItems() {

        if (sheetItemList == null || sheetItemList.size() <= 0) {
            return;
        }
        if (lLayout_content.getChildCount() == sheetItemList.size())
            return;
        lLayout_content.removeAllViews();
        sLayout_content.setOverScrollMode(View.OVER_SCROLL_NEVER);
        int size = sheetItemList.size();

        // TODO 高度控制，非最佳解决办法
        // 添加条目过多的时候控制高度
        if (size >= 7) {
            LayoutParams params = (LayoutParams) sLayout_content
                    .getLayoutParams();
            params.height = display.getHeight() / 2;
            sLayout_content.setLayoutParams(params);
        }

//        // 循环添加条目
        for (int i = 1; i <= size; i++) {
            SheetItem sheetItem = sheetItemList.get(i - 1);
            showSheetItem(sheetItem, i);
        }
    }

    private void showSheetItem(SheetItem sheetItem, final int index) {
        if (sLayout_content == null) return;
        // TODO 高度控制，非最佳解决办法
        // 添加条目过多的时候控制高度
        if (index >= 7) {
            LayoutParams params = (LayoutParams) sLayout_content
                    .getLayoutParams();
            params.height = display.getHeight() / 2;
            sLayout_content.setLayoutParams(params);
        }

        String strItem = sheetItem.name;
        SheetItemColor color = sheetItem.color;
        final OnSheetItemClickListener listener = sheetItem.itemClickListener;

        TextView textView = new TextView(context);
        textView.setText(strItem);
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);

        // 背景图片
        textView.setBackgroundResource(R.drawable.bg_alertbuttont_item);

        // 字体颜色
        if (color == null) {
            textView.setTextColor(Color.parseColor(SheetItemColor.Blue
                    .getName()));
        } else {
            textView.setTextColor(Color.parseColor(color.getName()));
        }

        // 高度
        float scale = context.getResources().getDisplayMetrics().density;
        int height = (int) (40 * scale + 0.5f);
        textView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, height));
        // 点击事件
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(index);
                dialog.dismiss();
            }
        });

        View view = new View(context);
        view.setBackgroundResource(R.color.bgColor_divier);
        view.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, 1));
        if (showTitle || index != 1)
            lLayout_content.addView(view);
        lLayout_content.addView(textView);
    }


    public void show() {
        setSheetItems();
        dialog.show();
    }

    public enum SheetItemColor {
        Blue("#037BFF"), Red("#FD4A2E");

        private String name;

        private SheetItemColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public interface OnSheetItemClickListener {
        void onClick(int which);
    }

    public class SheetItem {
        String name;
        OnSheetItemClickListener itemClickListener;
        SheetItemColor color;

        public SheetItem(String name, SheetItemColor color,
                         OnSheetItemClickListener itemClickListener) {
            this.name = name;
            this.color = color;
            this.itemClickListener = itemClickListener;
        }

        public SheetItem(String name,
                         OnSheetItemClickListener itemClickListener) {
            this.name = name;
            this.color = SheetItemColor.Blue;
            this.itemClickListener = itemClickListener;
        }
    }
}
