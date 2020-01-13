package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.ServiceRecordContract
import com.wmsj.baselibs.mvp.presenter.ServiceRecordPresenter
import com.wmsj.baselibs.ui.adapter.ServiceRecordListAdapter
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.Record
import com.wmsj.baselibs.bean.ServiceRecordBean
import com.wmsj.baselibs.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_service_record.*
import java.util.*

/**
 * Created by tian
on 2019/8/26.
 */
class ServiceRecordActivity : BaseActivity(), ServiceRecordContract.View {

    private val mPresenter by lazy { ServiceRecordPresenter() }
    private var recordList = ArrayList<Record>()
    private val mListAdapter by lazy { ServiceRecordListAdapter(this, recordList, R.layout.service_record_item) }
    private var page: Int = 1
    private var pagesize: Int = 8

    override fun layoutId(): Int {
        return R.layout.activity_service_record
    }

    override fun initData(savedInstanceState: Bundle?) {
        mPresenter.attachView(this)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.service_records))
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

    override fun setModelsData(data: ServiceRecordBean) {
        tv_long.text = "服务时长：" + data.totalservertime
        tv_num.text = "活动数量：" + data.total
        data?.list?.let { recordList.addAll(it) }
        mListAdapter.notifyDataSetChanged()
        if (page == 1) {
            smartRefresh.finishRefresh()
        } else {
            if (data?.list != null && data.list.isEmpty()) {
                smartRefresh.finishLoadMoreWithNoMoreData()
            } else if (data?.list != null && data.list.size < pagesize) {
                smartRefresh.finishLoadMore()
            } else {
                smartRefresh.finishLoadMoreWithNoMoreData()
            }
        }
    }

    override fun showError(msg: String, errorCode: Int) {
        ToastUtils.showShort(this,msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}