package com.wmsj.baselibs.ui.fragment

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.VolunteerContract
import com.wmsj.baselibs.mvp.presenter.VolunteerPresenter
import com.wmsj.baselibs.base.BaseFragment
import com.wmsj.baselibs.bean.OrgBaseInfoBean
import com.wmsj.baselibs.bean.UserCentreBean
import com.wmsj.baselibs.eventBus.EventBusMessage
import com.wmsj.baselibs.eventBus.EventBusUtils
import com.wmsj.baselibs.ui.activity.*
import com.wmsj.baselibs.utils.IntentUtils
import com.wmsj.baselibs.utils.SPUtil
import com.wmsj.baselibs.utils.StringUtils
import com.wmsj.baselibs.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_voluntary_organization.*

/**
 * 志愿组织
 * Created by tian
on 2019/7/16.
 */
class VoluntaryOrganizationFragment : BaseFragment(), VolunteerContract.View, View.OnClickListener {


    private var mTitle: String? = null
    private var orgInfo: OrgBaseInfoBean? = null
    private val mPresenter by lazy { VolunteerPresenter() }

    companion object {
        fun getInstance(title: String): VoluntaryOrganizationFragment {
            val fragment = VoluntaryOrganizationFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_voluntary_organization
    }

    override fun initView() {
        mPresenter.attachView(this)
//        initWhiteActionBar(-1, null, mTitle)
        ll_publish.setOnClickListener(this)
        ll_manage_activity.setOnClickListener(this)
        ll_manage_member.setOnClickListener(this)
        ll_base_message.setOnClickListener(this)
        ll_manage_org.setOnClickListener(this)
        ll_account_security.setOnClickListener(this)
        ll_safe_exit.setOnClickListener(this)
        ll_safe_exit_two.setOnClickListener(this)
        ll_base_message_two.setOnClickListener(this)
    }

    override fun lazyLoad() {
        if (!StringUtils.isEmpty(SPUtil.get(activity, "userId", "").toString())) {
            mPresenter.getOrgInfo(SPUtil.get(activity, "userId", "").toString())
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_publish -> {
                var bundle = Bundle()
                bundle.putString("contact", orgInfo?.contact)
                bundle.putString("contact_mobile", orgInfo?.contact_mobile)
                bundle.putString("zgorgname", orgInfo?.zgorgname)
                activity?.let { IntentUtils.to(it, PublishActsActivity::class.java, bundle) }
            }
            R.id.ll_manage_activity -> {
                activity?.let { IntentUtils.to(it, ManageOrgActsActivity::class.java) }
            }
            R.id.ll_manage_member -> {
                activity?.let { IntentUtils.to(it, OrgMemberManageActivity::class.java) }
            }
            R.id.ll_base_message, R.id.ll_base_message_two -> {
                orgInfo?.let {
                    var bundle = Bundle()
                    bundle.putSerializable("orginfo", orgInfo)
                    activity?.let { IntentUtils.to(it, PerfectOrgInfoActivity::class.java, bundle) }
                }
            }
            R.id.ll_manage_org -> {
                activity?.let { IntentUtils.to(it, OrgManageActivity::class.java) }
            }
            R.id.ll_account_security -> {
                var bundle = Bundle()
                bundle.putSerializable("role", "org")
                activity?.let { IntentUtils.to(it, AccountSecurityActivity::class.java, bundle) }
            }
            R.id.ll_safe_exit, R.id.ll_safe_exit_two -> {
                SPUtil.clear(activity)
                EventBusUtils.post(EventBusMessage(Const.LOGIN_EXIT, mTitle))
            }
        }
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun dismissLoading() {
        loadingDialog.dismiss()
    }

    override fun userCentreResult(data: UserCentreBean) {
    }

    override fun orgInfoResult(data: OrgBaseInfoBean) {
        orgInfo = data
        var requestOptions = RequestOptions()
        requestOptions.error(R.mipmap.author_default).placeholder(R.mipmap.author_default)
        activity?.let { Glide.with(it).load(Const.BASE_URL + data.logo).apply(requestOptions).into(iv_logo) }
        SPUtil.put(activity, "zgorgname", data.zgorgname)
        tv_name.text = data.org_cname
        tv_number.text = data.org_code
        tv_service_count.text = data.time + "小时"
        when (data.state) {
            1 -> { //正常
                iv_state.visibility = View.GONE
                ll_state.visibility = View.GONE
                rl_create.visibility = View.VISIBLE
                ll_one.visibility = View.VISIBLE
                ll_two.visibility = View.VISIBLE
                ll_three.visibility = View.GONE
            }
            2 -> {
                iv_state.visibility = View.VISIBLE
                ll_state.visibility = View.VISIBLE
                rl_create.visibility = View.GONE
                ll_one.visibility = View.GONE
                ll_two.visibility = View.GONE
                ll_three.visibility = View.VISIBLE
                Glide.with(activity!!).load(R.mipmap.in_review).into(iv_state)
                tv_state_message.text = "请耐心等待审核，审核通过可使用相应功能"
            }
            4 -> {
                iv_state.visibility = View.VISIBLE
                ll_state.visibility = View.VISIBLE
                rl_create.visibility = View.GONE
                ll_one.visibility = View.GONE
                ll_two.visibility = View.GONE
                ll_three.visibility = View.VISIBLE
                Glide.with(activity!!).load(R.mipmap.rejected).into(iv_state)
                tv_state_message.text = data.message
            }
            else -> {
                iv_state.visibility = View.GONE
                ll_state.visibility = View.VISIBLE
                rl_create.visibility = View.GONE
                ll_one.visibility = View.GONE
                ll_two.visibility = View.GONE
                ll_three.visibility = View.GONE
                tv_state_message.text = data.message
            }
        }
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        ToastUtils.showShort(activity, errorMsg)
    }

    override fun onReceiveEvent(event: EventBusMessage<Any>) {
        when (event.code) {
            Const.PERFECT_ORG_SUCCESS, Const.LOGIN_ORG_SUCCESS -> {
                if (!StringUtils.isEmpty(SPUtil.get(activity, "userId", "").toString())) {
                    mPresenter.getOrgInfo(SPUtil.get(activity, "userId", "").toString())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}