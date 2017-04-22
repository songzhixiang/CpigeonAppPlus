package com.cpigeon.app.modular.matchlive.model.bean.comparable;

import android.support.annotation.NonNull;

import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;

import java.util.Comparator;


/**
 * Created by chenshuai on 2017/4/20.
 */

public class MatchInfoTypeComparator implements Comparator<MatchInfo> {

    @Override
    public int compare(MatchInfo o1, MatchInfo o2) {
        if (o1.getDt().equals("bd") && o2.getDt().equals("bd"))
            return 1;
        return 0;
    }
}
