package com.wmsj.baselibs.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.StationActivityContract
import com.wmsj.baselibs.mvp.presenter.StationActivityPresenter
import com.wmsj.baselibs.ui.activity.EventsDetailsActivity
import com.wmsj.baselibs.ui.adapter.HomeActivityListAdapter
import com.wmsj.baselibs.base.BaseFragment
import com.wmsj.baselibs.bean.HomeActivityListBean
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.utils.IntentUtils
import kotlinx.android.synthetic.main.fragment_station_list.*
import java.util.*

/**
 *  所站风貌相关活动
on 2019/8/5.
 */
class StationActivityFragment : BaseFragment(), StationActivityContract.View {


    private val TAG: String = StationActivityFragment::class.java.simpleName
    private var newsActivityLists = ArrayList<HomeActivityListBean>()
    private val mHomeActivityListAdapter by lazy { activity?.let { HomeActivityListAdapter(it, newsActivityLists, R.layout.fragment_home_news) } }
    private var page: Int = 1
    private var pagesize: Int = 8
    private var stationId: String? = null
    private var state = ""
    private var type = ""
    private val mPresenter by lazy { StationActivityPresenter() }


    companion object {
        fun getInstance(stationId: String, state: String, type: String): StationActivityFragment {
            val fragment = StationActivityFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.stationId = stationId
            fragment.state = state
            fragment.type = type
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_station_list
    }

    override fun initView() {
        mPresenter.attachView(this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mHomeActivityListAdapter
        smartRefresh.setOnRefreshListener {
            newsActivityLists.clear()
            page = 1
            stationId?.let { mPresenter.requestModelsData(it, state, page.toString(),type) }
            smartRefresh.setNoMoreData(false)
        }
        smartRefresh.setOnLoadMoreListener {
            page++
            stationId?.let { mPresenter.requestModelsData(it, state, page.toString(),type) }
        }
        mHomeActivityListAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                var bundle = Bundle()
                bundle.putString("id", newsActivityLists[position].id)
                activity?.let { IntentUtils.to(it, EventsDetailsActivity::class.java, bundle) }
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

    override fun setModelsData(data: List<HomeActivityListBean>) {
        newsActivityLists.addAll(data)
        mHomeActivityListAdapter?.notifyDataSetChanged()
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

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}