package com.wmsj.baselibs.ui.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.OrgSearchContract
import com.wmsj.baselibs.mvp.presenter.OrgSearchPresenter
import com.wmsj.baselibs.ui.adapter.ActivityListAdapter
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.ArticileOrgList
import com.wmsj.baselibs.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_org_top.*

/**
 * 查找组织
on 2019/9/12.
 */
class OrgSearchActivity : BaseActivity(), OrgSearchContract.View {

    private val mPresenter by lazy { OrgSearchPresenter() }
    private var page: Int = 1
    private var keyword = ""
    private var pagesize: Int = 8
    private var orgList = ArrayList<ArticileOrgList>()
    private val mOrgSearchAdapter by lazy { ActivityListAdapter(this, orgList, R.layout.activity_list_item) }

    override fun layoutId(): Int {
        return R.layout.activity_org_top
    }

    override fun initData(savedInstanceState: Bundle?) {
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.org_search))
        et_search.hint = getString(R.string.input_org_name)
    }

    override fun initView() {
        mPresenter.attachView(this)
        smartRefresh.setOnRefreshListener {
            orgList?.clear()
            page = 1
            mPresenter.requestModelsData(keyword, page.toString())
            smartRefresh.setNoMoreData(false)
        }
        smartRefresh.setOnLoadMoreListener {
            page++
            mPresenter.requestModelsData(keyword, page.toString())
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mOrgSearchAdapter
        //点击键盘的搜索按钮
        et_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                keyword = et_search.text.toString()
                smartRefresh.autoRefresh()
                // 当按了搜索之后关闭软键盘
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                        currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
            false
        }
    }

    override fun start() {
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
        smartRefresh.finishRefresh()
        smartRefresh.finishLoadMore()
    }

    override fun setModelsData(data: List<ArticileOrgList>) {
        orgList.addAll(data)
        mOrgSearchAdapter?.notifyDataSetChanged()
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
        ToastUtils.showShort(this,msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}