package com.wmsj.baselibs.update

import android.os.Bundle
import com.wmsj.baselibs.R
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.utils.PackageManage
import kotlinx.android.synthetic.main.update_activity.*

/**
 * Created by tian
on 2019/9/20.
 */
class UpdateActivity : BaseActivity() {

    private var url = ""
    private var content = ""


    override fun layoutId(): Int {
        return R.layout.update_activity
    }

    override fun initData(savedInstanceState: Bundle?) {
        url = intent.getStringExtra("url")
        content = intent.getStringExtra("content")
    }

    override fun initView() {
        txt_content.text = content
    }

    override fun start() {

        txt_sure.setOnClickListener {
            DownLoadBuilder.Builder(this)
                    .addUrl(url)
                    .isWiFi(true)
                    .addDownLoadName(PackageManage.getAppProcessName(this))
                    .addDscription("开始下载")
                    .builder()
            finish()
        }
        txt_cancel.setOnClickListener {
            finish()
        }

    }

}