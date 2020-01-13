package com.wmsj.baselibs.ui.activity

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.utils.WebViewInitUtils
import kotlinx.android.synthetic.main.activity_webview_detail.*


/**
 * Created by tian
on 2019/8/14.
 */
class WebViewDetailActivity : BaseActivity() {
    private var type: String? = null
    private var url: String? = null
    private var oid: String? = null
    private var id = 0
    override fun layoutId(): Int {
        return R.layout.activity_webview_detail
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initData(savedInstanceState: Bundle?) {
        type = intent.getStringExtra("type")
        url = intent.getStringExtra("url")
        oid = intent.getStringExtra("oid")
        id = intent.getIntExtra("id", -1)

        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, if (type == Const.PATTERN) intent.getStringExtra("name") else getString(R.string.detail))

        WebViewInitUtils.init(this, webview)
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
    }

    override fun initView() {

        when (type) {
            Const.PATTERN -> webview.loadUrl(Const.WEBVIEW_URL + Const.PATTERN_URL)
            Const.VIDEO -> webview.loadUrl(Const.WEBVIEW_URL + Const.VIDEO_URL + id)
            Const.ARTICLE -> webview.loadUrl(Const.WEBVIEW_URL + Const.ARTICLE_URL + id)
            Const.PRACTICE, Const.STATION -> webview.loadUrl(Const.WEBVIEW_URL + Const.ARTICLE_URL + id + "&type=" + type)
            Const.BANNER -> webview.loadUrl(url)
            Const.ACT_REPORT -> webview.loadUrl(Const.WEBVIEW_URL + Const.ACT_REPORT + oid)
        }
    }

    override fun start() {
    }

}