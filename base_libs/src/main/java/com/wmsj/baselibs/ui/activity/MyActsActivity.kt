package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.wmsj.baselibs.R
import com.wmsj.baselibs.ui.fragment.MyActsFragment
import com.wmsj.baselibs.base.BaseActivity
import kotlinx.android.synthetic.main.activity_article_sort.*

import java.util.*

/**
 * 我的活动
on 2019/8/22.
 */
class MyActsActivity : BaseActivity() {

    private var mTitle = arrayOf("招募中", "进行中", "已完成")
    private var listFragment = ArrayList<Fragment>()

    override fun layoutId(): Int {
        return R.layout.activity_article_sort
    }

    override fun initData(savedInstanceState: Bundle?) {
        listFragment.add(MyActsFragment.getInstance(1))
        listFragment.add(MyActsFragment.getInstance(2))
        listFragment.add(MyActsFragment.getInstance(3))
    }

    override fun initView() {
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.my_activity))
        slidingTabLayout.setViewPager(viewPager, mTitle, this, listFragment)
    }

    override fun start() {
    }
}