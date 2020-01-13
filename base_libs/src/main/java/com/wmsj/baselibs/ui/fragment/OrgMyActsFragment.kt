package com.wmsj.baselibs.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.EventsManagementContract
import com.wmsj.baselibs.mvp.presenter.EventsManagementPresenter
import com.wmsj.baselibs.ui.adapter.OrgActsListAdapter
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.base.BaseFragment
import com.wmsj.baselibs.bean.EventsBean
import com.wmsj.baselibs.bean.HomeActivityListBean
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.ui.activity.*
import com.wmsj.baselibs.utils.IntentUtils
import com.wmsj.baselibs.utils.StringUtils
import com.wmsj.baselibs.utils.ToastUtils
import com.wmsj.baselibs.view.SetSuccessDialog
import kotlinx.android.synthetic.main.fragment_events_management.*
import java.util.*

/**
 * 活动管理fragment "招募中", "运行中", "待审核", "已完成", "草稿"
on 2019/8/5.
 */
class OrgMyActsFragment : BaseFragment(), EventsManagementContract.View {


    private val TAG: String = OrgMyActsFragment::class.java.simpleName
    private var actsLists = ArrayList<EventsBean>()
    private val orgListAdapter by lazy { activity?.let { OrgActsListAdapter(it, actsLists, R.layout.fragment_org_acts) } }
    private var name: String? = null
    private var page: Int = 1
    private var pagesize: Int = 8
    private var curPosition: Int = -1
    private var state: Int? = null
    private val mPresenter by lazy { EventsManagementPresenter() }


