package com.wmsj.baselibs.ui.fragment

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wmsj.baselibs.Const
import com.wmsj.baselibs.R
import com.wmsj.baselibs.mvp.contract.VolunteerContract
import com.wmsj.baselibs.mvp.presenter.VolunteerPresenter
import com.wmsj.baselibs.weight.QrCodeDialog
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
import kotlinx.android.synthetic.main.fragment_volunteer.*


/**
 * 志愿者
 * Created by tian
on 2019/7/16.
 */
class VolunteerFragment : BaseFragment(), VolunteerContract.View, View.OnClickListener {


    private val mPresenter by lazy { VolunteerPresenter() }

    private var mTitle: String? = null

    companion object {
        fun getInstance(title: String): VolunteerFragment {
            val fragment = VolunteerFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_volunteer
    }

    override fun initView() {
        mPresenter.attachView(this)
//        initWhiteActionBar(-1, null, mTitle)
        ll_my_activity.setOnClickListener(this)
        ll_my_org.setOnClickListener(this)
        ll_my_take_photo.setOnClickListener(this)
        ll_service_records.setOnClickListener(this)
        ll_check_in_card.setOnClickListener(this)
        ll_base_message.setOnClickListener(this)
        ll_system_message.setOnClickListener(this)
        ll_account_security.setOnClickListener(this)
        ll_safe_exit.setOnClickListener(this)

    }

    override fun lazyLoad() {
        mPresenter.getUserCentre(SPUtil.get(activity, "userId", "") as String)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_my_activity -> {
                activity?.let { IntentUtils.to(it, MyActsActivity::class.java) }
            }
            R.id.ll_my_org -> {
                activity?.let { IntentUtils.to(it, MyOrgActivity::class.java) }
            }
            R.id.ll_my_take_photo -> {
                activity?.let { IntentUtils.to(it, MyTakePhotoActivity::class.java) }
            }
            R.id.ll_service_records -> {
                activity?.let { IntentUtils.to(it, ServiceRecordActivity::class.java) }
            }
            R.id.ll_check_in_card -> {
                activity?.let { IntentUtils.to(it, CheckCardActivity::class.java) }
            }
            R.id.ll_base_message -> {
                activity?.let { IntentUtils.to(it, PerfectInformationActivity::class.java) }
            }
            R.id.ll_system_message -> {
                activity?.let { IntentUtils.to(it, SystemMessageActivity::class.java) }
            }
            R.id.ll_account_security -> {
                var bundle = Bundle()
                bundle.putSerializable("role", "zyz")
                activity?.let { IntentUtils.to(it, AccountSecurityActivity::class.java, bundle) }
            }
            R.id.ll_safe_exit -> {
                SPUtil.clear(activity)
                EventBusUtils.post(EventBusMessage(Const.LOGIN_EXIT, mTitle))
            }
        }
    }

    override fun onReceiveEvent(event: EventBusMessage<Any>) {
        when (event.code) {
            Const.LOGIN_VOL_SUCCESS, Const.PERFECT_INFO_SUCCESS -> {
                if (SPUtil.get(activity, "isVolLogin", false) as Boolean) {
                    mPresenter.getUserCentre(SPUtil.get(activity, "userId", "") as String)
                }
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
        activity?.let {
            var requestOptions = RequestOptions()
            requestOptions.error(R.mipmap.author_default).placeholder(R.mipmap.author_default)
            Glide.with(it).load(Const.BASE_URL + data.logo).apply(requestOptions).into(iv_logo)
            Glide.with(it).load(Const.BASE_URL + data.codeurl).apply(requestOptions).into(iv_codeurl)
        }
        tv_name.text = data.name
        tv_number.text = "NO." + data.volunteer_number
        if (StringUtils.isEmpty(data.service_count)) {
            tv_service_count.text = "0小时"
        } else {
            tv_service_count.text = data.service_count + "小时"
        }
        rating_bar.setStar(data.service_star.toFloat())
        iv_codeurl.setOnClickListener {
            activity?.let { it -> QrCodeDialog.buildDialog(it, Const.BASE_URL + data.codeurl).show() }
        }
        EventBusUtils.post(EventBusMessage(Const.ZYZ_INFO, data))
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        ToastUtils.showShort(activity, errorMsg)
    }

    override fun orgInfoResult(data: OrgBaseInfoBean) {
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}