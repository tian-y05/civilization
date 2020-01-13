package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.bumptech.glide.Glide
import com.wmsj.baselibs.R
import com.wmsj.baselibs.ui.fragment.StationActivityFragment
import com.wmsj.baselibs.ui.fragment.StationSpotFragment
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.base.BaseActivity
import kotlinx.android.synthetic.main.activity_station_detail.*
import java.util.*

/**
 * 所站风貌
on 2019/8/9.
 */
class StationContentDeatailActivity : BaseActivity() {

    private var title: String? = null
    private var logo: String? = null
    private var id: Int? = null
    private var mTitle = arrayOf("实践站", "相关活动")
    private var listFragment = ArrayList<Fragment>()


    override fun layoutId(): Int {
        return R.layout.activity_station_detail
    }

    override fun initData(savedInstanceState: Bundle?) {
        title = intent.getStringExtra("title")
        logo = intent.getStringExtra("logo")
        id = intent.getIntExtra("id", -1)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, title)

        listFragment.add(StationSpotFragment.getInstance(id.toString()))
        listFragment.add(StationActivityFragment.getInstance(id.toString(),"", Const.STATION))
    }

    override fun initView() {
        Glide.with(this).load(Const.BASE_URL + logo).into(iv_image)
        tv_title.text = title
        slidingTabLayout.setViewPager(viewPager, mTitle, this, listFragment)
    }

    override fun start() {

    }
}