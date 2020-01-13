package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.wmsj.baselibs.R
import com.wmsj.baselibs.ui.fragment.OrgMemberManageFragment
import com.wmsj.baselibs.base.BaseActivity
import kotlinx.android.synthetic.main.activity_article_sort.*
import java.util.*


/**
 * 组织成员管理
 */
class OrgMemberManageActivity : BaseActivity() {

    private var mTitle = arrayOf("待审核成员", "已审核成员")
    private var listFragment = ArrayList<Fragment>()

    override fun layoutId(): Int {
        return R.layout.activity_article_sort
    }

    override fun initData(savedInstanceState: Bundle?) {
        listFragment.add(OrgMemberManageFragment.getInstance(mTitle[0], 1))
        listFragment.add(OrgMemberManageFragment.getInstance(mTitle[1], 2))
    }

    override fun initView() {
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.manage_member))
        slidingTabLayout.setViewPager(viewPager, mTitle, this, listFragment)
    }

    override fun start() {
    }
}