package com.wmsj.baselibs.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.StationContentContract
import com.wmsj.baselibs.mvp.presenter.StationContentPresenter
import com.wmsj.baselibs.ui.activity.StationContentDeatailActivity
import com.wmsj.baselibs.ui.adapter.StationContentAdapter
import com.wmsj.baselibs.base.BaseFragment
import com.wmsj.baselibs.bean.StationListBean
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.utils.IntentUtils
import com.wmsj.baselibs.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_station_list.*

/**
 * 所站简介
on 2019/8/5.
 */
class StationContentFragment : BaseFragment(), StationContentContract.View {


    private var name: String? = null
    private var activityList = ArrayList<StationListBean>()
    private var page: Int = 1
    private var pagesize: Int = 8
    private val mActivityListAdapter by lazy { activity?.let { StationContentAdapter(it, activityList, R.layout.fragment_station_content) } }
    private val mPresenter by lazy { StationContentPresenter() }

    companion object {
        fun getInstance(name: String): StationContentFragment {
            val fragment = StationContentFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.name = name
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_station_list
    }

    override fun initView() {
        mPresenter.attachView(this)
        smartRefresh.setOnRefreshListener {
            activityList?.clear()
            page = 1
            mPresenter.requestModelsData(page.toString())
            smartRefresh.setNoMoreData(false)
        }
        smartRefresh.setOnLoadMoreListener {
            page++
            mPresenter.requestModelsData(page.toString())
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mActivityListAdapter
        mActivityListAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                var stationListBean = activityList[position]
                var bundle = Bundle()
                bundle.putString("title", stationListBean.sta_name)
                bundle.putInt("id", stationListBean.id)
                bundle.putString("logo", stationListBean.sta_logo)
                activity?.let { IntentUtils.to(it, StationContentDeatailActivity::class.java,bundle) }
            }

        })
    }

    override fun lazyLoad() {
        smartRefresh.autoRefresh()
    }


    override fun showLoading() {
    }

    override fun dismissLoading() {
        smartRefresh.finishRefresh()
        smartRefresh.finishLoadMore()
    }

    override fun setModelsData(data: List<StationListBean>) {
        activityList.addAll(data)
        mActivityListAdapter?.notifyDataSetChanged()
        if (page == 1) {
            smartRefresh.finishRefresh()
        } else {
            if ((data != null && data.isEmpty())) {
                smartRefresh.finishLoadMoreWithNoMoreData()
            } else if (data != null && data.size < pagesize) {
                smartRefresh.finishLoadMore()
            }
        }

    }

    override fun showError(msg: String, errorCode: Int) {
        ToastUtils.showShort(activity,msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}