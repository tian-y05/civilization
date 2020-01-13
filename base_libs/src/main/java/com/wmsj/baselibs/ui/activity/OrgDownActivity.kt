package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.wmsj.baselibs.R
import com.wmsj.baselibs.ui.fragment.OrgManageDownFragment
import com.wmsj.baselibs.base.BaseActivity
import kotlinx.android.synthetic.main.activity_org_down.*
import java.util.*


/**
 * 下级组织
 */
class OrgDownActivity : BaseActivity() {

    private var mTitle = arrayOf("已挂靠组织", "待审核组织")
    private var listFragment = ArrayList<Fragment>()

    override fun layoutId(): Int {
        return R.layout.activity_org_down
    }

    override fun initData(savedInstanceState: Bundle?) {
        listFragment.add(OrgManageDownFragment.getInstance(mTitle[0], 2))
        listFragment.add(OrgManageDownFragment.getInstance(mTitle[1], 3))
    }

    override fun initView() {
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.org_down))
        slidingTabLayout.setViewPager(viewPager, mTitle, this, listFragment)
    }

    override fun start() {
    }
}