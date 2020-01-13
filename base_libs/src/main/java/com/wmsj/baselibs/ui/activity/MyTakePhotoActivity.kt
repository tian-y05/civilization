package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.wmsj.baselibs.R
import com.wmsj.baselibs.ui.fragment.MyTakePhotoFragment
import com.wmsj.baselibs.base.BaseActivity
import kotlinx.android.synthetic.main.activity_article_sort.*
import java.util.*

/**
 * 我的随手拍
on 2019/8/22.
 */
class MyTakePhotoActivity : BaseActivity() {

    private var mTitle = arrayOf("已发布", "待审核","未通过")
    private var listFragment = ArrayList<Fragment>()

    override fun layoutId(): Int {
        return R.layout.activity_article_sort
    }

    override fun initData(savedInstanceState: Bundle?) {
        listFragment.add(MyTakePhotoFragment.getInstance(mTitle[0], 1))
        listFragment.add(MyTakePhotoFragment.getInstance(mTitle[1], 0))
        listFragment.add(MyTakePhotoFragment.getInstance(mTitle[1], -1))
    }

    override fun initView() {
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.my_take_photo))
        slidingTabLayout.setViewPager(viewPager, mTitle, this, listFragment)
    }

    override fun start() {
    }
}