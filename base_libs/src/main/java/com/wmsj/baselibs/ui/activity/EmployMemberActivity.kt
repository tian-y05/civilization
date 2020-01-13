package com.wmsj.baselibs.ui.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.EmployMemberContract
import com.wmsj.baselibs.mvp.presenter.EmployMemberPresenter
import com.wmsj.baselibs.ui.adapter.EmployMemberAdapter
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.OrgMemberBean
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.utils.SPUtil
import com.wmsj.baselibs.utils.StringUtils
import com.wmsj.baselibs.utils.ToastUtils
import com.wmsj.baselibs.view.RecycleViewDivider
import kotlinx.android.synthetic.main.activity_employ_member.*

/**
 * 录用本组成员
on 2019/9/3.
 */
class EmployMemberActivity : BaseActivity(), EmployMemberContract.View {


    private var id = ""
    private var keyword = ""
    private var actsList = ArrayList<OrgMemberBean>()
    private var memberSelect = ArrayList<String>()
    private val mPresenter by lazy { EmployMemberPresenter() }
    private val mEmployMemberAdapter by lazy { this?.let { EmployMemberAdapter(it, actsList, R.layout.employ_member_item) } }
    private var page: Int = 1
    private var pagesize: Int = 8

    override fun layoutId(): Int {
        return R.layout.activity_employ_member
    }

    override fun initData(savedInstanceState: Bundle?) {
        id = intent.getStringExtra("id")
    }

    override fun initView() {
        mPresenter.attachView(this)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.employ_member))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mEmployMemberAdapter
        recyclerView.addItemDecoration(RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 2, resources.getColor(R.color.under_line)))

        smartRefresh.setOnRefreshListener {
            actsList.clear()
            page = 1
            orgMember()
            smartRefresh.setNoMoreData(false)
        }
        smartRefresh.setOnLoadMoreListener {
            page++
            orgMember()
        }
        mEmployMemberAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                actsList[position].isSelect = !actsList[position].isSelect
                if (actsList[position].isSelect) {
                    memberSelect.add(actsList[position].id)
                } else {
                    memberSelect.remove(actsList[position].id)
                }
                mEmployMemberAdapter.notifyDataSetChanged()
            }
        })

        //点击键盘的搜索按钮
        et_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                keyword = et_search.text.toString()
                page = 1
                actsList.clear()
                loadingDialog.show()
                orgMember()
                // 当按了搜索之后关闭软键盘
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                        currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
            false
        }

        tv_employ_member.setOnClickListener {
            var map = HashMap<String, String>()
            map.put("id", StringUtils.stringSpell(memberSelect).toString())
            map.put("aid", id)
            map.put("type", "1")
            mPresenter.orgEmployment(map)
        }
        ll_all.setOnClickListener {
            memberSelect.clear()
            if (cb_all.isChecked) {
                cb_all.isChecked = false
                actsList
                        .filter { it.join != "1" }
                        .forEach { it.isSelect = false }
            } else {
                cb_all.isChecked = true
                actsList
                        .filter { it.join != "1" }
                        .forEach {
                            it.isSelect = true
                            memberSelect.add(it.id)
                        }
            }

            mEmployMemberAdapter.notifyDataSetChanged()
        }

    }

    /**
     * 获取组织成员
     */
    private fun orgMember() {
        var map = HashMap<String, String>()
        map.put("id", SPUtil.get(this, "userId", "").toString())
        map.put("aid", id)
        map.put("keyword", keyword)
        map.put("page", page.toString())
        map.put("pagesize", "8")
        mPresenter.requestModelsData(map)
    }

    override fun start() {
        smartRefresh.autoRefresh()
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun dismissLoading() {
        loadingDialog.dismiss()
        smartRefresh.finishRefresh()
        smartRefresh.finishLoadMore()
    }

    override fun setModelsData(data: List<OrgMemberBean>) {
        actsList.addAll(data)

        mEmployMemberAdapter.notifyDataSetChanged()
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

    override fun employmentResult(data: String) {
        ToastUtils.showShort(this, data)
        finish()
    }

    override fun showError(msg: String, errorCode: Int) {
        ToastUtils.showShort(this, msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}