package com.wmsj.baselibs.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.StationSpotContract
import com.wmsj.baselibs.mvp.presenter.StationSpotPresenter
import com.wmsj.baselibs.ui.activity.StationSpotActivity
import com.wmsj.baselibs.ui.activity.WebViewDetailActivity
import com.wmsj.baselibs.ui.adapter.StationSpotAdapter
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.base.BaseFragment
import com.wmsj.baselibs.bean.StationListBean
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.utils.IntentUtils
import com.wmsj.baselibs.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_station_list.*
import java.util.*

/**
 *  所站风貌实践站
on 2019/8/5.
 */
class StationSpotFragment : BaseFragment(), StationSpotContract.View {


    private val TAG: String = StationSpotFragment::class.java.simpleName
    private var newsActivityLists = ArrayList<StationListBean>()
    private val mStationSpotAdapter by lazy { activity?.let { StationSpotAdapter(it, newsActivityLists, R.layout.fragment_station_spot) } }
    private var page: Int = 1
    private var pagesize: Int = 8
    private var stationId: String? = null
    private val mPresenter by lazy { StationSpotPresenter() }


    companion object {
        fun getInstance(stationId: String): StationSpotFragment {
            val fragment = StationSpotFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.stationId = stationId
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_station_list
    }

    override fun initView() {
        mPresenter.attachView(this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mStationSpotAdapter
        smartRefresh.setOnRefreshListener {
            newsActivityLists.clear()
            page = 1
            stationId?.let { mPresenter.requestModelsData(it, page.toString()) }
            smartRefresh.setNoMoreData(false)
        }
        smartRefresh.setOnLoadMoreListener {
            page++
            stationId?.let { mPresenter.requestModelsData(it, page.toString()) }
        }

        mStationSpotAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                var bundle = Bundle()
                bundle.putInt("id", newsActivityLists[position].id)
                bundle.putString("type", Const.STATION)
                activity?.let { IntentUtils.to(it, WebViewDetailActivity::class.java,bundle) }
            }

        })

        mStationSpotAdapter?.setOnItemLookClickListener(object : StationSpotAdapter.onItemLookClickListener {
            override fun onIntroductionClick(position: Int) {
                var bundle = Bundle()
                bundle.putInt("id", newsActivityLists[position].id)
                bundle.putString("type", Const.STATION)
                activity?.let { IntentUtils.to(it, WebViewDetailActivity::class.java,bundle) }
            }

            override fun onActivityClick(position: Int) {
                var bundle = Bundle()
                bundle.putString("stationId", newsActivityLists[position].id.toString())
                bundle.putString("type", Const.STATION)
                activity?.let { IntentUtils.to(it, StationSpotActivity::class.java,bundle) }

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
        newsActivityLists.addAll(data)
        mStationSpotAdapter?.notifyDataSetChanged()
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