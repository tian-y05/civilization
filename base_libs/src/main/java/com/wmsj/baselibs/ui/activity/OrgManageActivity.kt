package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.view.View
import com.wmsj.baselibs.R
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.utils.IntentUtils
import kotlinx.android.synthetic.main.activity_org_manage.*


/**
 * 组织挂靠
 */
class OrgManageActivity : BaseActivity() {


    override fun layoutId(): Int {
        return R.layout.activity_org_manage
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.manage_org))

        rl_top.setOnClickListener {
            IntentUtils.to(this, OrgTopActivity::class.java)
        }

        rl_down.setOnClickListener {
            IntentUtils.to(this, OrgDownActivity::class.java)
        }
    }

    override fun start() {
    }
}