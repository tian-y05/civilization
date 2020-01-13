package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.wmsj.baselibs.R
import com.wmsj.baselibs.ui.fragment.StationActivityFragment
import com.wmsj.baselibs.base.BaseActivity
import kotlinx.android.synthetic.main.activity_events_management.*
import java.util.*

/**
 * 所站风貌实践站查看活动详情
on 2019/8/5.
 */
class StationSpotActivity : BaseActivity() {

    private var mTitle = arrayOf("全部", "招募中", "进行中", "已完成")
    private var listFragment = ArrayList<Fragment>()

    override fun layoutId(): Int {
        return R.layout.activity_events_management
    }

    override fun initData(savedInstanceState: Bundle?) {

        var stationId = intent.getStringExtra("stationId")
        var type = intent.getStringExtra("type")
        listFragment.add(StationActivityFragment.getInstance(stationId, "0",type))
        listFragment.add(StationActivityFragment.getInstance(stationId, "1",type))
        listFragment.add(StationActivityFragment.getInstance(stationId, "2",type))
        listFragment.add(StationActivityFragment.getInstance(stationId, "3",type))

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