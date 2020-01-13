package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.wmsj.baselibs.R
import com.wmsj.baselibs.ui.fragment.EventsManagementFragment
import com.wmsj.baselibs.base.BaseActivity
import kotlinx.android.synthetic.main.activity_events_management.*
import java.util.*

/**
 * 活动管理
on 2019/8/5.
 */
class EventsManagementActivity : BaseActivity() {

    private var mTitle = arrayOf("招募前", "招募中", "进行中",  "已完成")
    private var listFragment = ArrayList<Fragment>()

    override fun layoutId(): Int {
        return R.layout.activity_events_management
    }

    override fun initData(savedInstanceState: Bundle?) {

        listFragment.add(EventsManagementFragment.getInstance(4))
        listFragment.add(EventsManagementFragment.getInstance(1))
        listFragment.add(EventsManagementFragment.getInstance(2))
        listFragment.add(EventsManagementFragment.getInstance(3))

    }

    override fun initView() {
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, "活动")
        slidingTabLayout.setViewPager(viewPager, mTitle, this, listFragment)
    }

    override fun start() {

    }
}