package com.wmsj.baselibs.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.EventsManagementContract
import com.wmsj.baselibs.mvp.presenter.EventsManagementPresenter
import com.wmsj.baselibs.ui.activity.EventsDetailsActivity
import com.wmsj.baselibs.ui.adapter.HomeActivityListAdapter
import com.wmsj.baselibs.base.BaseFragment
import com.wmsj.baselibs.bean.EventsBean
import com.wmsj.baselibs.bean.HomeActivityListBean
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.utils.IntentUtils
import com.wmsj.baselibs.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_events_management.*
import java.util.*

/**
 * 活动管理fragment "招募中", "运行中", "待审核", "已完成", "草稿"
on 2019/8/5.
 */
class EventsManagementFragment : BaseFragment(), EventsManagementContract.View {


    private val TAG: String = EventsManagementFragment::class.java.simpleName
    private var newsActivityLists = ArrayList<HomeActivityListBean>()
    private val mHomeActivityListAdapter by lazy { activity?.let { HomeActivityListAdapter(it, newsActivityLists, R.layout.fragment_home_news) } }
    private var name: String? = null
    private var page: Int = 1
    private var pagesize: Int = 8
    private var state: Int? = null
    private val mPresenter by lazy { EventsManagementPresenter() }


    companion object {
        fun getInstance(state: Int): EventsManagementFragment {
            val fragment = EventsManagementFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.state = state
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_events_management
    }

    override fun initView() {
        mPresenter.attachView(this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mHomeActivityListAdapter
        smartRefresh.setOnRefreshListener {
            newsActivityLists.clear()
            page = 1
            mPresenter.requestModelsData(state.toString(), page.toString(), "")
            smartRefresh.setNoMoreData(false)
        }
        smartRefresh.setOnLoadMoreListener {
            page++
            mPresenter.requestModelsData(state.toString(), page.toString(), "")
        }
        mHomeActivityListAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                var bundle = Bundle()
                bundle.putString("id", newsActivityLists[position].aid)
                activity?.let { IntentUtils.to(it, EventsDetailsActivity::class.java, bundle) }
            }

        })

        //点击键盘的搜索按钮
        et_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                newsActivityLists.clear()
                page = 1
                mPresenter.requestModelsData(state.toString(), page.toString(), et_search.text.toString())
                // 当按了搜索之后关闭软键盘
                (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                        activity?.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
            false
        }
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
        Log.d("setModelsData:", "1:" + (data != null) + ";" + data.isEmpty() + ";" + data.size)
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
        ToastUtils.showShort(activity, msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun setOrgActs(data: List<EventsBean>) {
    }

    override fun backDraftResult(msg: String) {
    }

}