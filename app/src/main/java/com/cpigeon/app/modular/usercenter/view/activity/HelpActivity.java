package com.cpigeon.app.modular.usercenter.view.activity;

import com.cpigeon.app.commonstandard.view.activity.BaseWebViewActivity;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.NetUtils;

/**
 * Created by Administrator on 2017/4/10.
 */

public class HelpActivity extends BaseWebViewActivity {
    @Override
    protected String getLoadUrl() {
        return CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.APP_HELP_URL;
    }
    

}
