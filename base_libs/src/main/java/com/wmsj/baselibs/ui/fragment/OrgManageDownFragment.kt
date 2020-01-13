package com.wmsj.baselibs.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.OrgManageContract
import com.wmsj.baselibs.mvp.presenter.OrgManagePresenter
import com.wmsj.baselibs.ui.activity.OrgManageDetailsActivity
import com.wmsj.baselibs.ui.adapter.OrgManageListAdapter
import com.wmsj.baselibs.base.BaseFragment
import com.wmsj.baselibs.bean.HangupOrgBean
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.utils.IntentUtils
import com.wmsj.baselibs.utils.L
import com.wmsj.baselibs.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_org_down.*

/**
 * 下级组织列表
on 2019/8/5.
 */
class OrgManageDownFragment : BaseFragment(), OrgManageContract.View {

    private var name: String? = null
    private var state: Int = -1
    private var orgList = ArrayList<HangupOrgBean>()
    private val mOrgListAdapter by lazy { activity?.let { OrgManageListAdapter(it, orgList, R.layout.activity_list_item) } }
    private val mPresenter by lazy { OrgManagePresenter() }
    private var curPos: Int = -1


    companion object {
        fun getInstance(name: String, state: Int): OrgManageDownFragment {
            val fragment = OrgManageDownFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.name = name
            fragment.state = state
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_org_down
    }

    override fun initView() {
        mPresenter.attachView(this)
        smartRefresh.setOnRefreshListener {
            orgList?.clear()
            mPresenter.requestModelsData("-1")
            smartRefresh.setNoMoreData(false)
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mOrgListAdapter

        mOrgListAdapter?.setType(state)

        mOrgListAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                curPos = position
                var bundle = Bundle()
                bundle.putString("id", orgList[position].id)
                bundle.putString("state", orgList[position].state)
                bundle.putString("type", state.toString())
                activity?.let { IntentUtils.to(it, OrgManageDetailsActivity::class.java, bundle) }
            }
        })

        mOrgListAdapter?.setOnItemChildListener(object : CommonRecyclerAdapter.OnItemChildListener {
            override fun onItemChidClick(v: View, position: Int) {
                curPos = position
                L.d("mOrgListAdapter" + orgList[position].state + ";" + orgList[position].id)
                when (v.id) {
                    R.id.tv_right -> {
                        mPresenter.hangupOrgOperation(orgList[position].state, orgList[position].id)
                    }
                    R.id.tv_left -> {
                        mPresenter.hangupOrgOperation("6", orgList[position].id)
                    }
                }
            }
        })
    }

    override fun lazyLoad() {
        smartRefresh.autoRefresh()
    }


    override fun showLoading() {
    }

    override fun dismissLoading() {
        smartRefresh?.let {
            it.finishRefresh()
            it.finishLoadMore()
        }
    }

    override fun setModelsData(data: List<HangupOrgBean>) {
        when (state) {
            2 -> {//已挂靠
                data.forEach {
                    if (it.state == "4") {//移除挂靠
                        orgList.add(it)
                    }
                }
            }
            3 -> { //待审核
                data.forEach {
                    if (it.state == "5" || it.state == "6") { //通过、拒绝
                        orgList.add(it)
                    }
                }
            }
        }

        mOrgListAdapter?.notifyDataSetChanged()
        smartRefresh.finishRefresh()

    }

    override fun operationResult(data: String) {
        ToastUtils.showShort(activity, data)
        orgList.removeAt(curPos)
        mOrgListAdapter?.notifyItemRemoved(curPos)
        mOrgListAdapter?.notifyItemRangeChanged(curPos, orgList.size - curPos)
    }

    override fun showError(msg: String, errorCode: Int) {
        ToastUtils.showShort(activity, msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun onReceiveEvent(event: EventBusMessage<Any>) {
        when (event.code) {
            Const.ORG_MANAGE -> {
                if (event.data == state.toString()) {
                    orgList.removeAt(curPos)
                    mOrgListAdapter?.notifyItemRemoved(curPos)
                    mOrgListAdapter?.notifyItemRangeChanged(curPos, orgList.size - curPos)
                }
            }
        }
    }

}