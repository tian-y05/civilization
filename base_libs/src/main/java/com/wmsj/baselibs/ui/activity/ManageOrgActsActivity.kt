package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.wmsj.baselibs.R
import com.wmsj.baselibs.ui.fragment.OrgMyActsFragment
import com.wmsj.baselibs.base.BaseActivity
import kotlinx.android.synthetic.main.activity_manage_acts.*
import java.util.*

/**
 * 组织 活动管理
on 2019/8/30.
 */
class ManageOrgActsActivity : BaseActivity() {

    private var mTitle = arrayOf("招募前", "招募中", "进行中", "待审核", "已完成", "草稿")
    private var listFragment = ArrayList<Fragment>()


    override fun layoutId(): Int {
        return R.layout.activity_manage_acts
    }

    override fun initData(savedInstanceState: Bundle?) {

        listFragment.add(OrgMyActsFragment.getInstance(6))
        listFragment.add(OrgMyActsFragment.getInstance(1))
        listFragment.add(OrgMyActsFragment.getInstance(2))
        listFragment.add(OrgMyActsFragment.getInstance(3))
        listFragment.add(OrgMyActsFragment.getInstance(4))
        listFragment.add(OrgMyActsFragment.getInstance(5))
    }

    override fun initView() {
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.manage_activity))
        slidingTabLayout.setViewPager(viewPager, mTitle, this, listFragment)
    }

    override fun start() {
    }
}