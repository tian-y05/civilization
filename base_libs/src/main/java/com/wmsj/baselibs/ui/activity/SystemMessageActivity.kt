package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.SystemMessageContract
import com.wmsj.baselibs.mvp.presenter.SystemMessagePresenter
import com.wmsj.baselibs.ui.adapter.SystemMessageListAdapter
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.SystemNewsBean
import com.wmsj.baselibs.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_article_list.*
import java.util.*

/**
 * Created by tian
on 2019/8/26.
 */
class SystemMessageActivity : BaseActivity(), SystemMessageContract.View {

    private val mPresenter by lazy { SystemMessagePresenter() }
    private var recordList = ArrayList<SystemNewsBean>()
    private val mListAdapter by lazy { SystemMessageListAdapter(this, recordList, R.layout.system_message_item) }
    private var page: Int = 1
    private var pagesize: Int = 8

    override fun layoutId(): Int {
        return R.layout.activity_article_list
    }

    override fun initData(savedInstanceState: Bundle?) {
        mPresenter.attachView(this)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.system_message))
    }

    override fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mListAdapter
        smartRefresh.setOnRefreshListener {
            recordList.clear()
            page = 1
            mPresenter.requestModelsData(page.toString())
            smartRefresh.setNoMoreData(false)
        }
        smartRefresh.setOnLoadMoreListener {
            page++
            mPresenter.requestModelsData(page.toString())
        }
    }

    override fun start() {
        smartRefresh.autoRefresh()
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    override fun setModelsData(data: List<SystemNewsBean>?) {
        data?.let { recordList.addAll(it) }
        mListAdapter.notifyDataSetChanged()
        if (page == 1) {
            smartRefresh.finishRefresh()
        } else {
            if ((data != null && data.isEmpty())) {
                smartRefresh.finishLoadMoreWithNoMoreData()
            } else if (data != null && data.size < pagesize) {
                smartRefresh.finishLoadMore()
            } else {
                smartRefresh.finishLoadMoreWithNoMoreData()
            }
        }
    }

    override fun showError(msg: String, errorCode: Int) {
        ToastUtils.showShort(this, msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}