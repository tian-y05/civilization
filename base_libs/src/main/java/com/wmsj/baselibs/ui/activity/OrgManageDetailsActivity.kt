package com.wmsj.baselibs.ui.activity

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.OrgDetailsContract
import com.wmsj.baselibs.mvp.presenter.OrgDeatilsPresenter
import com.wmsj.baselibs.base.BaseActivity
import com.wmsj.baselibs.bean.HangupDetailBean
import com.wmsj.baselibs.bean.OrgBaseInfoBean
import com.wmsj.baselibs.bean.OrgDetailBean
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.eventBus.EventBusUtils
import com.wmsj.baselibs.utils.StringUtils
import com.wmsj.baselibs.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_sort_details.*

/**
 * 挂靠组织详情
 */
class OrgManageDetailsActivity : BaseActivity(), OrgDetailsContract.View {


    private val TAG = OrgManageDetailsActivity::class.java.simpleName
    private var id: String = ""
    private var state: String = ""
    private var type: String = ""
    private val mPresenter by lazy { OrgDeatilsPresenter() }

    override fun layoutId(): Int {
        return R.layout.activity_sort_details
    }

    override fun initData(savedInstanceState: Bundle?) {
        id = intent.getStringExtra("id")
        state = intent.getStringExtra("state")
        type = intent.getStringExtra("type")
        mPresenter.attachView(this)
        initWhiteActionBar(View.VISIBLE, View.OnClickListener {
            finish()
        }, getString(R.string.organize_detail))

    }

    override fun initView() {
    }

    override fun start() {
        mPresenter.orgManageDetail(id)
    }


    override fun showLoading() {
        loadingDialog.show()
    }

    override fun dismissLoading() {
        loadingDialog.dismiss()
    }

    override fun setOrgDetails(data: OrgDetailBean) {
    }

    override fun setOrgInfoDetails(data: OrgBaseInfoBean) {
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        ToastUtils.showShort(this, errorMsg)
    }

    override fun joinOrgResult(data: String) {
    }

    override fun exitOrgResult(data: String) {
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


    override fun orgManageDetail(data: HangupDetailBean) {
        data?.let {
            var requestOptions = RequestOptions()
            requestOptions.error(R.mipmap.zhi_default).placeholder(R.mipmap.zhi_default)
            Glide.with(this)
                    .load(R.mipmap.zhi_default)
                    .apply(requestOptions)
                    .into(iv_logo)
            tv_name.text = data.name
            tv_area.text = "地址:" + data.address
            tv_service_duration.text = data.service_count
            tv_member_num.text = data.membernum.toString()
            tv_create_time.text = StringUtils.split(data.time, " ")[0]
            tv_leader.text = data.duty_name
            tv_desc.text = "\u3000\u3000" + data.desc
            tv_contacts_phone.text = data.phone
            ll_organize_activity.visibility = View.INVISIBLE
            when (state) {
                "1" -> {// 申请挂靠

                }
                "2" -> {//已申请挂靠,请等待审核
                    ll_bottom.visibility = View.VISIBLE
                    tv_join_organize.visibility = View.VISIBLE
                    tv_join_wait.visibility = View.GONE
                    tv_apply_for_registration.visibility = View.GONE
                    tv_join_organize.background = resources.getDrawable(R.color.move_hungup)
                    tv_join_organize.text = getString(R.string.move_hungup_wait)
                }
                "3" -> {//撤销挂靠
                    ll_bottom.visibility = View.VISIBLE
                    tv_join_organize.visibility = View.VISIBLE
                    tv_join_wait.visibility = View.GONE
                    tv_apply_for_registration.visibility = View.GONE
                    tv_join_organize.background = resources.getDrawable(R.color.tab_select)
                    tv_join_organize.text = getString(R.string.move_hungup)
                }
                "4" -> {//移除挂靠
                    ll_bottom.visibility = View.VISIBLE
                    tv_join_organize.visibility = View.VISIBLE
                    tv_join_wait.visibility = View.GONE
                    tv_apply_for_registration.visibility = View.GONE
                    tv_join_organize.text = getString(R.string.delete_hungup)
                    tv_join_organize.background = resources.getDrawable(R.color.tab_select)
                }
                "5", "6" -> {//通过
                    ll_bottom.visibility = View.VISIBLE
                    tv_join_organize.visibility = View.VISIBLE
                    tv_join_wait.visibility = View.GONE
                    tv_apply_for_registration.visibility = View.VISIBLE
                    tv_join_organize.text = getString(R.string.go_on)
                    tv_apply_for_registration.text = getString(R.string.refuse)
                    tv_join_organize.background = resources.getDrawable(R.color.tab_select)
                    tv_apply_for_registration.background = resources.getDrawable(R.color.org_hungup)
                }

            }
            tv_join_organize.setOnClickListener {
                mPresenter.hangupOrgOperation(if (state == "2") "3" else state, data.id)
            }
            tv_apply_for_registration.setOnClickListener {
                mPresenter.hangupOrgOperation("6", data.id)
            }
        }
    }

    override fun operationResult(data: String) {
        finish()
        EventBusUtils.post(EventBusMessage(Const.ORG_MANAGE, type))
    }

}