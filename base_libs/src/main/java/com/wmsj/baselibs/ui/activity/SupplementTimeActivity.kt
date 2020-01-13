package com.wmsj.baselibs.ui.activity

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.RecuritMemberContract
import com.wmsj.baselibs.mvp.presenter.RecruitMemberPresenter
import com.wmsj.baselibs.ui.adapter.SupplementTimeAdapter
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.ActsPerson
import com.wmsj.baselibs.bean.Member
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.recyclerview.adapter.OnItemClickListener
import com.wmsj.baselibs.utils.L
import com.wmsj.baselibs.utils.StringUtils
import com.wmsj.baselibs.utils.ToastUtils
import com.wmsj.baselibs.view.RecycleViewDivider
import com.wmsj.baselibs.view.SupplementDialog
import kotlinx.android.synthetic.main.activity_supplement_time.*

/**
 * 时长录入
on 2019/9/3.
 */
class SupplementTimeActivity : BaseActivity(), RecuritMemberContract.View {


    private var id = ""
    private var curPos = -1
    private var keyword = ""
    private var actsList = ArrayList<Member>()
    private var memberSelectList = ArrayList<String>()
    private val mPresenter by lazy { RecruitMemberPresenter() }
    private val mTimeAdapter by lazy { this?.let { SupplementTimeAdapter(it, actsList, R.layout.supplement_time_item) } }
    private var page: Int = 1
    private var pagesize: Int = 8
    private var isAll = false

    override fun layoutId(): Int {
        return R.layout.activity_supplement_time
    }

    override fun initData(savedInstanceState: Bundle?) {
        id = intent.getStringExtra("id")
    }

    override fun initView() {
        mPresenter.attachView(this)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.supplement_time))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mTimeAdapter
        recyclerView.addItemDecoration(RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 2, resources.getColor(R.color.under_line)))

        smartRefresh.setOnRefreshListener {
            actsList.clear()
            page = 1
            mPresenter.requestModelsData("1", id, "", page.toString())
            smartRefresh.setNoMoreData(false)
        }
        smartRefresh.setOnLoadMoreListener {
            page++
            mPresenter.requestModelsData("1", id, "", page.toString())
        }
        mTimeAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(obj: Any?, position: Int) {
                if (ll_employ_member.visibility == View.VISIBLE) {
                    actsList[position].isSelect = !actsList[position].isSelect
                    if (actsList[position].isSelect) {
                        memberSelectList.add(actsList[position].id)
                    } else {
                        memberSelectList.remove(actsList[position].id)
                    }
                    mTimeAdapter.notifyDataSetChanged()
                    tv_sure.text = getString(R.string.sure) + "(" + memberSelectList.size + ")"
                }
            }
        })

        //点击键盘的搜索按钮
        et_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                keyword = et_search.text.toString()
                page = 1
                actsList.clear()
                loadingDialog.show()
                mPresenter.requestModelsData("1", id, keyword, page.toString())
                // 当按了搜索之后关闭软键盘
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                        currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
            false
        }

        tv_employ_member.setOnClickListener {
            tv_employ_member.visibility = View.GONE
            ll_employ_member.visibility = View.VISIBLE
            rl_top.visibility = View.VISIBLE
        }

        tv_cancel.setOnClickListener {
            tv_employ_member.visibility = View.VISIBLE
            ll_employ_member.visibility = View.GONE
            rl_top.visibility = View.GONE
            memberSelectList.clear()
            actsList.forEach {
                it.isSelect = false
            }
            mTimeAdapter.notifyDataSetChanged()
        }
        tv_sure.setOnClickListener {
            if (memberSelectList.size > 0) {
                supplementDialogShow()
            } else {
                ToastUtils.showShort(this, getString(R.string.choose_null))
            }
        }

        tv_all.setOnClickListener {
            isAll = !isAll
            if (isAll) {
                tv_all.background = resources.getDrawable(R.color.tab_select)
                actsList.forEach {
                    it.isSelect = isAll
                    memberSelectList.add(it.id)
                }
                tv_sure.text = getString(R.string.sure) + "(" + memberSelectList.size + ")"
            } else {
                actsList.forEach {
                    it.isSelect = isAll
                }
                memberSelectList.clear()
                tv_all.background = resources.getDrawable(R.color.activity_bottom)
                tv_sure.text = getString(R.string.sure)
            }
            mTimeAdapter.notifyDataSetChanged()

        }
        mTimeAdapter?.setOnItemChildListener(object : CommonRecyclerAdapter.OnItemChildListener {
            override fun onItemChidClick(v: View, position: Int) {
                curPos = position
                when (v.id) {
                    R.id.tv_enter -> {
                        memberSelectList.add(actsList[position].id)
                        supplementDialogShow()
                    }
                }
            }
        })
    }

    private fun supplementDialogShow() {
        SupplementDialog.Builder(this@SupplementTimeActivity)
                .setTitle(getString(R.string.time_enter))
                .setPositiveButton(getString(R.string.sure), object : SupplementDialog.OnClickListener {
                    override fun onClick(dialog: DialogInterface, time: String, ratingBar: Int) {
                        dialog.dismiss()
                        if (StringUtils.isEmpty(time) || ratingBar == 0) {
                            ToastUtils.showShort(this@SupplementTimeActivity, getString(R.string.input_time))
                        } else {
                            var map = HashMap<String, String>()
                            map.put("aid", id)
                            map.put("ids", StringUtils.stringSpell(memberSelectList).toString())
                            map.put("servicetime", time)
                            map.put("servicestar", ratingBar.toString())
                            mPresenter.timeEnter(map)
                            L.d("timeEnter:" + map.toString())
                        }

                    }

                })
                .setWith(0.77f)
                .create()
                .show()
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

    override fun setModelsData(data: ActsPerson) {
        keyword = ""
        actsList.addAll(data.list)
        mTimeAdapter.notifyDataSetChanged()
        if (page == 1) {
            smartRefresh.finishRefresh()
        } else {
            if ((data != null && data.list.isEmpty())) {
                smartRefresh.finishLoadMoreWithNoMoreData()
            } else if (data != null && data.list.size < pagesize) {
                smartRefresh.finishLoadMore()
            }
        }
    }

    override fun cancelResult(data: String) {
        ToastUtils.showShort(this, data)
        page = 1
        smartRefresh.autoRefresh()
    }

    override fun showError(msg: String, errorCode: Int) {
        ToastUtils.showShort(this, msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}