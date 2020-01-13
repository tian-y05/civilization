package com.wmsj.baselibs.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.OrgMemberContract
import com.wmsj.baselibs.mvp.presenter.OrgMemberManagePresenter
import com.wmsj.baselibs.ui.activity.OrgUserInfoActivity
import com.wmsj.baselibs.ui.adapter.OrgMemberManageAdapter
import com.wmsj.baselibs.base.BaseFragment
import com.wmsj.baselibs.bean.OrgMemberManageBean
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.utils.IntentUtils
import com.wmsj.baselibs.utils.StringUtils
import com.wmsj.baselibs.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_my_org.*

/**
 * 我的组织列表
on 2019/8/5.
 */
class OrgMemberManageFragment : BaseFragment(), OrgMemberContract.View {


    private var name: String? = null
    private var activityList = ArrayList<OrgMemberManageBean>()
    private var page: Int = 1
    private var pagesize: Int = 8
    private var state: Int = -1
    private val mActivityListAdapter by lazy { activity?.let { OrgMemberManageAdapter(it, activityList, R.layout.activity_list_item) } }
    private val mPresenter by lazy { OrgMemberManagePresenter() }
    private var curPos: Int = -1


    companion object {
        fun getInstance(name: String, state: Int): OrgMemberManageFragment {
            val fragment = OrgMemberManageFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.name = name
            fragment.state = state
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_org
    }

    override fun initView() {
        mPresenter.attachView(this)
        smartRefresh.setOnRefreshListener {
            activityList?.clear()
            page = 1
            mPresenter.orgMemberManage(state.toString(), page.toString())
            smartRefresh.setNoMoreData(false)
        }
        smartRefresh.setOnLoadMoreListener {
            page++
            mPresenter.orgMemberManage(state.toString(), page.toString())
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mActivityListAdapter
        mActivityListAdapter?.setType(state.toString())

        mActivityListAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                if(StringUtils.isEmpty(activityList[position].userid)){
                    ToastUtils.showShort(activity,"该志愿者状态错误")
                }else{
                    var bundle = Bundle()
                    bundle.putString("id", activityList[position].userid)
                    curPos = position
                    activity?.let { IntentUtils.to(it, OrgUserInfoActivity::class.java, bundle) }
                }

            }
        })

        mActivityListAdapter?.setOnItemChildListener(object : CommonRecyclerAdapter.OnItemChildListener {
            override fun onItemChidClick(v: View, position: Int) {
                curPos = position
                when (v.id) {
                    R.id.tv_cancel -> {
                        mPresenter.orgMemberCheck("3", activityList[position].userid)
                    }
                    R.id.tv_employment -> {
                        mPresenter.orgMemberCheck("1", activityList[position].userid)
                    }
                    R.id.tv_refuse -> {
                        mPresenter.orgMemberCheck("2", activityList[position].userid)
                    }
                }
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
        smartRefresh?.let {
            it.finishRefresh()
            it.finishLoadMore()
        }
        loadingDialog.dismiss()
    }

    override fun setModelsData(data: List<OrgMemberManageBean>) {
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

    override fun orgCheckResult(data: String) {
        ToastUtils.showShort(activity, data)
        activityList.removeAt(curPos)
        mActivityListAdapter?.notifyItemRemoved(curPos)
        mActivityListAdapter?.notifyItemRangeChanged(curPos, activityList.size - curPos)
    }


    override fun showError(msg: String, errorCode: Int) {
        ToastUtils.showShort(activity, msg)
    }

    override fun onReceiveEvent(event: EventBusMessage<Any>) {
        when (event.code) {
            Const.EXIT_ORG -> {
                if (event.data == state) {
                    activityList.removeAt(curPos)
                    mActivityListAdapter?.notifyItemRemoved(curPos)
                    mActivityListAdapter?.notifyItemRangeChanged(curPos, activityList.size - curPos)
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}