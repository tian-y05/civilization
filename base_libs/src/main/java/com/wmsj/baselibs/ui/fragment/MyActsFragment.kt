package com.wmsj.baselibs.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.MyActsContract
import com.wmsj.baselibs.mvp.presenter.MyActsPresenter
import com.wmsj.baselibs.ui.activity.CheckCardActivity
import com.wmsj.baselibs.ui.activity.EventsDetailsActivity
import com.wmsj.baselibs.ui.activity.MyActsDetailsActivity
import com.wmsj.baselibs.ui.adapter.MyActivityListAdapter
import com.wmsj.baselibs.base.BaseFragment
import com.wmsj.baselibs.bean.ActivityService
import com.wmsj.baselibs.bean.ClockCardBean
import com.wmsj.baselibs.bean.MyActivityBean
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.utils.IntentUtils
import com.wmsj.baselibs.utils.SPUtil
import com.wmsj.baselibs.utils.ToastUtils
import com.wmsj.baselibs.view.SetSuccessDialog
import kotlinx.android.synthetic.main.fragment_events_management.*
import java.util.*
import kotlin.collections.HashMap

/**
 * 活动管理fragment "招募中", "运行中", "待审核", "已完成", "草稿"
on 2019/8/5.
 */
class MyActsFragment : BaseFragment(), MyActsContract.View {

    private val TAG: String = MyActsFragment::class.java.simpleName
    private var newsActivityLists = ArrayList<MyActivityBean>()
    private val mHomeActivityListAdapter by lazy { activity?.let { MyActivityListAdapter(it, newsActivityLists, R.layout.fragment_my_acts) } }
    private var name: String? = null
    private var page: Int = 1
    private var pagesize: Int = 8
    private var state: Int = -1
    private var curPos: Int = -1
    private val mPresenter by lazy { MyActsPresenter() }


    companion object {
        fun getInstance(state: Int): MyActsFragment {
            val fragment = MyActsFragment()
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
        rl_select.visibility = View.GONE
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mHomeActivityListAdapter
        mHomeActivityListAdapter?.setActivityType(state)
        smartRefresh.setOnRefreshListener {
            newsActivityLists.clear()
            page = 1
            mPresenter.requestModelsData(state.toString(), page.toString())
            smartRefresh.setNoMoreData(false)
        }
        smartRefresh.setOnLoadMoreListener {
            page++
            mPresenter.requestModelsData(state.toString(), page.toString())
        }
        mHomeActivityListAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                var bundle = Bundle()
                bundle.putString("id", newsActivityLists[position].aid)
                activity?.let { IntentUtils.to(it, EventsDetailsActivity::class.java, bundle) }
            }

        })

        mHomeActivityListAdapter?.setOnItemOperateListener(object : MyActivityListAdapter.onItemOperateListener {
            override fun leftClick(position: Int) {
                curPos = position
                when (state) {
                    1 -> {
                        activity?.let {
                            SetSuccessDialog.Builder(it)
                                    .setTitle(getString(R.string.tips))
                                    .setMessage(getString(R.string.sure_to_cancel))
                                    .setPositiveButton(getString(R.string.cancel), DialogInterface.OnClickListener { p0, p1 ->
                                        p0.dismiss()
                                    })
                                    .setNegativeButton(getString(R.string.sure), DialogInterface.OnClickListener { p0, p1 ->
                                        p0.dismiss()
                                        var map = HashMap<String, String>()
                                        map.put("userid", SPUtil.get(activity, "userId", "").toString())
                                        map.put("aid", newsActivityLists[position].aid)
                                        mPresenter.removeSign(map)
                                    })
                                    .setWith(0.77f)
                                    .create()
                                    .show()
                        }
                    }
                    2, 3 -> {
                        var bundle = Bundle()
                        bundle.putString("aid", newsActivityLists[position].aid)
                        activity?.let { IntentUtils.to(it, MyActsDetailsActivity::class.java, bundle) }
                    }

                }
            }

            override fun rightClick(position: Int) {
                var bundle = Bundle()
                bundle.putString("aid", newsActivityLists[position].aid)
                activity?.let { IntentUtils.to(it, CheckCardActivity::class.java, bundle) }

            }

        })
    }

    override fun lazyLoad() {
        smartRefresh.autoRefresh()
    }


    override fun showLoading() {
        loadingDialog.show()
    }

    override fun dismissLoading() {
        smartRefresh.finishRefresh()
        smartRefresh.finishLoadMore()
        loadingDialog.dismiss()
    }

    override fun setModelsData(data: List<MyActivityBean>) {
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

    override fun activityServiceDetail(data: ActivityService) {
    }

    override fun checkCardResult(date: ClockCardBean) {
    }

    override fun removeSignResult(date: String) {
        ToastUtils.showShort(activity, date)
        newsActivityLists.removeAt(curPos)
        mHomeActivityListAdapter?.notifyItemRemoved(curPos)
        mHomeActivityListAdapter?.notifyItemRangeChanged(curPos, newsActivityLists.size - curPos)
    }

}