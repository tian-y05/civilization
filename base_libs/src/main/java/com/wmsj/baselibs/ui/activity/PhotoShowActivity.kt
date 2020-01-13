package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.view.View
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.R
import com.wmsj.baselibs.ui.adapter.PhotoShowAdapter
import com.wmsj.baselibs.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_photo_show.*


/**
 * 文明随手拍查看图片
on 2019/8/12.
 */
class PhotoShowActivity : BaseActivity() {

    private var photoLists: Array<String>? = null

    override fun layoutId(): Int {
        return R.layout.activity_photo_show
    }

    override fun initData(savedInstanceState: Bundle?) {
        StatusBarUtil.setColor(this, resources.getColor(R.color.color_black))
        photoLists = intent.getStringArrayExtra("photos")
    }

    override fun initView() {
        view_pager.adapter = photoLists?.let { PhotoShowAdapter(this, it) }
        iv_finish.setOnClickListener {
            finish()
        }
        photoLists?.size?.let {
            if(it > 1){
                pageIndicator.visibility = View.VISIBLE
            }
        }
        pageIndicator.setViewPager(view_pager)
    }

    override fun start() {
    }


}