    companion object {
        fun getInstance(state: Int): OrgMyActsFragment {
            val fragment = OrgMyActsFragment()
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
        recyclerView.adapter = orgListAdapter
        state?.let { orgListAdapter?.setActsType(it) }
        smartRefresh.setOnRefreshListener {
            actsLists.clear()
            page = 1
            mPresenter.requestOrgActs(state.toString(), page.toString())
            smartRefresh.setNoMoreData(false)
        }
        smartRefresh.setOnLoadMoreListener {
            page++
            mPresenter.requestOrgActs(state.toString(), page.toString())
        }
        orgListAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                var bundle = Bundle()
                bundle.putString("id", actsLists[position].id)
                activity?.let { IntentUtils.to(it, EventsDetailsActivity::class.java, bundle) }
            }

        })
        orgListAdapter?.setOnItemChildListener(object : CommonRecyclerAdapter.OnItemChildListener {
            override fun onItemChidClick(v: View, position: Int) {
                curPosition = position
                when (v.id) {
                    R.id.tv_back -> {//撤回草稿
                        activity?.let {
                            SetSuccessDialog.Builder(it)
                                    .setTitle(getString(R.string.tips))
                                    .setMessage("确定撤回草稿？")
                                    .setPositiveButton(getString(R.string.cancel), DialogInterface.OnClickListener { p0, p1 ->
                                        p0.dismiss()
                                    })
                                    .setNegativeButton(getString(R.string.sure), DialogInterface.OnClickListener { p0, p1 ->
                                        p0.dismiss()
                                        var map = HashMap<String, String>()
                                        map.put("id", actsLists[position].id)
                                        map.put("statu", "-1")
                                        mPresenter.backDraft(map)
                                    })
                                    .setWith(0.77f)
                                    .create()
                                    .show()
                        }


                    }
                    R.id.ll_recruit_num -> {//招募人数
                        var bundle = Bundle()
                        bundle.putString("id", actsLists[position].id)
                        bundle.putString("type", "1")
                        activity?.let { IntentUtils.to(it, RecruitMemberActivity::class.java, bundle) }
                    }
                    R.id.ll_pending_trial -> {//待审人数
                        var bundle = Bundle()
                        bundle.putString("id", actsLists[position].id)
                        bundle.putString("type", "2")
                        activity?.let { IntentUtils.to(it, RecruitMemberActivity::class.java, bundle) }
                    }
                    R.id.ll_employ_member -> {//录用本组成员
                        var bundle = Bundle()
                        bundle.putString("id", actsLists[position].id)
                        activity?.let { IntentUtils.to(it, EmployMemberActivity::class.java, bundle) }
                    }
                    R.id.ll_join_num -> {//参与人数
                        var bundle = Bundle()
                        bundle.putString("id", actsLists[position].id)
                        bundle.putString("type", "3")
                        activity?.let { IntentUtils.to(it, RecruitMemberActivity::class.java, bundle) }
                    }
                    R.id.ll_acts_photo -> {//活动图片
                        var bundle = Bundle()
                        bundle.putString("id", actsLists[position].id)
                        bundle.putString("pic", actsLists[position].picnum)
                        activity?.let { IntentUtils.to(it, OrgActsPicActivity::class.java, bundle) }
                    }
                    R.id.ll_supplement_time -> {//补录时长
                        var bundle = Bundle()
                        bundle.putString("id", actsLists[position].id)
                        activity?.let { IntentUtils.to(it, SupplementTimeActivity::class.java, bundle) }
                    }
                    R.id.ll_acts_report -> {//活动报道
                        if (actsLists[position].report_num > 0) {
                            var bundle = Bundle()
                            bundle.putString("oid", actsLists[position].id)
                            bundle.putString("type", Const.ACT_REPORT)
                            activity?.let { IntentUtils.to(it, WebViewDetailActivity::class.java, bundle) }
                        } else {
                            var bundle = Bundle()
                            bundle.putString("id", actsLists[position].id)
                            bundle.putString("name", actsLists[position].name)
                            activity?.let { IntentUtils.to(it, OrgActsReportActivity::class.java, bundle) }
                        }
                    }
                    R.id.tv_back_reason -> {//驳回原因
                        if (StringUtils.isEmpty(actsLists[position].reason)) {
                            ToastUtils.showShort(activity, getString(R.string.back_reason) + getString(R.string.is_null))
                        } else {
                            SetSuccessDialog.Builder(activity!!)
                                    .setTitle(getString(R.string.back_reason))
                                    .setMessage(actsLists[position].reason)
                                    .setPositiveButton(getString(R.string.sure), DialogInterface.OnClickListener { p0, p1 ->
                                        p0.dismiss()
                                    })
                                    .setWith(0.77f)
                                    .create()
                                    .show()
                        }

                    }
                    R.id.tv_edit -> {//修改
                        var bundle = Bundle()
                        bundle.putString("id", actsLists[position].id)
                        activity?.let { IntentUtils.to(it, PublishActsActivity::class.java, bundle) }
                    }
                    R.id.tv_delete -> {//删除
                        var map = HashMap<String, String>()
                        map.put("id", actsLists[position].id)
                        map.put("state", "3")
                        mPresenter.actsManage(map)
                    }
                    R.id.tv_publish -> {//发布
                        var map = HashMap<String, String>()
                        map.put("id", actsLists[position].id)
                        map.put("state", "1")
                        mPresenter.actsManage(map)
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
        smartRefresh.finishRefresh()
        smartRefresh.finishLoadMore()
        loadingDialog.dismiss()
    }

    override fun setModelsData(data: List<HomeActivityListBean>) {

    }

    override fun showError(msg: String, errorCode: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun setOrgActs(data: List<EventsBean>) {
        actsLists.addAll(data)
        orgListAdapter?.notifyDataSetChanged()
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

    override fun backDraftResult(msg: String) {
        ToastUtils.showShort(activity, msg)
        actsLists.removeAt(curPosition)
        orgListAdapter?.notifyItemRemoved(curPosition)
        orgListAdapter?.notifyItemRangeChanged(curPosition, actsLists.size - curPosition)
    }

    override fun onReceiveEvent(event: EventBusMessage<Any>) {
        when (event.code) {
            Const.ACTS_PIC_UPLOAD -> {
                smartRefresh.autoRefresh()
            }
        }
    }

}