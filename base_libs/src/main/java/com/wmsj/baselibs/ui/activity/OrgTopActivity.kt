package com.wmsj.baselibs.ui.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.OrgManageContract
import com.wmsj.baselibs.mvp.presenter.OrgManagePresenter
import com.wmsj.baselibs.ui.adapter.OrgManageListAdapter
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.HangupOrgBean
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.utils.IntentUtils
import com.wmsj.baselibs.utils.L
import com.wmsj.baselibs.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_org_top.*


/**
 * 上级组织
 */
class OrgTopActivity : BaseActivity(), OrgManageContract.View {


    private val mPresenter by lazy { OrgManagePresenter() }
    private var orgList = ArrayList<HangupOrgBean>()
    private val mOrgListAdapter by lazy { OrgManageListAdapter(this, orgList, R.layout.activity_list_item) }
    private var curPos: Int = -1

    override fun layoutId(): Int {
        return R.layout.activity_org_top
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initView() {
        mPresenter.attachView(this)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.org_top))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mOrgListAdapter
        mOrgListAdapter.setType(1)

        smartRefresh.setOnRefreshListener {
            orgList.clear()
            mPresenter.requestModelsData("1")
            smartRefresh.setNoMoreData(false)
        }

        mOrgListAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                curPos = position
                var bundle = Bundle()
                bundle.putString("id", orgList[position].id)
                bundle.putString("state", orgList[position].state)
                bundle.putString("type", "1")
                IntentUtils.to(this@OrgTopActivity, OrgManageDetailsActivity::class.java, bundle)
            }

        })

        mOrgListAdapter?.setOnItemChildListener(object : CommonRecyclerAdapter.OnItemChildListener {
            override fun onItemChidClick(v: View, position: Int) {
                curPos = position
                L.d("mOrgListAdapter" + orgList[position].state + ";" + orgList[position].id)
                when (v.id) {
                    R.id.tv_right -> {
                        mPresenter.hangupOrgOperation(if (orgList[position].state == "2") "3" else orgList[position].state, orgList[position].id)
                    }
                    R.id.tv_left -> {
                        mPresenter.hangupOrgOperation("6", orgList[position].id)
                    }
                }
            }
        })

        //点击键盘的搜索按钮
        et_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mPresenter.hangupOrgSearch(et_search.text.toString())
                // 当按了搜索之后关闭软键盘
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                        currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
            false
        }
    }

    override fun start() {
        smartRefresh.autoRefresh()
    }

    override fun setModelsData(data: List<HangupOrgBean>) {
        orgList.addAll(data)
        mOrgListAdapter.notifyDataSetChanged()
        smartRefresh.finishRefresh()

    }

    override fun showError(msg: String, errorCode: Int) {
        ToastUtils.showShort(this, msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun dismissLoading() {
        smartRefresh.finishRefresh()
        smartRefresh.finishLoadMore()
        loadingDialog.dismiss()
    }


    override fun operationResult(data: String) {
        ToastUtils.showShort(this, data)
        orgList.removeAt(curPos)
        mOrgListAdapter.notifyItemRemoved(curPos)
        mOrgListAdapter?.notifyItemRangeChanged(curPos, orgList.size - curPos)
    }

    override fun onReceiveEvent(event: EventBusMessage<Any>) {
        when (event.code) {
            Const.ORG_MANAGE -> {
                orgList.removeAt(curPos)
                mOrgListAdapter.notifyItemRemoved(curPos)
                mOrgListAdapter?.notifyItemRangeChanged(curPos, orgList.size - curPos)
            }
        }
    }
}