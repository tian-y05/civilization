package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.wmsj.baselibs.R
import com.wmsj.baselibs.ui.fragment.MyOrgFragment
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.utils.IntentUtils
import kotlinx.android.synthetic.main.activity_article_sort.*
import java.util.*

/**
 * 我的组织
on 2019/8/22.
 */
class MyOrgActivity : BaseActivity() {

    private var mTitle = arrayOf("已加入组织", "待审核组织")
    private var listFragment = ArrayList<Fragment>()

    override fun layoutId(): Int {
        return R.layout.activity_article_sort
    }

    override fun initData(savedInstanceState: Bundle?) {
        listFragment.add(MyOrgFragment.getInstance(mTitle[0], 1))
        listFragment.add(MyOrgFragment.getInstance(mTitle[1], 0))
    }

    override fun initView() {
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.my_org))
        slidingTabLayout.setViewPager(viewPager, mTitle, this, listFragment)
        fab_button.visibility = View.VISIBLE
        fab_button.setOnClickListener {
            IntentUtils.to(this, OrgSearchActivity::class.java)
        }
    }

    override fun start() {
    }

}