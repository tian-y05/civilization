package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.PlatformShowContract
import com.wmsj.baselibs.mvp.presenter.PlatformShowPresenter
import com.wmsj.baselibs.ui.adapter.PlatformShowAdapter
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.HomeColumnsBean
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.utils.IntentUtils
import com.wmsj.baselibs.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_platform_show.*
import java.util.*

/**
 * 平台展示
on 2019/8/7.
 */
class PlatformShowActivity : BaseActivity(), PlatformShowContract.View {

    private val TAG = PlatformShowActivity::class.java.simpleName

    private var title: String? = null
    private val mPresenter by lazy { PlatformShowPresenter() }
    private var columnsLists = ArrayList<HomeColumnsBean>()
    private val mPlatformShowAdapter by lazy { this?.let { PlatformShowAdapter(it, columnsLists, R.layout.platform_show_item) } }


    override fun layoutId(): Int {
        return R.layout.activity_platform_show
    }

    override fun initData(savedInstanceState: Bundle?) {
        title = intent.getStringExtra("name")
    }

    override fun initView() {
        mPresenter.attachView(this)

        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, title)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mPlatformShowAdapter

        mPlatformShowAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                var bundle = Bundle()
                bundle.putInt("key", columnsLists[position].id)
                bundle.putString("name", if (TextUtils.isEmpty(columnsLists[position].name)) columnsLists[position].app_name else columnsLists[position].name)
                IntentUtils.to(this@PlatformShowActivity, ArticleListActivity::class.java, bundle)
            }

        })
    }

    override fun start() {
        mPresenter.requestModelsData(1)
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun dismissLoading() {
        loadingDialog.dismiss()
    }

    override fun setModelsData(data: List<HomeColumnsBean>) {
        columnsLists.addAll(data)
        mPlatformShowAdapter.notifyDataSetChanged()
    }

    override fun showError(msg: String, errorCode: Int) {
        ToastUtils.showShort(this, msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}