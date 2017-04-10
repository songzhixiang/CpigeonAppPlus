package com.cpigeon.app.commonstandard.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 */

public class ContentFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();
    private int itemPosition = PagerAdapter.POSITION_UNCHANGED;

    public ContentFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public ContentFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public int getItemPosition(Object object) {
        return getItemPosition();
    }

    private int getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public void setData(List<Fragment> fragmentList) {
        this.fragments = fragmentList;
        if (this.fragments == null)
            this.fragments = new ArrayList<>();
        this.notifyDataSetChanged();
    }

    public void appendData(Fragment fragment) {
        if (this.fragments == null)
            this.fragments = new ArrayList<>();
        this.fragments.add(fragment);
        this.notifyDataSetChanged();
    }
}
