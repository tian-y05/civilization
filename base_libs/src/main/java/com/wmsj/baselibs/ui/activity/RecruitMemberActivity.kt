package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.RelativeLayout
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.RecuritMemberContract
import com.wmsj.baselibs.mvp.presenter.RecruitMemberPresenter
import com.wmsj.baselibs.ui.adapter.RecruitMemberAdapter
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.ActsPerson
import com.wmsj.baselibs.bean.Member
import com.wmsj.baselibs.recyclerview.adapter.CommonRecyclerAdapter
import com.wmsj.baselibs.utils.IntentUtils
import com.wmsj.baselibs.utils.ToastUtils
import com.wmsj.baselibs.view.RecycleViewDivider
import kotlinx.android.synthetic.main.activity_recruit_member.*


/**
 * 招募人数
on 2019/9/3.
 */
class RecruitMemberActivity : BaseActivity(), RecuritMemberContract.View {

    private var id = ""
    private var type = ""
    private var actsList = ArrayList<Member>()
    private val mPresenter by lazy { RecruitMemberPresenter() }
    private val mRecruitMemberAdapter by lazy { this?.let { RecruitMemberAdapter(it, actsList, R.layout.recruit_member_item) } }
    private var page: Int = 1
    private var pagesize: Int = 8
    private var curPosition = -1

    override fun layoutId(): Int {
        return R.layout.activity_recruit_member
    }

    override fun initData(savedInstanceState: Bundle?) {
        id = intent.getStringExtra("id")
        type = intent.getStringExtra("type")
    }

    override fun initView() {
        mPresenter.attachView(this)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, when (type) {
            "1" -> getString(R.string.activity_number)
            "2" -> getString(R.string.pending_trial)
            "3" -> getString(R.string.number_of_applicants)
            else -> getString(R.string.activity_number)
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mRecruitMemberAdapter
        recyclerView.addItemDecoration(RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 2, resources.getColor(R.color.under_line)))
        mRecruitMemberAdapter.setType(type)

        smartRefresh.setOnRefreshListener {
            actsList.clear()
            page = 1
            mPresenter.requestModelsData(type, id, "", page.toString())
            smartRefresh.setNoMoreData(false)
        }
        smartRefresh.setOnLoadMoreListener {
            page++
            mPresenter.requestModelsData(type, id, "", page.toString())
        }
        mRecruitMemberAdapter.setOnItemChildListener(object : CommonRecyclerAdapter.OnItemChildListener {
            override fun onItemChidClick(v: View, position: Int) {
                curPosition = position
                when (v.id) {
                    R.id.iv_logo -> {
                        var bundle = Bundle()
                        bundle.putString("id", actsList[position].id)
                        IntentUtils.to(this@RecruitMemberActivity, OrgMemberDeatailActivity::class.java, bundle)
                    }
                    R.id.tv_cancel -> {
                        mPresenter.cancelSign(actsList[position].id, "1")
                    }
                    R.id.tv_employment -> {
                        mPresenter.cancelSign(actsList[position].id, "2")
                    }
                    R.id.tv_refuse -> {
                        mPresenter.cancelSign(actsList[position].id, "3")
                    }
                }
            }
        })

        ll_employ_member.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("id", id)
            IntentUtils.to(this, EmployMemberActivity::class.java, bundle)
        }
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
        when (type) {
            "1" -> {
                tv_number.text = "已招募" + data.list.size + "/" + data.recruitment
            }
            "2" -> {
                ll_employ_member.visibility = View.VISIBLE
                tv_number.text = "待审核" + data.list.size + "人"
                var lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                lp.bottomMargin = resources.getDimensionPixelSize(R.dimen.dp_45)
                smartRefresh.layoutParams = lp
            }
            "3" -> {
                tv_number.text = "参与人数" + data.list.size + "人"
            }
        }

        actsList.addAll(data.list)

        mRecruitMemberAdapter.notifyDataSetChanged()
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
        actsList.removeAt(curPosition)
        mRecruitMemberAdapter.notifyItemRemoved(curPosition)
        mRecruitMemberAdapter.notifyItemRangeChanged(curPosition, actsList.size - curPosition)
    }

    override fun showError(msg: String, errorCode: Int) {
        ToastUtils.showShort(this, msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}