package com.cpigeon.app.modular.matchlive.view.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.matchlive.model.bean.SearchHistory;
import com.cpigeon.app.utils.CpigeonConfig;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.customview.CpigeonListView;
import com.cpigeon.app.utils.customview.SearchEditText;
import com.cpigeon.app.utils.customview.SearchTitleBar;

import org.xutils.DbManager;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/14.
 */

public class SearchActivity extends BaseActivity implements ISearchView {
    public static final String INTENT_KEY_SEARCHKEY = "search_key";//传递过来的搜索关键字
    public static final String INTENT_KEY_SEARCH_HINT_TEXT = "search_hint_text";//输入框提示
    @BindView(R.id.search_bar)
    SearchTitleBar searchBar;
    @BindView(R.id.list_history)
    CpigeonListView listHistory;
    @BindView(R.id.tv_history_clear)
    TextView tvHistoryClear;
    @BindView(R.id.layout_search_history)
    ScrollView layoutSearchHistory;
    @BindView(R.id.tv_search_result)
    TextView tvSearchResult;
    @BindView(R.id.recyclerview_searchReault)
    RecyclerView recyclerviewSearchReault;
    @BindView(R.id.layout_search_result)
    LinearLayout layoutSearchResult;
    private DbManager mDB;
    private int userId = 0;
    private String sKey = "";
    private Map<String, Object> searchResultTempData = new HashMap<>();//用户零时存储搜索的结果
    private SearchEditText search_edittext;
    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mDB = x.getDb(CpigeonConfig.getDataDb());
        search_edittext = searchBar.getTitleBarSearchEditText();
        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String lastKey = sKey;
                sKey = s.toString().trim();
                if (!lastKey.equals(sKey)) {
//                    loadSearchHistory();
                }
            }
        });
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }


    @Override
    public void showLoadSearchResult(List data) {

    }

    @Override
    public void showLoadSearchHistory(List<SearchHistory> data) {

    }

    @Override
    public void saveSearchHistory() {

    }

    @Override
    public String getSearch() {
        return null;
    }
}
